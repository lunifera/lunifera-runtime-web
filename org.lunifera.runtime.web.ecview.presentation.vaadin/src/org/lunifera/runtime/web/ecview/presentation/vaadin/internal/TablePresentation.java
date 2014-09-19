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
import org.lunifera.ecview.core.ui.core.editparts.extension.presentation.ITabPresentation;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;

/**
 * This presenter is responsible to render a table on the given layout.
 */
public class TablePresentation extends AbstractFieldWidgetPresenter<Component>
		implements ITabPresentation<Component> {

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
			table.setMultiSelect(modelAccess.yTable.getSelectionType() == YSelectionType.MULTI);
			table.setSelectable(true);
			table.setImmediate(true);
			setupComponent(table, getCastedModel());

			associateWidget(table, modelAccess.yTable);
			if (modelAccess.isCssIdValid()) {
				table.setId(modelAccess.getCssID());
			} else {
				table.setId(getEditpart().getId());
			}

			if (table.isMultiSelect()) {
				property = new ObjectProperty(new HashSet(), Set.class);
			} else {
				if (modelAccess.yTable.getType() != null) {
					property = new ObjectProperty(null,
							modelAccess.yTable.getType());
				} else {
					property = new ObjectProperty(null, Object.class);
				}
			}
			table.setPropertyDataSource(property);

			applyColumns = false;
			if (modelAccess.yTable.getType() == String.class) {
				IndexedContainer datasource = new IndexedContainer();
				table.setContainerDataSource(datasource);
				table.setItemCaptionMode(ItemCaptionMode.ID);
			} else {
				if (modelAccess.yTable.getType() != null) {
					BeanItemContainer datasource = null;
					datasource = new BeanItemContainer(
							modelAccess.yTable.getType());
					table.setContainerDataSource(datasource);

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

			String itemImageProperty = modelAccess.yTable
					.getItemImageProperty();
			if (itemImageProperty != null && !itemImageProperty.equals("")) {
				table.setItemIconPropertyId(itemImageProperty);
				table.setRowHeaderMode(RowHeaderMode.EXPLICIT);
			}

			// creates the binding for the field
			createBindings(modelAccess.yTable, table);

			if (modelAccess.isCssClassValid()) {
				table.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(table);
		}
		return table;
	}

	/**
	 * Applies the column setting to the table.
	 */
	protected void applyColumns() {
		// set the visible columns and icons
		List<String> columns = new ArrayList<String>();
		Collection<?> propertyIds = table.getContainerDataSource()
				.getContainerPropertyIds();

		for (YColumn yColumn : modelAccess.yTable.getColumns()) {
			if (yColumn.isVisible() && propertyIds.contains(yColumn.getName())) {
				columns.add(yColumn.getName());
			}
		}

		table.setVisibleColumns(columns.toArray(new Object[columns.size()]));
		table.setColumnCollapsingAllowed(true);

		// traverse the columns again and set other properties
		for (YColumn yColumn : modelAccess.yTable.getColumns()) {
			if (yColumn.isVisible() && propertyIds.contains(yColumn.getName())) {
				table.setColumnHeader(yColumn.getName(),
						getColumnHeader(yColumn));
				table.setColumnAlignment(yColumn.getName(),
						toAlign(yColumn.getAlignment()));
				table.setColumnCollapsed(yColumn.getName(),
						yColumn.isCollapsed());
				table.setColumnCollapsible(yColumn.getName(),
						yColumn.isCollapsible());
				if (yColumn.getExpandRatio() >= 0) {
					table.setColumnExpandRatio(yColumn.getName(),
							yColumn.getExpandRatio());
				}
				if (yColumn.getIcon() != null && !yColumn.getIcon().equals("")) {
					table.setColumnIcon(yColumn.getName(), new ThemeResource(
							yColumn.getIcon()));
				}
			}
		}
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
			return yColumn.getName();
		}

		II18nService service = getI18nService();
		if (service != null && yDt.getLabelI18nKey() != null) {
			return service.getValue(yDt.getLabelI18nKey(), getLocale());
		} else {
			return yDt.getLabel();
		}
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

		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			table.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				table.setCaption(modelAccess.getLabel());
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
	@SuppressWarnings("restriction")
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YTABLE__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yTable.getType(),
				modelAccess.yTable.getEmfNsURI());
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
		if (modelAccess.yTable.getSelectionType() == YSelectionType.MULTI) {
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
		private final YTable yTable;

		public ModelAccess(YTable yTable) {
			super();
			this.yTable = yTable;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yTable.getCssClass();
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
			return yTable.getCssID();
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
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelValid() {
			return yTable.getDatadescription() != null
					&& yTable.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yTable.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yTable.getDatadescription() != null
					&& yTable.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yTable.getDatadescription().getLabelI18nKey();
		}
	}

	/**
	 * Converts the string value of the item icon property to
	 * {@link ThemeResource}.
	 */
	@SuppressWarnings("serial")
	private static class CustomTable extends Table {
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
	}
}
