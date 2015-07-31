/**
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Emf Bar</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getName
 * <em>Name</em>}</li>
 * <li>
 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getMyfoo
 * <em>Myfoo</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage#getEmfBar()
 * @model
 * @generated
 */
public interface EmfBar extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage#getEmfBar_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Myfoo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Myfoo</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Myfoo</em>' containment reference.
	 * @see #setMyfoo(EmfFoo)
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage#getEmfBar_Myfoo()
	 * @model containment="true"
	 * @generated
	 */
	EmfFoo getMyfoo();

	/**
	 * Sets the value of the '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getMyfoo
	 * <em>Myfoo</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Myfoo</em>' containment reference.
	 * @see #getMyfoo()
	 * @generated
	 */
	void setMyfoo(EmfFoo value);

} // EmfBar
