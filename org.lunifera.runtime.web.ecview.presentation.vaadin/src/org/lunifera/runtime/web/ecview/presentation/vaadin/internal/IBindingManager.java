// TODO : Include Copyright
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.model.core.YEditable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEnable;
import org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;

public interface IBindingManager {

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yVisibleAble
	 * @param field
	 */
	public abstract void bindVisible(YVisibleable yVisibleAble, Field<?> field);

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yEnable
	 * @param field
	 */
	public abstract void bindEnabled(YEnable yEnable, Field<?> field);

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yEditable
	 * @param field
	 */
	public abstract void bindReadonly(YEditable yEditable,
			Property.ReadOnlyStatusChangeNotifier field);

}