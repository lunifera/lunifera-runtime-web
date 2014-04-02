package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class Bar implements Serializable {
	  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	  
	  private boolean disposed;
	  
	  private String name;
	  
	  private Foo myfoo;
  /**
   * Returns true, if the object is disposed. 
   * Disposed means, that it is prepared for garbage collection and may not be used anymore. 
   * Accessing objects that are already disposed will cause runtime exceptions.
   */
  public boolean isDisposed() {
    return this.disposed;
  }
  
  /**
   * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
   */
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  /**
   * @see PropertyChangeSupport#addPropertyChangeListener(String, PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }
  
  /**
   * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }
  
  /**
   * @see PropertyChangeSupport#removePropertyChangeListener(String, PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }
  
  /**
   * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
   */
  public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  /**
   * Checks whether the object is disposed.
   * @throws RuntimeException if the object is disposed.
   */
  private void checkDisposed() {
    if (isDisposed()) {
      throw new RuntimeException("Object already disposed: " + this);
    }
  }
  
  /**
   * Calling dispose will destroy that instance. The internal state will be 
   * set to 'disposed' and methods of that object must not be used anymore. 
   * Each call will result in runtime exceptions.<br/>
   * If this object keeps composition containments, these will be disposed too. 
   * So the whole composition containment tree will be disposed on calling this method.
   */
  public void dispose() {
    if (isDisposed()) {
      return;
    }
    disposed = true;
  }
  
  /**
   * Returns the name property or <code>null</code> if not present.
   */
  public String getName() {
    checkDisposed();
    return this.name;
  }
  
  /**
   * Sets the <code>name</code> property to this instance.
   * 
   * @param name - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setName(final String name) {
    firePropertyChange("name", this.name, this.name = name );
  }
  
  /**
   * Returns the myfoo property or <code>null</code> if not present.
   */
  public Foo getMyfoo() {
    checkDisposed();
    return this.myfoo;
  }
  
  /**
   * Sets the <code>myfoo</code> property to this instance.
   * Since the reference has an opposite reference, the opposite <code>foo#
   * mybars</code> of the <code>myfoo</code> will be handled automatically and no 
   * further coding is required to keep them in sync.<p>
   * See {@link foo#setMybars(foo)
   * 
   * @param myfoo - the property
   * @throws RuntimeException if instance is <code>disposed</code>
   * 
   */
  public void setMyfoo(final Foo myfoo) {
    checkDisposed();
    if (this.myfoo != null) {
      this.myfoo.internalRemoveFromMybars(this);
    }
    internalSetMyfoo(myfoo);
    if (this.myfoo != null) {
      this.myfoo.internalAddToMybars(this);
    }
    
  }
  
  /**
   * For internal use only!
   */
  public void internalSetMyfoo(final Foo myfoo) {
    firePropertyChange("myfoo", this.myfoo, this.myfoo = myfoo);
  }
}