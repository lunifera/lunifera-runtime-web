/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas from Eclipse Databinding.
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.databinding;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableList;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableSet;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.values.SetToListAdapter;

import com.vaadin.data.Buffered;
import com.vaadin.data.BufferedValidatable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validatable;
import com.vaadin.server.Scrollable;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractEmbedded;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractMedia;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.SpacingHandler;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Video;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.colorpicker.ColorPickerGradient;
import com.vaadin.ui.components.colorpicker.ColorPickerGrid;
import com.vaadin.ui.components.colorpicker.ColorPickerHistory;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;
import com.vaadin.ui.components.colorpicker.ColorSelector;

/**
 * A factory for creating observables for Vaadin Components
 */
public class VaadinObservables {

	private static java.util.List<UIRealm> realms = new ArrayList<UIRealm>();

	/**
	 * Returns the realm representing the UI thread for the given display.
	 * 
	 * @param ui
	 * @return the realm representing the UI thread for the given display
	 */
	public static Realm getRealm(final UI ui) {
		synchronized (realms) {
			for (Iterator<UIRealm> it = realms.iterator(); it.hasNext();) {
				UIRealm displayRealm = it.next();
				if (displayRealm.ui == ui) {
					displayRealm.makeDefault();
					return displayRealm;
				}
			}
			UIRealm result = new UIRealm(ui);
			realms.add(result);
			return result;
		}
	}

	/**
	 * Activates the realm for the current thread.
	 * 
	 * @param ui
	 */
	public static void activateRealm(final UI ui) {
		UIRealm uiRealm = (UIRealm) getRealm(ui);
		uiRealm.makeDefault();
	}

	/**
	 * Returns the UI of the widget or the current UI.
	 * 
	 * @param widget
	 * @return
	 */
	public static UI getUI(Component widget) {
		UI ui = widget != null ? widget.getUI() : null;
		return ui != null ? ui : UI.getCurrent();
	}

	/**
	 * Returns the property type of the given field.
	 * 
	 * @param field
	 * @return
	 */
	public static Class<?> getPropertyType(Field<?> field) {
		return getProperty(field).getType();
	}

	/**
	 * Returns the property of the given field.
	 * 
	 * @param field
	 * @return
	 */
	public static Property<?> getProperty(Field<?> field) {
		Property<?> property = field.getPropertyDataSource() != null ? field
				.getPropertyDataSource() : field;
		return property;
	}

	/**
	 * Returns an observable value tracking the propertyset of the given item
	 * notifier.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeItemPropertySetValue(
			Item.PropertySetChangeNotifier notifier) {
		return VaadinProperties.itemPropertysetValue().observe(notifier);
	}

	/**
	 * Returns an observable value tracking the propertyset of the given item
	 * notifier.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeItemPropertySetInfoValue(
			Item.PropertySetChangeNotifier notifier) {
		return VaadinProperties.itemPropertysetInfoValue().observe(notifier);
	}

	/**
	 * Returns an observable list tracking the item set of the given item
	 * notifier.
	 * 
	 * @param notifier
	 *            the field
	 * @param collectionType
	 *            the type contained in the collection
	 * @return
	 */
	public static IVaadinObservableList observeContainerItemSetContents(
			Container.ItemSetChangeNotifier notifier, Class<?> collectionType) {
		return VaadinProperties.containerItemsetAsList(collectionType).observe(
				notifier);
	}

	/**
	 * Returns an observable value tracking the container of the given viewer.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeContainerDatasource(
			Container.Viewer viewer) {
		return VaadinProperties.containerDatasource().observe(viewer);
	}

	/**
	 * Returns an observable value tracking the container of the given viewer.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeItemDatasource(
			Item.Viewer viewer) {
		return VaadinProperties.itemDatasource().observe(viewer);
	}

	/**
	 * Returns an observable value tracking the container of the given viewer.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeItemDatasource(
			Property.Viewer viewer) {
		return VaadinProperties.datasource().observe(viewer);
	}

	/**
	 * Returns an observable value tracking the selection of the given viewer.
	 * 
	 * @param notifier
	 * @param type
	 *            - the type of the selection object
	 * @return
	 */
	public static IVaadinObservableValue observeSingleSelection(
			Property.ValueChangeNotifier notifier, Class<?> type) {
		return VaadinProperties.singleSelection(type).observe(notifier);
	}

	/**
	 * Returns an observable value tracking nested attribute in the selected
	 * element.
	 * <p>
	 * For instance:<br>
	 * Given a list with Bar-beans. Bar has a relation to Foo-bean by
	 * myfoo-reference.<br>
	 * Then "myFoo.name" can be used as nestedPath to observe the foo#name
	 * attribute of the selected bean in the list.
	 * <p>
	 * This implementation supports pojos and beans. EObjects are not supported.
	 * 
	 * @param notifier
	 * @param type
	 *            - the type of the selection object
	 * @param nestedPath
	 *            - the path from the selected element to the observed value.
	 *            For instance "myFoo.name".
	 * @return
	 */
	public static IObservableValue observeSingleSelectionDetailValue(
			Property.ValueChangeNotifier notifier, Class<?> type,
			String nestedPath) {
		IVaadinObservableValue masterObservable = VaadinProperties
				.singleSelection(type).observe(notifier);

		if (hasPropertyChangeSupport(type)) {
			return BeansObservables.observeDetailValue(masterObservable, type,
					nestedPath, null);
		} else {
			return PojoObservables.observeDetailValue(masterObservable,
					nestedPath, null);
		}
	}

	/**
	 * Returns true, if the bean has property change support.
	 * 
	 * @param valueType
	 * @return
	 */
	private static boolean hasPropertyChangeSupport(Class<?> valueType) {
		@SuppressWarnings("unused")
		Method method = null;
		try {
			try {
				method = valueType.getMethod("addPropertyChangeListener",
						new Class[] { String.class,
								PropertyChangeListener.class });
				return true;
			} catch (NoSuchMethodException e) {
				method = valueType.getMethod("addPropertyChangeListener",
						new Class[] { PropertyChangeListener.class });
				return true;
			}
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		return false;
	}

	/**
	 * Returns an observable list tracking the multi selection of the given
	 * viewer.
	 * 
	 * @param notifier
	 * @param collectionType
	 *            the type contained in the multi selection
	 * @return
	 */
	public static IVaadinObservableList observeMultiSelectionAsList(
			Property.ValueChangeNotifier notifier, Class<?> collectionType) {
		return new SetToListAdapter(observeMultiSelectionAsSet(notifier,
				collectionType), notifier);
	}

	/**
	 * Returns an observable set tracking the multi selection of the given
	 * viewer.
	 * 
	 * @param notifier
	 * @param collectionType
	 *            the type contained in the multi selection
	 * @return
	 */
	public static IVaadinObservableSet observeMultiSelectionAsSet(
			Property.ValueChangeNotifier notifier, Class<?> collectionType) {
		return VaadinProperties.propertyMultiSelectionAsSet(collectionType)
				.observe(notifier);
	}

	/**
	 * Returns an observable value tracking the value of the given viewer.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeValue(
			Property.ValueChangeNotifier notifier) {
		return VaadinProperties.value().observeVaadinProperty(notifier);
	}

	/**
	 * Returns an observable value tracking the converted value of the given
	 * field.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeConvertedValue(
			AbstractField<?> field) {
		return VaadinProperties.accessor(AbstractField.class, "convertedValue")
				.observeVaadinProperty(field);
	}

	/**
	 * Returns an observable value tracking the readonly state of the given
	 * notifier.
	 * 
	 * @param notifier
	 * @return
	 */
	public static IVaadinObservableValue observeReadonly(
			Property.ReadOnlyStatusChangeNotifier notifier) {
		return VaadinProperties.readonly().observe(notifier);
	}

	/**
	 * Returns an observable value tracking the focus state of the given
	 * focusable. Note that isFocus() can not be returned. You can only use
	 * setFocus().
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeFocus(Focusable focusable) {
		return VaadinProperties.focus().observe(focusable);
	}

	/**
	 * Returns an observable value tracking the caption of the given component.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeCaption(Component component) {
		return VaadinProperties.accessor(Component.class, "caption").observe(
				component);
	}

	/**
	 * Returns an observable value tracking the enabled state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeEnabled(Component component) {
		return VaadinProperties.accessor(Component.class, "enabled").observe(
				component);
	}

	/**
	 * Returns an observable value tracking the enabled state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeDescription(Component component) {
		return VaadinProperties.description().observe(component);
	}

	/**
	 * Returns an observable value tracking the icon of the given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeIcon(Component component) {
		return VaadinProperties.accessor(Component.class, "icon").observe(
				component);
	}

	/**
	 * Returns an observable value tracking the primary style name of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observePrimaryStyleName(
			Component component) {
		return VaadinProperties.accessor(Component.class, "primaryStyleName")
				.observe(component);
	}

	/**
	 * Returns an observable value tracking the styleName of the given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeStyleName(Component component) {
		return VaadinProperties.accessor(Component.class, "styleName").observe(
				component);
	}

	/**
	 * Returns an observable value tracking the visible state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeVisible(Component component) {
		return VaadinProperties.accessor(Component.class, "visible").observe(
				component);
	}

	/**
	 * Returns an observable value tracking the required state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeRequired(Field<?> field) {
		return VaadinProperties.accessor(Field.class, "required")
				.observe(field);
	}

	/**
	 * Returns an observable value tracking the "required error message" of the
	 * given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeRequiredError(Field<?> field) {
		return VaadinProperties.accessor(Field.class, "requiredError").observe(
				field);
	}

	public static IVaadinObservableValue observeAlternateText(
			AbstractEmbedded component) {
		return VaadinProperties.accessor(AbstractEmbedded.class,
				"alternateText").observe(component);
	}

	public static IVaadinObservableValue observeSource(
			AbstractEmbedded component) {
		return VaadinProperties.accessor(AbstractEmbedded.class, "source")
				.observe(component);
	}

	public static IVaadinObservableValue observeItemCaptionMode(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class,
				"itemCaptionMode").observe(component);
	}

	public static IVaadinObservableValue observeItemCaptionPropertyId(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class,
				"itemCaptionPropertyId").observe(component);
	}

	public static IVaadinObservableValue observeItemIconPropertyId(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class,
				"itemIconPropertyId").observe(component);
	}

	public static IVaadinObservableValue observeMultiSelectMode(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class, "multiSelect")
				.observe(component);
	}

	public static IVaadinObservableValue observeNewItemHandler(
			AbstractSelect component) {
		return VaadinProperties
				.accessor(AbstractSelect.class, "newItemHandler").observe(
						component);
	}

	public static IVaadinObservableValue observeNewItemsAllowed(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class,
				"newItemsAllowed").observe(component);
	}

	public static IVaadinObservableValue observeNullSelectionAllowed(
			AbstractSelect component) {
		return VaadinProperties.accessor(AbstractSelect.class,
				"nullSelectionAllowed").observe(component);
	}

	public static IVaadinObservableValue observeBuffered(Buffered component) {
		return VaadinProperties.accessor(Buffered.class, "buffered").observe(
				component);
	}

	public static IVaadinObservableValue observeInvalidCommitted(
			BufferedValidatable component) {
		return VaadinProperties.accessor(Buffered.class, "invalidCommitted")
				.observe(component);
	}

	public static IVaadinObservableValue observeInvalidCommitted(
			ComboBox component) {
		return VaadinProperties.accessor(ComboBox.class, "pageLength").observe(
				component);
	}

	public static IVaadinObservableValue observeScrollToSelectedItem(
			ComboBox component) {
		return VaadinProperties
				.accessor(ComboBox.class, "scrollToSelectedItem").observe(
						component);
	}

	public static IVaadinObservableValue observeTextInputAllowed(
			ComboBox component) {
		return VaadinProperties.accessor(ComboBox.class, "textInputAllowed")
				.observe(component);
	}

	public static IVaadinObservableValue observeTemplateContents(
			CustomLayout component) {
		return VaadinProperties
				.accessor(CustomLayout.class, "templateContents").observe(
						component);
	}

	public static IVaadinObservableValue observeTemplateNameProperty(
			CustomLayout component) {
		return VaadinProperties.accessor(CustomLayout.class,
				"templateNameProperty").observe(component);
	}

	public static IVaadinObservableValue observeDateFormat(DateField component) {
		return VaadinProperties.accessor(DateField.class, "dateFormat")
				.observe(component);
	}

	public static IVaadinObservableValue observeLenient(DateField component) {
		return VaadinProperties.accessor(DateField.class, "lenient").observe(
				component);
	}

	public static IVaadinObservableValue observeParseErrorMessage(
			DateField component) {
		return VaadinProperties.accessor(DateField.class, "parseErrorMessage")
				.observe(component);
	}

	public static IVaadinObservableValue observeResolution(DateField component) {
		return VaadinProperties.accessor(DateField.class, "resolution")
				.observe(component);
	}

	public static IVaadinObservableValue observeShowISOWeekNumbers(
			DateField component) {
		return VaadinProperties.accessor(DateField.class, "showISOWeekNumbers")
				.observe(component);
	}

	public static IVaadinObservableValue observeTextFieldEnabled(
			DateField component) {
		return VaadinProperties.accessor(DateField.class, "textFieldEnabled")
				.observe(component);
	}

	public static IVaadinObservableValue observeTimeZone(DateField component) {
		return VaadinProperties.accessor(DateField.class, "timeZone").observe(
				component);
	}

	public static IVaadinObservableValue observeValidationVisible(
			AbstractField<?> component) {
		return VaadinProperties.accessor(AbstractField.class,
				"validationVisible").observe(component);
	}

	public static IVaadinObservableValue observeColumns(GridLayout component) {
		return VaadinProperties.accessor(GridLayout.class, "columns").observe(
				component);
	}

	public static IVaadinObservableValue observeCursorX(GridLayout component) {
		return VaadinProperties.accessor(GridLayout.class, "cursorX").observe(
				component);
	}

	public static IVaadinObservableValue observeCursorY(GridLayout component) {
		return VaadinProperties.accessor(GridLayout.class, "cursorY").observe(
				component);
	}

	public static IVaadinObservableValue observeRows(GridLayout component) {
		return VaadinProperties.accessor(GridLayout.class, "rows").observe(
				component);
	}

	public static IVaadinObservableValue observeRows(ListSelect component) {
		return VaadinProperties.accessor(ListSelect.class, "rows").observe(
				component);
	}

	public static IVaadinObservableValue observeRows(MarginHandler component) {
		return VaadinProperties.accessor(MarginHandler.class, "marginInfo")
				.observe(component);
	}

	public static IVaadinObservableValue observeRows(OptionGroup component) {
		return VaadinProperties.accessor(OptionGroup.class,
				"htmlContentAllowed").observe(component);
	}

	public static IVaadinObservableValue observeRows(PopupDateField component) {
		return VaadinProperties.accessor(PopupDateField.class, "inputPrompt")
				.observe(component);
	}

	public static IVaadinObservableValue observeTextFieldEnabled(
			PopupDateField component) {
		return VaadinProperties.accessor(PopupDateField.class,
				"textFieldEnabled").observe(component);
	}

	public static IVaadinObservableValue observeHeight(Sizeable component) {
		return VaadinProperties.height().observe(component);
	}

	public static IVaadinObservableValue observeWidth(Sizeable component) {
		return VaadinProperties.width().observe(component);
	}

	public static IVaadinObservableValue observeSpacing(SpacingHandler component) {
		return VaadinProperties.accessor(SpacingHandler.class, "spacing")
				.observe(component);
	}

	public static IVaadinObservableValue observeFirstComponent(
			AbstractSplitPanel component) {
		return VaadinProperties.accessor(AbstractSplitPanel.class,
				"firstComponent").observe(component);
	}

	public static IVaadinObservableValue observeLocked(
			AbstractSplitPanel component) {
		return VaadinProperties.accessor(AbstractSplitPanel.class, "locked")
				.observe(component);
	}

	public static IVaadinObservableValue observeMaxSplitPosition(
			AbstractSplitPanel component) {
		return VaadinProperties.maxSplitPosition().observe(component);
	}

	public static IVaadinObservableValue observeMaxSplitPositionUnit(
			AbstractSplitPanel component) {
		return VaadinProperties.maxSplitPositionUnit().observe(component);
	}

	public static IVaadinObservableValue observeMinSplitPosition(
			AbstractSplitPanel component) {
		return VaadinProperties.minSplitPosition().observe(component);
	}

	public static IVaadinObservableValue observeMinSplitPositionUnit(
			AbstractSplitPanel component) {
		return VaadinProperties.minSplitPositionUnit().observe(component);
	}

	public static IVaadinObservableValue observeSplitPosition(
			AbstractSplitPanel component) {
		return VaadinProperties.splitPosition().observe(component);
	}

	public static IVaadinObservableValue observeSplitPositionUnit(
			AbstractSplitPanel component) {
		return VaadinProperties.splitPositionUnit().observe(component);
	}

	public static IVaadinObservableValue observeSeconComponent(
			AbstractSplitPanel component) {
		return VaadinProperties.accessor(AbstractSplitPanel.class,
				"secondComponent").observe(component);
	}

	public static IVaadinObservableValue observeCache(Table component) {
		return VaadinProperties.accessor(Table.class, "cache").observe(
				component);
	}

	public static IVaadinObservableValue observeCellStyleGenerator(
			Table component) {
		return VaadinProperties.accessor(Table.class, "cellStyleGenerator")
				.observe(component);
	}

	public static IVaadinObservableValue observeColumnAlignments(Table component) {
		return VaadinProperties.accessor(Table.class, "columnAlignments")
				.observe(component);
	}

	public static IVaadinObservableValue observeColumnCollapsingAllowed(
			Table component) {
		return VaadinProperties
				.accessor(Table.class, "columnCollapsingAllowed").observe(
						component);
	}

	public static IVaadinObservableValue observeColumnHeaderMode(Table component) {
		return VaadinProperties.accessor(Table.class, "columnHeaderMode")
				.observe(component);
	}

	public static IVaadinObservableValue observeColumnHeaders(Table component) {
		return VaadinProperties.accessor(Table.class, "columnHeaders").observe(
				component);
	}

	public static IVaadinObservableValue observeColumnIcons(Table component) {
		return VaadinProperties.accessor(Table.class, "columnIcons").observe(
				component);
	}

	public static IVaadinObservableValue observeColumnReorderingAllowed(
			Table component) {
		return VaadinProperties
				.accessor(Table.class, "columnReorderingAllowed").observe(
						component);
	}

	public static IVaadinObservableValue observeCurrentPageFirstItemId(
			Table component) {
		return VaadinProperties.accessor(Table.class, "currentPageFirstItemId")
				.observe(component);
	}

	public static IVaadinObservableValue observeCurrentPageFirstItemIndex(
			Table component) {
		return VaadinProperties.accessor(Table.class,
				"currentPageFirstItemIndex").observe(component);
	}

	public static IVaadinObservableValue observeDropHandler(Table component) {
		return VaadinProperties.accessor(Table.class, "dropHandler").observe(
				component);
	}

	public static IVaadinObservableValue observeDragMode(Table component) {
		return VaadinProperties.accessor(Table.class, "dragMode").observe(
				component);
	}

	public static IVaadinObservableValue observeEditable(Table component) {
		return VaadinProperties.accessor(Table.class, "editable").observe(
				component);
	}

	public static IVaadinObservableValue observeTableFieldFactory(
			Table component) {
		return VaadinProperties.accessor(Table.class, "tableFieldFactory")
				.observe(component);
	}

	public static IVaadinObservableValue observeFooterVisible(Table component) {
		return VaadinProperties.accessor(Table.class, "footerVisible").observe(
				component);
	}

	public static IVaadinObservableValue observeItemDescriptionGenerator(
			Table component) {
		return VaadinProperties.accessor(Table.class,
				"itemDescriptionGenerator").observe(component);
	}

	public static IVaadinObservableValue observeMultiSelectMode(Table component) {
		return VaadinProperties.accessor(Table.class, "multiSelectMode")
				.observe(component);
	}

	public static IVaadinObservableValue observeRowGenerator(Table component) {
		return VaadinProperties.accessor(Table.class, "rowGenerator").observe(
				component);
	}

	public static IVaadinObservableValue observeRowHeaderMode(Table component) {
		return VaadinProperties.accessor(Table.class, "rowHeaderMode").observe(
				component);
	}

	public static IVaadinObservableValue observeSelectable(Table component) {
		return VaadinProperties.accessor(Table.class, "selectable").observe(
				component);
	}

	public static IVaadinObservableValue observeSortAscending(Table component) {
		return VaadinProperties.accessor(Table.class, "sortAscending").observe(
				component);
	}

	public static IVaadinObservableValue observeSortContainerPropertyId(
			Table component) {
		return VaadinProperties
				.accessor(Table.class, "sortContainerPropertyId").observe(
						component);
	}

	public static IVaadinObservableValue observeSortEnabled(Table component) {
		return VaadinProperties.accessor(Table.class, "sortEnabled").observe(
				component);
	}

	public static IVaadinObservableValue observeVisible(Table component) {
		return VaadinProperties.accessor(Table.class, "visible").observe(
				component);
	}

	public static IVaadinObservableValue observeSelectedTab(TabSheet component) {
		return VaadinProperties.selectedTab().observe(component);
	}

	public static IVaadinObservableValue observeTabIndex(TabSheet component) {
		return VaadinProperties.accessor(TabSheet.class, "tabIndex").observe(
				component);
	}

	public static IVaadinObservableValue observeRows(TextArea component) {
		return VaadinProperties.accessor(TextArea.class, "rows").observe(
				component);
	}

	public static IVaadinObservableValue observeWordWrap(TextArea component) {
		return VaadinProperties.accessor(TextArea.class, "wordWrap").observe(
				component);
	}

	public static IVaadinObservableValue observeTextChangeEventMode(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class,
				"textChangeEventMode").observe(component);
	}

	public static IVaadinObservableValue observeTextChangeTimeout(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class,
				"textChangeTimeout").observe(component);
	}

	public static IVaadinObservableValue observeColumns(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class, "columns")
				.observe(component);
	}

	public static IVaadinObservableValue observeCursorPosition(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class,
				"cursorPosition").observe(component);
	}

	public static IVaadinObservableValue observeInputPrompt(
			AbstractTextField component) {
		return VaadinProperties
				.accessor(AbstractTextField.class, "inputPrompt").observe(
						component);
	}

	public static IVaadinObservableValue observeMaxLength(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class, "maxLength")
				.observe(component);
	}

	public static IVaadinObservableValue observeNullRepresentation(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class,
				"nullRepresentation").observe(component);
	}

	public static IVaadinObservableValue observeNullSettingAllowed(
			AbstractTextField component) {
		return VaadinProperties.accessor(AbstractTextField.class,
				"nullSettingAllowed").observe(component);
	}

	public static IVaadinObservableValue observeDragMode(Tree component) {
		return VaadinProperties.accessor(Tree.class, "dragMode").observe(
				component);
	}

	public static IVaadinObservableValue observeDropHandler(Tree component) {
		return VaadinProperties.accessor(Tree.class, "dropHandler").observe(
				component);
	}

	public static IVaadinObservableValue observeItemDescriptionGenerator(
			Tree component) {
		return VaadinProperties
				.accessor(Tree.class, "itemDescriptionGenerator").observe(
						component);
	}

	public static IVaadinObservableValue observeItemStyleGenerator(
			Tree component) {
		return VaadinProperties.accessor(Tree.class, "itemStyleGenerator")
				.observe(component);
	}

	public static IVaadinObservableValue observeMultiSelectMode(Tree component) {
		return VaadinProperties.accessor(Tree.class, "multiSelectMode")
				.observe(component);
	}

	public static IVaadinObservableValue observeSelectable(Tree component) {
		return VaadinProperties.accessor(Tree.class, "selectable").observe(
				component);
	}

	public static IVaadinObservableValue observeSelectable(TreeTable component) {
		return VaadinProperties.accessor(TreeTable.class, "animationsEnabled")
				.observe(component);
	}

	public static IVaadinObservableValue observeHierachyColumnId(
			TreeTable component) {
		return VaadinProperties.accessor(TreeTable.class, "hierachyColumnId")
				.observe(component);
	}

	public static IVaadinObservableValue observeRows(TwinColSelect component) {
		return VaadinProperties.accessor(TwinColSelect.class, "rows").observe(
				component);
	}

	public static IVaadinObservableValue observeLeftColumnCaption(
			TwinColSelect component) {
		return VaadinProperties.accessor(TwinColSelect.class,
				"leftColumnCaption").observe(component);
	}

	public static IVaadinObservableValue observeRightColumnCaption(
			TwinColSelect component) {
		return VaadinProperties.accessor(TwinColSelect.class,
				"rightColumnCaption").observe(component);
	}

	public static IVaadinObservableValue observeInvalidAllowed(
			Validatable component) {
		return VaadinProperties.accessor(Validatable.class, "invalidAllowed")
				.observe(component);
	}

	public static IVaadinObservableValue observeIndeterminate(
			ProgressIndicator component) {
		return VaadinProperties.accessor(ProgressIndicator.class,
				"indeterminate").observe(component);
	}

	public static IVaadinObservableValue observePollingInterval(
			ProgressIndicator component) {
		return VaadinProperties.accessor(ProgressIndicator.class,
				"pollingInterval").observe(component);
	}

	public static IVaadinObservableValue observeNullRepresentation(
			RichTextArea component) {
		return VaadinProperties.accessor(RichTextArea.class,
				"nullRepresentation").observe(component);
	}

	public static IVaadinObservableValue observeNullSettingAllowed(
			RichTextArea component) {
		return VaadinProperties.accessor(RichTextArea.class,
				"nullSettingAllowed").observe(component);
	}

	public static IVaadinObservableValue observeMax(Slider component) {
		return VaadinProperties.accessor(Slider.class, "max")
				.observe(component);
	}

	public static IVaadinObservableValue observeMin(Slider component) {
		return VaadinProperties.accessor(Slider.class, "min")
				.observe(component);
	}

	public static IVaadinObservableValue observeOrientation(Slider component) {
		return VaadinProperties.accessor(Slider.class, "orientation").observe(
				component);
	}

	public static IVaadinObservableValue observeResolution(Slider component) {
		return VaadinProperties.accessor(Slider.class, "resolution").observe(
				component);
	}

	public static IVaadinObservableValue observeAltText(AbstractMedia component) {
		return VaadinProperties.accessor(AbstractMedia.class, "altText")
				.observe(component);
	}

	public static IVaadinObservableValue observeAutoplay(AbstractMedia component) {
		return VaadinProperties.accessor(AbstractMedia.class, "autoplay")
				.observe(component);
	}

	public static IVaadinObservableValue observeHtmlContentAllowed(
			AbstractMedia component) {
		return VaadinProperties.accessor(AbstractMedia.class,
				"htmlContentAllowed").observe(component);
	}

	public static IVaadinObservableValue observeMuted(AbstractMedia component) {
		return VaadinProperties.accessor(AbstractMedia.class, "muted").observe(
				component);
	}

	public static IVaadinObservableValue observeShowControls(
			AbstractMedia component) {
		return VaadinProperties.accessor(AbstractMedia.class, "showControls")
				.observe(component);
	}

	public static IVaadinObservableValue observePoster(Video component) {
		return VaadinProperties.accessor(Video.class, "poster").observe(
				component);
	}

	public static IVaadinObservableValue observeScrollLeft(Scrollable component) {
		return VaadinProperties.accessor(Scrollable.class, "scrollLeft")
				.observe(component);
	}

	public static IVaadinObservableValue observeScrollTop(Scrollable component) {
		return VaadinProperties.accessor(Scrollable.class, "scrollTop")
				.observe(component);
	}

	public static IVaadinObservableValue observeClosable(Window component) {
		return VaadinProperties.accessor(Window.class, "closable").observe(
				component);
	}

	public static IVaadinObservableValue observeScrollTop(Window component) {
		return VaadinProperties.accessor(Window.class, "scrollTop").observe(
				component);
	}

	public static IVaadinObservableValue observeDraggable(Window component) {
		return VaadinProperties.accessor(Window.class, "draggable").observe(
				component);
	}

	public static IVaadinObservableValue observeModal(Window component) {
		return VaadinProperties.accessor(Window.class, "modal").observe(
				component);
	}

	public static IVaadinObservableValue observeResizeable(Window component) {
		return VaadinProperties.accessor(Window.class, "resizable").observe(
				component);
	}

	public static IVaadinObservableValue observeResizeLazy(Window component) {
		return VaadinProperties.accessor(Window.class, "resizeLazy").observe(
				component);
	}

	public static IVaadinObservableValue observePositionX(Window component) {
		return VaadinProperties.accessor(Window.class, "positionX").observe(
				component);
	}

	public static IVaadinObservableValue observePositionY(Window component) {
		return VaadinProperties.accessor(Window.class, "positionY").observe(
				component);
	}

	public static IVaadinObservableValue observeColor(ColorSelector component) {
		return VaadinProperties.accessor(ColorSelector.class, "color").observe(
				component);
	}

	public static IVaadinObservableValue observeContent(
			SingleComponentContainer component) {
		return VaadinProperties.accessor(SingleComponentContainer.class,
				"content").observe(component);
	}

	public static IVaadinObservableValue observeLastHeartbeatTimestamp(
			UI component) {
		return VaadinProperties.accessor(UI.class, "lastHeartbeatTimestamp")
				.observe(component);
	}

	public static IVaadinObservableValue observeNavigator(UI component) {
		return VaadinProperties.accessor(UI.class, "navigator").observe(
				component);
	}

	public static IVaadinObservableValue observeResizeLazy(UI component) {
		return VaadinProperties.accessor(UI.class, "resizeLazy").observe(
				component);
	}

	public static IVaadinObservableValue observeScrollLeft(UI component) {
		return VaadinProperties.accessor(UI.class, "scrollLeft").observe(
				component);
	}

	public static IVaadinObservableValue observeScrollTop(UI component) {
		return VaadinProperties.accessor(UI.class, "scrollTop").observe(
				component);
	}

	public static IVaadinObservableValue observeSession(UI component) {
		return VaadinProperties.accessor(UI.class, "session")
				.observe(component);
	}

	public static IVaadinObservableValue observeDisableOnClick(Button component) {
		return VaadinProperties.accessor(Button.class, "disableOnClick")
				.observe(component);
	}

	public static IVaadinObservableValue observeHtmlContentAllowed(
			Button component) {
		return VaadinProperties.accessor(Button.class, "htmlContentAllowed")
				.observe(component);
	}

	public static IVaadinObservableValue observeColor(
			ColorPickerGradient component) {
		return VaadinProperties.colorColorPickerGradient().observe(component);
	}

	public static IVaadinObservableValue observeColor(ColorPickerGrid component) {
		return VaadinProperties.colorColorPickerGrid().observe(component);
	}

	public static IVaadinObservableValue observeColor(
			ColorPickerHistory component) {
		return VaadinProperties.colorColorPickerHistory().observe(component);
	}

	public static IVaadinObservableValue observeColor(
			ColorPickerSelect component) {
		return VaadinProperties.colorColorPickerSelect().observe(component);
	}

	public static IVaadinObservableValue observeAlternateText(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "alternateText")
				.observe(component);
	}

	public static IVaadinObservableValue observeArchive(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "archive").observe(
				component);
	}

	public static IVaadinObservableValue observeClassId(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "classId").observe(
				component);
	}

	public static IVaadinObservableValue observeCodebase(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "codebase").observe(
				component);
	}

	public static IVaadinObservableValue observeCodetype(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "codetype").observe(
				component);
	}

	public static IVaadinObservableValue observeMimeType(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "mimeType").observe(
				component);
	}

	public static IVaadinObservableValue observeSource(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "source").observe(
				component);
	}

	public static IVaadinObservableValue observeStandby(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "standby").observe(
				component);
	}

	public static IVaadinObservableValue observeType(Embedded component) {
		return VaadinProperties.accessor(Embedded.class, "type").observe(
				component);
	}

	public static IVaadinObservableValue observeContentMode(Label component) {
		return VaadinProperties.accessor(Label.class, "contentMode").observe(
				component);
	}

	public static IVaadinObservableValue observeConverter(Label component) {
		return VaadinProperties.accessor(Label.class, "converter").observe(
				component);
	}

	public static IVaadinObservableValue observeResource(Link component) {
		return VaadinProperties.accessor(Link.class, "resource").observe(
				component);
	}

	public static IVaadinObservableValue observeTargetBorder(Link component) {
		return VaadinProperties.accessor(Link.class, "targetBorder").observe(
				component);
	}

	public static IVaadinObservableValue observeTargetHeight(Link component) {
		return VaadinProperties.accessor(Link.class, "targetHeight").observe(
				component);
	}

	public static IVaadinObservableValue observeTargetName(Link component) {
		return VaadinProperties.accessor(Link.class, "targetName").observe(
				component);
	}

	public static IVaadinObservableValue observeTargetWidth(Link component) {
		return VaadinProperties.accessor(Link.class, "targetWidth").observe(
				component);
	}

	public static IVaadinObservableValue observeAutoOpen(MenuBar component) {
		return VaadinProperties.accessor(MenuBar.class, "autoOpen").observe(
				component);
	}

	public static IVaadinObservableValue observeHtmlContentAllowed(
			MenuBar component) {
		return VaadinProperties.accessor(MenuBar.class, "htmlContentAllowed")
				.observe(component);
	}

	public static IVaadinObservableValue observeMoreMenuItem(MenuBar component) {
		return VaadinProperties.accessor(MenuBar.class, "moreMenuItem")
				.observe(component);
	}

	public static IVaadinObservableValue observeContent(PopupView component) {
		return VaadinProperties.accessor(PopupView.class, "content").observe(
				component);
	}

	public static IVaadinObservableValue observeHideOnMouseOut(
			PopupView component) {
		return VaadinProperties.accessor(PopupView.class, "hideOnMouseOut")
				.observe(component);
	}

	public static IVaadinObservableValue observePopupVisible(PopupView component) {
		return VaadinProperties.accessor(PopupView.class, "popupVisible")
				.observe(component);
	}

	public static IVaadinObservableValue observeButtonCaption(Upload component) {
		return VaadinProperties.accessor(PopupView.class, "buttonCaption")
				.observe(component);
	}

	public static IVaadinObservableValue observeReceiver(Upload component) {
		return VaadinProperties.accessor(PopupView.class, "receiver").observe(
				component);
	}

	private static class UIRealm extends Realm {
		private final UI ui;

		/**
		 * @param ui
		 */
		private UIRealm(UI ui) {
			this.ui = ui;
			setDefault(this);
		}

		@Override
		public boolean isCurrent() {
			return UI.getCurrent() == ui;
		}

		/**
		 * Makes the realm to the thread default.
		 */
		public void makeDefault() {
			setDefault(this);
		}

		@Override
		public void asyncExec(final Runnable runnable) {

			throw new UnsupportedOperationException("Not a valid call!");
		}

		@Override
		public void timerExec(int milliseconds, final Runnable runnable) {
			throw new UnsupportedOperationException("Not a valid call!");
		}

		@Override
		public int hashCode() {
			return (ui == null) ? 0 : ui.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final UIRealm other = (UIRealm) obj;
			if (ui == null) {
				if (other.ui != null) {
					return false;
				}
			} else if (!ui.equals(other.ui)) {
				return false;
			}
			return true;
		}
	}

}
