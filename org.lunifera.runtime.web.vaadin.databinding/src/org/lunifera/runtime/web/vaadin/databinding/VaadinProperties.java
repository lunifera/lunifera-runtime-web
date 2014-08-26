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

import org.lunifera.runtime.web.vaadin.databinding.component.internal.ButtonClickProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ColorPickerGradientColorProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ColorPickerGridColorProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ColorPickerHistoryColorProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ColorPickerSelectColorProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentDescriptionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentFocusedProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SimpleAccessorProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SizeableHeightProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SizeableWidthProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMaxSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMaxSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMinSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMinSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.TabSheetSelectedTabProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ContainerItemSetContentProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ItemPropertySetInfoValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ItemPropertySetValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.MultiSelectionListProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.MultiSelectionSetProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyReadonlyProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.SingleSelectionProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerContainerDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerItemDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerPropertyDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.IVaadinListProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.IVaadinSetProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.IVaadinValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.VaadinListPropertyDecorator;
import org.lunifera.runtime.web.vaadin.databinding.properties.VaadinSetPropertyDecorator;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.components.colorpicker.ColorPickerGradient;
import com.vaadin.ui.components.colorpicker.ColorPickerGrid;
import com.vaadin.ui.components.colorpicker.ColorPickerHistory;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;

/**
 * A factory for creating properties of SWT {@link Component components}.
 * 
 * @since 1.3
 */
public class VaadinProperties {

	/**
	 * Returns a property that handles the attribute defined by the given
	 * property.
	 * 
	 * @param componentClass
	 * @param property
	 * @return
	 */
	public static IVaadinValueProperty accessor(Class<?> componentClass,
			String property) {
		return new SimpleAccessorProperty(componentClass, property);
	}

	/**
	 * Returns a property that handles the focus for {@link Field fields}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty focus() {
		return new ComponentFocusedProperty();
	}

	/**
	 * Returns a property that observes a button click.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty buttonClick() {
		return new ButtonClickProperty();
	}

	/**
	 * Returns a property that handles the description attribute of components.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty description() {
		return new ComponentDescriptionProperty();
	}

	/**
	 * Returns a property that handles the max split position for the
	 * {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty maxSplitPosition() {
		return new SplitPanelMaxSplitPositionProperty();
	}

	/**
	 * 
	 * Returns a property that handles the unit of the maximum split position
	 * for the {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty maxSplitPositionUnit() {
		return new SplitPanelMaxSplitPositionUnitProperty();
	}

	/**
	 * Returns a property that handles the minimal split position for the
	 * {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty minSplitPosition() {
		return new SplitPanelMinSplitPositionProperty();
	}

	/**
	 * 
	 * Returns a property that handles the unit of the minimal split position
	 * for the {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty minSplitPositionUnit() {
		return new SplitPanelMinSplitPositionUnitProperty();
	}

	/**
	 * Returns a property that handles the split position for the
	 * {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty splitPosition() {
		return new SplitPanelSplitPositionProperty();
	}

	/**
	 * Returns a property that handles the unit of the split position for the
	 * {@link AbstractSplitPanel}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty splitPositionUnit() {
		return new SplitPanelSplitPositionUnitProperty();
	}

	/**
	 * Returns a property that handles the selected tab of {@link TabSheet}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty selectedTab() {
		return new TabSheetSelectedTabProperty();
	}

	/**
	 * Returns a property that handles the color attribute of the
	 * {@link ColorPickerGradient}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty colorColorPickerGradient() {
		return new ColorPickerGradientColorProperty();
	}

	/**
	 * Returns a property that handles the color attribute of the
	 * {@link ColorPickerGrid}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty colorColorPickerGrid() {
		return new ColorPickerGridColorProperty();
	}

	/**
	 * Returns a property that handles the color attribute of the
	 * {@link ColorPickerHistory}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty colorColorPickerHistory() {
		return new ColorPickerHistoryColorProperty();
	}

	/**
	 * Returns a property that handles the color attribute of a
	 * {@link ColorPickerSelect}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty colorColorPickerSelect() {
		return new ColorPickerSelectColorProperty();
	}

	/**
	 * Returns a property that handles the height attribute.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty height() {
		return new SizeableHeightProperty();
	}

	/**
	 * Returns a property that handles the width attribute.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty width() {
		return new SizeableWidthProperty();
	}

	/**
	 * TODO change to observableList
	 * 
	 * @return
	 */
	public static IVaadinValueProperty itemPropertysetValue() {
		return new ItemPropertySetValueProperty();
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public static IVaadinValueProperty itemPropertysetInfoValue() {
		return new ItemPropertySetInfoValueProperty();
	}

	/**
	 * Creates a list property that observes changes of multi selections. The
	 * sort order of the list random since vaadin selects handle the selections
	 * in a set. So the list is just a wrapper for the set.
	 * 
	 * @param collectionType
	 *            the types contained in the collection
	 * @return listProperty
	 */
	public static IVaadinListProperty propertyMultiSelectionAsList(
			Class<?> collectionType) {
		MultiSelectionListProperty property = new MultiSelectionListProperty(
				collectionType);
		return new VaadinListPropertyDecorator(property);
	}

	/**
	 * Creates a set property that observes changes of multi selections.
	 * 
	 * @param collectionType
	 *            the types contained in the collection
	 * @return setProperty
	 */
	public static IVaadinSetProperty propertyMultiSelectionAsSet(
			Class<?> collectionType) {
		MultiSelectionSetProperty property = new MultiSelectionSetProperty(
				collectionType);
		return new VaadinSetPropertyDecorator(property);
	}

	/**
	 * Creates a list property that observes changes of item sets in container.
	 * 
	 * @param collectionType
	 *            the types contained in the collection
	 * @return listProperty
	 */
	public static IVaadinListProperty containerItemsetAsList(
			Class<?> collectionType) {
		ContainerItemSetContentProperty property = new ContainerItemSetContentProperty(
				collectionType);
		return new VaadinListPropertyDecorator(property);
	}

	/**
	 * Returns a property that handles the containerDatasource attribute of
	 * {@link Container.Viewer}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty containerDatasource() {
		return new ViewerContainerDatasourceProperty();
	}

	/**
	 * Returns a property that handles the itemDatasource attribute of
	 * {@link Item.Viewer}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty itemDatasource() {
		return new ViewerItemDatasourceProperty();
	}

	/**
	 * Returns a property that handles the propertyDatasource attribute of
	 * {@link Property.Viewer}.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty datasource() {
		return new ViewerPropertyDatasourceProperty();
	}

	/**
	 * Returns a property that handles the value attribute.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty value() {
		return new PropertyValueProperty();
	}

	/**
	 * Returns a property that handles single selections.
	 * 
	 * @param type
	 * @return
	 */
	public static IVaadinValueProperty singleSelection(Class<?> type) {
		return new SingleSelectionProperty(type);
	}

	/**
	 * Returns a property that handles the readonly attribute.
	 * 
	 * @return
	 */
	public static IVaadinValueProperty readonly() {
		return new PropertyReadonlyProperty();
	}

}
