/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.common;

/**
 * Disposable objects can be destroyed. Which means, that the state is invalid
 * for further use and internal references are prepared for garbage collection.<br>
 * Object that have been disposed once cannot be used anymore. The creation of a
 * new object is required.
 */
public interface IDisposable {

	/**
	 * Returns true, during the disposal process.
	 * 
	 * @return disposing
	 */
	boolean isDisposing();
	
	/**
	 * Returns true, if this object was disposed.
	 * 
	 * @return disposed
	 */
	boolean isDisposed();

	/**
	 * If called, the element will be disposed. All created ui elements will be
	 * removed from its parent and this class will be prepared for garbage
	 * collection.<br>
	 * NO further use is possible. Creation of a new element required.
	 * <p>
	 * Calling this method twice is allowed.
	 */
	void dispose();

	/**
	 * Adds a listener which will be notified if the object was disposed.<br>
	 * Adding a listener twice has no effect.
	 * 
	 * @param listener Listener to be added
	 */
	void addDisposeListener(IDisposable.Listener listener);

	/**
	 * Remove the dispose listener.
	 * 
	 * @param listener Listener to be removed
	 */
	void removeDisposeListener(IDisposable.Listener listener);

	/**
	 * A public helper util that can be used to check the dispose state of an element.
	 */
	public static class DisposableUtil {

		/**
		 * Throws a {@link DisposeException} if the given object is disposed.
		 * 
		 * @param disposable The disposable object
		 */
		public static void checkDisposed(IDisposable disposable) {
			if (disposable != null && disposable.isDisposed()) {
				throw new DisposeException(disposable);
			}
		}
	}

	/**
	 * Will be thrown if disposed objects are accessed.
	 */
	public static class DisposeException extends RuntimeException {

		private static final long serialVersionUID = 23566849649196005L;

		/**
		 * Constructor.
		 * 
		 * @param o Any kind of object.
		 */
		public DisposeException(Object o) {
			super(o.toString());
		}
	}

	/**
	 * A listener that is notified if the observed object was disposed.
	 */
	public interface Listener {
		/**
		 * Is called if the object was disposed.
		 * 
		 * @param notifier The object sending the dispose event.
		 */
		void notifyDisposed(IDisposable notifier);
	}
}