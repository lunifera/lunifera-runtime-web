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

import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentCaptionProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentEnabledProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentFocusedProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentIconProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentPrimaryStylenameProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentReadonlyProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentStylenameProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.ComponentVisibleProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.FieldRequiredErrorProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.FieldRequiredProperty;
import org.lunifera.runtime.web.vaadin.databinding.internal.FieldValueProperty;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * A factory for creating properties of SWT {@link Component components}.
 * 
 * @since 1.3
 */
public class VaadinProperties {

	// /**
	// * Returns a value property for observing the bounds of a {@link
	// Component}.
	// *
	// * @return a value property for observing the bounds of a {@link
	// Component}.
	// */
	// public static IComponentValueProperty bounds() {
	// return new ComponentBoundsProperty();
	// }
	//
	// /**
	// * Returns a value property for observing the readonly state of a
	// * component.
	// *
	// * @return a value property for observing the editable state of a
	// * {@link CCombo}, {@link StyledText}, or {@link Text}.
	// */
	// public static IComponentValueProperty readonly() {
	// return new ComponentReadonlyProperty();
	// }
	//
	// /**
	// * Returns a value property for observing the enablement state of a
	// * {@link Component}.
	// *
	// * @return a value property for observing the enablement state of a
	// * {@link Component}.
	// */
	// public static IComponentValueProperty enabled() {
	// return new ComponentEnabledProperty();
	// }
	//
	// /**
	// * Returns a value property for observing the focus state of a
	// * {@link Component}.
	// *
	// * @return a value property for observing the focus state of a
	// * {@link Component}.
	// */
	// public static IComponentValueProperty focused() {
	// return new ComponentFocusedProperty();
	// }

	// /**
	// * Returns a list property for observing the items of a {@link
	// Container$Viewer}.
	// *
	// * @return a list property for observing the items of a {@link
	// Container$Viewer}.
	// */
	// public static IComponentListProperty items() {
	// return new ContainerViewerItemsProperty();
	// }
	//
	// /**
	// * Returns a value property for observing the single selection index of a
	// * {@link Container$Viewer}.
	// *
	// * @return a value property for the single selection index of a {@link
	// Container$Viewer}.
	// */
	// public static IComponentValueProperty singleSelectionIndex() {
	// return new ContainerViewerSingleSelectionIndexProperty();
	// }

	/**
	 * Returns a value property for observing the value of a {@link Field}.
	 * 
	 * @return a value property for observing the text of a {@link Field}.
	 */
	public static IComponentValueProperty value() {
		return new FieldValueProperty();
	}

	public static IComponentValueProperty focus() {
		return new ComponentFocusedProperty();
	}

	public static IComponentValueProperty caption() {
		return new ComponentCaptionProperty();
	}

	public static IComponentValueProperty enabled() {
		return new ComponentEnabledProperty();
	}

	public static IComponentValueProperty icon() {
		return new ComponentIconProperty();
	}

	public static IComponentValueProperty primaryStylename() {
		return new ComponentPrimaryStylenameProperty();
	}

	public static IComponentValueProperty readonly() {
		return new ComponentReadonlyProperty();
	}

	public static IComponentValueProperty stylename() {
		return new ComponentStylenameProperty();
	}

	public static IComponentValueProperty visible() {
		return new ComponentVisibleProperty();
	}

	public static IComponentValueProperty required() {
		return new FieldRequiredProperty();
	}

	public static IComponentValueProperty requiredError() {
		return new FieldRequiredErrorProperty();
	}

	// /**
	// * Returns a value property for observing the tooltip text of a
	// * component.
	// *
	// * @return a value property for observing the tooltip text.
	// */
	// public static IComponentValueProperty tooltip() {
	// return new ComponentTooltipTextProperty();
	// }
	//
	// /**
	// * Returns a value property for observing the visibility state of a
	// * {@link Component}.
	// *
	// * @return a value property for observing the visibility state of a
	// * {@link Component}.
	// */
	// public static IComponentValueProperty visible() {
	// return new ComponentVisibleProperty();
	// }
}
