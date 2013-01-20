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
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMaxSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMaxSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMinSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelMinSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelSplitPositionProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.SplitPanelSplitPositionUnitProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.TabSheetSelectedTabProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ContainerItemSetProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ContainerPropertySetProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ItemPropertySetProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyReadonlyProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.PropertyValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerContainerDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerItemDatasourceProperty;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ViewerPropertyDatasourceProperty;

import com.vaadin.ui.Component;

/**
 * A factory for creating properties of SWT {@link Component components}.
 * 
 * @since 1.3
 */
public class VaadinProperties {

	public static IComponentValueProperty accessor(Class<?> componentClass,
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
	// public static IComponentValueProperty fieldValue(Class<?> type) {
	// return new FieldValueProperty(type);
	// }

	public static IComponentValueProperty focus() {
		return new ComponentFocusedProperty();
	}

	public static IComponentValueProperty description() {
		return new ComponentDescriptionProperty();
	}

	public static IComponentValueProperty maxSplitPosition() {
		return new SplitPanelMaxSplitPositionProperty();
	}

	public static IComponentValueProperty maxSplitPositionUnit() {
		return new SplitPanelMaxSplitPositionUnitProperty();
	}

	public static IComponentValueProperty minSplitPosition() {
		return new SplitPanelMinSplitPositionProperty();
	}

	public static IComponentValueProperty minSplitPositionUnit() {
		return new SplitPanelMinSplitPositionUnitProperty();
	}

	public static IComponentValueProperty splitPosition() {
		return new SplitPanelSplitPositionProperty();
	}

	public static IComponentValueProperty splitPositionUnit() {
		return new SplitPanelSplitPositionUnitProperty();
	}

	public static IComponentValueProperty selectedTab() {
		return new TabSheetSelectedTabProperty();
	}

	public static IComponentValueProperty color_ColorPickerGradient() {
		return new ColorPickerGradientColorProperty();
	}

	public static IComponentValueProperty color_ColorPickerGrid() {
		return new ColorPickerGridColorProperty();
	}

	public static IComponentValueProperty color_ColorPickerHistory() {
		return new ColorPickerHistoryColorProperty();
	}

	public static IComponentValueProperty color_ColorPickerSelect() {
		return new ColorPickerSelectColorProperty();
	}

	public static IModelValueProperty itemPropertyset() {
		return new ItemPropertySetProperty();
	}

	public static IModelValueProperty containerPropertyset() {
		return new ContainerPropertySetProperty();
	}

	public static IModelValueProperty containerItemset() {
		return new ContainerItemSetProperty();
	}

	public static IModelValueProperty containerDatasource() {
		return new ViewerContainerDatasourceProperty();
	}

	public static IModelValueProperty itemDatasource() {
		return new ViewerItemDatasourceProperty();
	}

	public static IModelValueProperty propertyDatasource() {
		return new ViewerPropertyDatasourceProperty();
	}

	public static IModelValueProperty propertyValue() {
		return new PropertyValueProperty();
	}

	public static IModelValueProperty readonly() {
		return new PropertyReadonlyProperty();
	}

}
