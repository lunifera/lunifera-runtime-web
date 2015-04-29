/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;

/**
 */
@SuppressWarnings("serial")
public class PropertyReadonlyChangeListener extends NativePropertyListener
		implements Property.ReadOnlyStatusChangeListener {

	public PropertyReadonlyChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		if (source instanceof Property.ReadOnlyStatusChangeNotifier) {
			Property.ReadOnlyStatusChangeNotifier notifier = (Property.ReadOnlyStatusChangeNotifier) source;
			notifier.addReadOnlyStatusChangeListener(this);
		}
	}

	protected void doRemoveFrom(Object source) {
		if (source instanceof Property.ReadOnlyStatusChangeNotifier) {
			Property.ReadOnlyStatusChangeNotifier notifier = (Property.ReadOnlyStatusChangeNotifier) source;
			notifier.removeReadOnlyStatusChangeListener(this);
		}
	}

	@Override
	public void readOnlyStatusChange(ReadOnlyStatusChangeEvent event) {
		fireChange(event.getProperty().isReadOnly(), null);
	}

}