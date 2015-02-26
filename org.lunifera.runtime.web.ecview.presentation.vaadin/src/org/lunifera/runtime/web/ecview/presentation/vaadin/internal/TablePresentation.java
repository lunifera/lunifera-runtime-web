/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.filter.IFilterablePresentation;
import org.lunifera.ecview.core.common.filter.IRefreshRowsPresentation;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableCollectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableMultiSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YFlatAlignment;
import org.lunifera.ecview.core.common.model.datatypes.YDatadescription;
import org.lunifera.ecview.core.databinding.emf.model.ECViewModelBindable;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YColumn;
import org.lunifera.ecview.core.extension.model.extension.YSelectionType;
import org.lunifera.ecview.core.extension.model.extension.YTable;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITableEditpart;
import org.lunifera.runtime.common.annotations.DtoUtils;
import org.lunifera.runtime.common.state.ISharedStateContext;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.data.EnumOptionBeanHelper;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.common.data.BeanServiceLazyLoadingContainer;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactory;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.UI;

/**
 * This presenter is responsible to render a table on the given layout.
 */
@SuppressWarnings("restriction")
public class TablePresentation extends AbstractFieldWidgetPresenter<Component>
		implements IFilterablePresentation, IRefreshRowsPresentation {

	private final ModelAccess modelAccess;
	private Table table;
	@SuppressWarnings("rawtypes")
	private ObjectProperty property;
	private boolean applyColumns;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public TablePresentation(IElementEditpart editpart) {
		super((ITableEditpart) editpart);
		this.modelAccess = new ModelAccess((YTable) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Component doCreateWidget(Object parent) {
		if (table == null) {

			table = new CustomTable();
			table.addStyleName(CSS_CLASS_CONTROL);
			table.setMultiSelect(modelAccess.yField.getSelectionType() == YSelectionType.MULTI);
			table.setSelectable(true);
			table.setImmediate(true);
			setupComponent(table, getCastedModel());

			associateWidget(table, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				table.setId(modelAccess.getCssID());
			} else {
				table.setId(getEditpart().getId());
			}

			if (table.isMultiSelect()) {
				property = new ObjectProperty(new HashSet(), Set.class);
			} else {
				if (modelAccess.yField.getType() != null) {
					property = new ObjectProperty(null,
							modelAccess.yField.getType());
				} else {
					property = new ObjectProperty(null, Object.class);
				}
			}
			table.setPropertyDataSource(property);

			applyColumns = false;
			if (modelAccess.yField.getType() == String.class) {
				IndexedContainer datasource = new IndexedContainer();
				table.setContainerDataSource(datasource);
				table.setItemCaptionMode(ItemCaptionMode.ID);
			} else {
				if (modelAccess.yField.getType() != null) {
					IBeanSearchService<?> service = null;
					IBeanSearchServiceFactory factory = getViewContext()
							.getService(
									IBeanSearchServiceFactory.class.getName());
					if (factory != null) {
						service = factory.createService(modelAccess.yField
								.getType());
					}
					if (modelAccess.yField.isUseBeanService()
							&& service != null) {
						ISharedStateContext sharedState = getViewContext()
								.getService(ISharedStateContext.class.getName());
						BeanServiceLazyLoadingContainer<?> datasource = new BeanServiceLazyLoadingContainer(
								service, modelAccess.yField.getType(),
								sharedState);
						table.setContainerDataSource(datasource);
					} else {
						DeepResolvingBeanItemContainer datasource = new DeepResolvingBeanItemContainer(
								modelAccess.yField.getType());
						table.setContainerDataSource(datasource);
					}
					applyColumns = true;
				} else {
					IndexedContainer container = new IndexedContainer();
					container.addContainerProperty("for", String.class, null);
					container.addContainerProperty("preview", String.class,
							null);
					container.addItem(new String[] { "Some value", "other" });
					table.setContainerDataSource(container);
				}
			}

			String itemImageProperty = modelAccess.yField
					.getItemImageProperty();
			if (itemImageProperty != null && !itemImageProperty.equals("")) {
				table.setItemIconPropertyId(itemImageProperty);
				table.setRowHeaderMode(RowHeaderMode.EXPLICIT);
			}

			// creates the binding for the field
			createBindings(modelAccess.yField, table);

			if (modelAccess.isCssClassValid()) {
				table.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(table);
		}
		return table;
	}

	@Override
	public void refreshRows() {
		if (isRendered()) {
			table.refreshRowCache();
		}
	}

	/**
	 * Applies the column setting to the table.
	 */
	@SuppressWarnings("serial")
	protected void applyColumns() {
		Class<?> type = modelAccess.yField.getType();

		// set the visible columns and icons
		List<String> columns = new ArrayList<String>();
		Collection<?> propertyIds = table.getContainerDataSource()
				.getContainerPropertyIds();

		for (YColumn yColumn : modelAccess.yField.getColumns()) {
			if (yColumn.isVisible()
					&& propertyIds.contains(yColumn.getPropertyPath())
					|| isNestedColumn(yColumn)) {
				columns.add(yColumn.getPropertyPath());
			}
		}

		// add nested properties
		if (table.getContainerDataSource() instanceof DeepResolvingBeanItemContainer) {
			DeepResolvingBeanItemContainer<?> container = (DeepResolvingBeanItemContainer<?>) table
					.getContainerDataSource();
			for (String property : columns) {
				if (property.contains(".")) {
					container.addNestedContainerProperty(property);
				}
			}
		}

		table.setVisibleColumns(columns.toArray(new Object[columns.size()]));
		table.setColumnCollapsingAllowed(true);

		final String dirtyProperty = getDirtyProperty(type);

		II18nService i18nService = getViewContext().getService(
				II18nService.class.getName());
		IResourceProvider resourceProvider = (IResourceProvider) getViewContext()
				.getService(IResourceProvider.class.getName());
		// traverse the columns again and set other properties
		for (YColumn yColumn : modelAccess.yField.getColumns()) {
			if (yColumn.isVisible()
					&& (propertyIds.contains(yColumn.getPropertyPath()) || isNestedColumn(yColumn))) {
				String columnId = yColumn.getPropertyPath();

				table.setColumnHeader(columnId, getColumnHeader(yColumn));
				table.setColumnAlignment(columnId,
						toAlign(yColumn.getAlignment()));
				table.setColumnCollapsed(columnId, yColumn.isCollapsed());
				table.setColumnCollapsible(columnId, yColumn.isCollapsible());
				if (yColumn.getExpandRatio() >= 0) {
					table.setColumnExpandRatio(columnId,
							yColumn.getExpandRatio());
				}
				if (yColumn.getIcon() != null && !yColumn.getIcon().equals("")) {
					if (i18nService != null) {
						String translation = i18nService.getValue(
								yColumn.getIcon(), UI.getCurrent().getLocale());
						table.setColumnIcon(columnId,
								resourceProvider.getResource(translation));
					} else {
						table.setColumnIcon(columnId,
								resourceProvider.getResource(yColumn.getIcon()));
					}
				}
			}
		}

		// if a dirty property is available, we use it
		if (dirtyProperty != null) {
			table.setCellStyleGenerator(new Table.CellStyleGenerator() {
				@Override
				public String getStyle(Table source, Object itemId,
						Object propertyId) {
					if (propertyId == null) {
						return null;
					}
					if (propertyId.equals(dirtyProperty)) {
						try {
							boolean dirty = DtoUtils.invokeDirtyGetter(itemId);
							return dirty ? "dirty" : null;
						} catch (IllegalAccessException e) {
						}
					}
					return null;
				}
			});
		}
	}

	protected String getDirtyProperty(Class<?> type) {
		String temp = null;
		java.lang.reflect.Field dirtyField = DtoUtils.getDirtyField(type);
		if (dirtyField != null) {
			temp = dirtyField.getName();
		}
		return temp;
	}

	protected boolean isNestedColumn(YColumn yColumn) {
		return yColumn.getPropertyPath() != null
				&& yColumn.getPropertyPath().contains(".");
	}

	/**
	 * Returns the column header
	 * 
	 * @param yColumn
	 * @return
	 */
	private String getColumnHeader(YColumn yColumn) {
		YDatadescription yDt = yColumn.getDatadescription();
		if (yDt == null) {
			return yColumn.getPropertyPath();
		}

		String result = null;
		II18nService service = getI18nService();
		if (service != null && yDt.getLabelI18nKey() != null) {
			result = service.getValue(yDt.getLabelI18nKey(), getLocale());
		}

		if (result == null || result.equals("")) {
			result = yDt.getLabel();
		}

		if (result == null || result.equals("")) {
			result = yColumn.getPropertyPath();
		}

		return result;
	}

	private Align toAlign(YFlatAlignment alignment) {
		switch (alignment) {
		case LEFT:
			return Align.LEFT;
		case CENTER:
			return Align.CENTER;
		case RIGHT:
			return Align.RIGHT;
		}
		return Align.LEFT;
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// no need to set the locale to the ui elements. Is handled by vaadin
		// internally.

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {

		// applies the column properties
		if (applyColumns) {
			applyColumns();
		}

		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), table);
	}

	@Override
	public void applyFilter(Object filter) {
		Container container = table.getContainerDataSource();
		if (container instanceof Container.Filterable) {
			Container.Filterable filterable = (Filterable) container;
			filterable.removeAllContainerFilters();
			if (filter != null) {
				filterable.addContainerFilter((Filter) filter);
			}
		}
	}

	@Override
	protected Field<?> doGetField() {
		return table;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new IllegalArgumentException(
					"BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableCollectionEndpoint) {
			return internalGetCollectionEndpoint();
		} else if (bindableValue instanceof YEmbeddableSelectionEndpoint) {
			return internalGetSelectionEndpoint((YEmbeddableSelectionEndpoint) bindableValue);
		} else if (bindableValue instanceof YEmbeddableMultiSelectionEndpoint) {
			return internalGetMultiSelectionEndpoint();
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe the collection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetCollectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YTABLE__COLLECTION).observe(
				getModel());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YTABLE__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yField.getType(),
				modelAccess.yField.getEmfNsURI());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetMultiSelectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YTABLE__MULTI_SELECTION)
				.observe(getModel());
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YTable yField, Table field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_ContainerContents(
				castEObject(getModel()),
				ExtensionModelPackage.Literals.YTABLE__COLLECTION, field,
				yField.getType()));

		// create the model binding from ridget to ECView-model
		if (modelAccess.yField.getSelectionType() == YSelectionType.MULTI) {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsMultiSelection(
					castEObject(getModel()),
					ExtensionModelPackage.Literals.YTABLE__MULTI_SELECTION,
					field, yField.getType()));
		} else {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsSelection(castEObject(getModel()),
					ExtensionModelPackage.Literals.YTABLE__SELECTION, field,
					yField.getType()));

		}

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return table;
	}

	@Override
	public boolean isRendered() {
		return table != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (table != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) table.getParent());
			if (parent != null) {
				parent.removeComponent(table);
			}

			// remove assocations
			unassociateWidget(table);

			table = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YTable yField;

		public ModelAccess(YTable yField) {
			super();
			this.yField = yField;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yField.getCssClass();
		}

		/**
		 * Returns true, if the css class is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssClassValid() {
			return getCssClass() != null && !getCssClass().equals("");
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yField.getCssID();
		}

		/**
		 * Returns true, if the css id is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssIdValid() {
			return getCssID() != null && !getCssID().equals("");
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabel() : null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabelI18nKey() : null;
		}
	}

	/**
	 * Converts the string value of the item icon property to
	 * {@link ThemeResource}.
	 */
	@SuppressWarnings("serial")
	private class CustomTable extends Table {
		private Object itemIconPropertyId;

		@Override
		public void setItemIconPropertyId(Object propertyId)
				throws IllegalArgumentException {
			if (propertyId == null) {
				super.setItemIconPropertyId(propertyId);
			} else if (!getContainerPropertyIds().contains(propertyId)) {
				// super.setItemIconPropertyId(propertyId);
			} else if (String.class.isAssignableFrom(getType(propertyId))) {
				itemIconPropertyId = propertyId;
			} else {
				super.setItemIconPropertyId(propertyId);
			}
		}

		public Object getItemIconPropertyId() {
			return itemIconPropertyId != null ? itemIconPropertyId : super
					.getItemIconPropertyId();
		}

		public Resource getItemIcon(Object itemId) {
			if (itemIconPropertyId == null) {
				return super.getItemIcon(itemId);
			} else {
				final Property<?> ip = getContainerProperty(itemId,
						getItemIconPropertyId());
				if (ip == null) {
					return null;
				}
				final Object icon = ip.getValue();
				if (icon instanceof String) {
					return new ThemeResource((String) icon);
				}
			}
			return null;
		}

		protected String formatPropertyValue(Object rowId, Object colId,
				Property<?> property) {
			if (property == null) {
				return "";
			}
			Converter<String, Object> converter = null;

			if (hasConverter(colId)) {
				converter = getConverter(colId);
			} else {
				converter = (Converter) ConverterUtil.getConverter(
						String.class, property.getType(), getSession());
			}
			Object value = property.getValue();
			if (converter != null) {
				return converter.convertToPresentation(value, String.class,
						getLocale());
			} else {
				if (value instanceof Enum<?>) {
					return formatEnum(value);
				} else if (value instanceof Boolean) {
					return formatBoolean((Boolean) value);
				}
			}
			return (null != value) ? value.toString() : "";
		}

		/**
		 * Formats the enum literal.
		 * 
		 * @param value
		 * @return
		 */
		public String formatEnum(Object value) {
			Enum<?> enumx = (Enum<?>) value;
			String i18nKey = EnumOptionBeanHelper.getI18nKey(enumx.getClass()
					.getName(), enumx);
			II18nService i18nService = getI18nService();

			String result = null;
			if (i18nService != null) {
				result = i18nService.getValue(i18nKey, getLocale());
			}

			if (result == null || result.trim().equals("")) {
				result = enumx.name();
			}

			return result;
		}

		public String formatBoolean(Boolean value) {
			String result = getI18nService().getValue(
					Boolean.class.getName() + "." + value.toString(),
					getLocale());

			if (result == null || result.trim().equals("")) {
				result = value.toString();
			}

			return result;
		}
	}
}
