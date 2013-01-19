/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.SWTObservables (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.databinding;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.databinding.observable.Realm;

import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Field;
import com.vaadin.ui.UI;

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
					return displayRealm;
				}
			}
			UIRealm result = new UIRealm(ui);
			realms.add(result);
			return result;
		}
	}

	/**
	 * Returns an observable value tracking the value of the given field.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeValue(Field<?> field) {
		return VaadinProperties.value().observe(field);
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
		return VaadinProperties.caption().observe(component);
	}

	/**
	 * Returns an observable value tracking the enabled state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeEnabled(Component component) {
		return VaadinProperties.enabled().observe(component);
	}

	/**
	 * Returns an observable value tracking the icon of the given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeIcon(Component component) {
		return VaadinProperties.icon().observe(component);
	}

	/**
	 * Returns an observable value tracking the primary style name of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observePrimaryStylename(
			Component component) {
		return VaadinProperties.primaryStylename().observe(component);
	}

	/**
	 * Returns an observable value tracking the readonly state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeReadonly(Component component) {
		return VaadinProperties.readonly().observe(component);
	}

	/**
	 * Returns an observable value tracking the stylename of the given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeStylename(Component component) {
		return VaadinProperties.stylename().observe(component);
	}

	/**
	 * Returns an observable value tracking the visible state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeVisible(Component component) {
		return VaadinProperties.visible().observe(component);
	}

	/**
	 * Returns an observable value tracking the required state of the given
	 * widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeRequired(Field<?> field) {
		return VaadinProperties.required().observe(field);
	}

	/**
	 * Returns an observable value tracking the "required error message" of the
	 * given widget.
	 * 
	 * @param field
	 * @return
	 */
	public static IVaadinObservableValue observeRequiredError(Field<?> field) {
		return VaadinProperties.requiredError().observe(field);
	}

	private static class UIRealm extends Realm {
		private UI ui;

		/**
		 * @param ui
		 */
		private UIRealm(UI ui) {
			this.ui = ui;
			setDefault(this);
		}

		public boolean isCurrent() {
			return UI.getCurrent() == ui;
		}

		public void asyncExec(final Runnable runnable) {
			// Runnable safeRunnable = new Runnable() {
			// public void run() {
			// safeRun(runnable);
			// }
			// };
			throw new UnsupportedOperationException("Not a valid call!");
		}

		public void timerExec(int milliseconds, final Runnable runnable) {
			throw new UnsupportedOperationException("Not a valid call!");
		}

		public int hashCode() {
			return (ui == null) ? 0 : ui.hashCode();
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final UIRealm other = (UIRealm) obj;
			if (ui == null) {
				if (other.ui != null)
					return false;
			} else if (!ui.equals(other.ui))
				return false;
			return true;
		}
	}
}
