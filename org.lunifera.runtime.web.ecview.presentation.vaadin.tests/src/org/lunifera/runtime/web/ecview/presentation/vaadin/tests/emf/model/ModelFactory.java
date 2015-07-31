/**
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelFactory eINSTANCE = org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Emf Foo</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Emf Foo</em>'.
	 * @generated
	 */
	EmfFoo createEmfFoo();

	/**
	 * Returns a new object of class '<em>Emf Bar</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Emf Bar</em>'.
	 * @generated
	 */
	EmfBar createEmfBar();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} // ModelFactory
