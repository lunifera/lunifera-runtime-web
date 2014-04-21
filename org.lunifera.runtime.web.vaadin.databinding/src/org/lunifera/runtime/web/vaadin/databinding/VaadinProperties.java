/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.WidgetProperties (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding;

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
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyReadonlyProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerContainerDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerItemDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerPropertyDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.IVaadinListProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.IVaadinValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.VaadinListPropertyDecorator;

import com.vaadin.ui.Component;

/**
 * A factory for creating properties of SWT {@link Component components}.
 * 
 * @since 1.3
 */
public class VaadinProperties {

	public static IVaadinValueProperty accessor(Class<?> componentClass,
			String property) {
		return new SimpleAccessorProperty(componentClass, property);
	}

	// /**
	// * Returns a value property for observing the value of a {@link Field}.
	// *
	// * @param type
	// * The type of the value. Can be <code>null</code>.
	// *
	// * @return a value property for observing the text of a {@link Field}.
	// */
	// public static IVaadinValueProperty fieldValue(Class<?> type) {
	// return new FieldValueProperty(type);
	// }

	public static IVaadinValueProperty focus() {
		return new ComponentFocusedProperty();
	}

	public static IVaadinValueProperty description() {
		return new ComponentDescriptionProperty();
	}

	public static IVaadinValueProperty maxSplitPosition() {
		return new SplitPanelMaxSplitPositionProperty();
	}

	public static IVaadinValueProperty maxSplitPositionUnit() {
		return new SplitPanelMaxSplitPositionUnitProperty();
	}

	public static IVaadinValueProperty minSplitPosition() {
		return new SplitPanelMinSplitPositionProperty();
	}

	public static IVaadinValueProperty minSplitPositionUnit() {
		return new SplitPanelMinSplitPositionUnitProperty();
	}

	public static IVaadinValueProperty splitPosition() {
		return new SplitPanelSplitPositionProperty();
	}

	public static IVaadinValueProperty splitPositionUnit() {
		return new SplitPanelSplitPositionUnitProperty();
	}

	public static IVaadinValueProperty selectedTab() {
		return new TabSheetSelectedTabProperty();
	}

	public static IVaadinValueProperty color_ColorPickerGradient() {
		return new ColorPickerGradientColorProperty();
	}

	public static IVaadinValueProperty color_ColorPickerGrid() {
		return new ColorPickerGridColorProperty();
	}

	public static IVaadinValueProperty color_ColorPickerHistory() {
		return new ColorPickerHistoryColorProperty();
	}

	public static IVaadinValueProperty color_ColorPickerSelect() {
		return new ColorPickerSelectColorProperty();
	}

	public static IVaadinValueProperty height() {
		return new SizeableHeightProperty();
	}

	public static IVaadinValueProperty width() {
		return new SizeableWidthProperty();
	}

	public static IVaadinValueProperty itemPropertysetValue() {
		return new ItemPropertySetValueProperty();
	}

	public static IVaadinValueProperty itemPropertysetInfoValue() {
		return new ItemPropertySetInfoValueProperty();
	}

	/**
	 * Creates a list property that observes changes of item sets in container.
	 * 
	 * @param collectionType
	 *            the types contained in the collection
	 * @return listProperty
	 */
	public static IVaadinListProperty containerItemsetValue(
			Class<?> collectionType) {
		ContainerItemSetContentProperty property = new ContainerItemSetContentProperty(collectionType);
		return new VaadinListPropertyDecorator(property);
	}

	public static IVaadinValueProperty containerDatasource() {
		return new ViewerContainerDatasourceProperty();
	}

	public static IVaadinValueProperty itemDatasource() {
		return new ViewerItemDatasourceProperty();
	}

	public static IVaadinValueProperty propertyDatasource() {
		return new ViewerPropertyDatasourceProperty();
	}

	public static IVaadinValueProperty propertyValue() {
		return new PropertyValueProperty();
	}

	public static IVaadinValueProperty readonly() {
		return new PropertyReadonlyProperty();
	}

}
