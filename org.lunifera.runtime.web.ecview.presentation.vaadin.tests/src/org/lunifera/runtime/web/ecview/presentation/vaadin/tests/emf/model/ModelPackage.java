/**
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.lunifera.org/vaadin/presentation/tests";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelPackage eINSTANCE = org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfFooImpl
	 * <em>Emf Foo</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfFooImpl
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelPackageImpl#getEmfFoo()
	 * @generated
	 */
	int EMF_FOO = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_FOO__NAME = 0;

	/**
	 * The number of structural features of the '<em>Emf Foo</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_FOO_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Emf Foo</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_FOO_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl
	 * <em>Emf Bar</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelPackageImpl#getEmfBar()
	 * @generated
	 */
	int EMF_BAR = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_BAR__NAME = 0;

	/**
	 * The feature id for the '<em><b>Myfoo</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_BAR__MYFOO = 1;

	/**
	 * The number of structural features of the '<em>Emf Bar</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_BAR_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Emf Bar</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMF_BAR_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo
	 * <em>Emf Foo</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Emf Foo</em>'.
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo
	 * @generated
	 */
	EClass getEmfFoo();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo#getName()
	 * @see #getEmfFoo()
	 * @generated
	 */
	EAttribute getEmfFoo_Name();

	/**
	 * Returns the meta object for class '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar
	 * <em>Emf Bar</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Emf Bar</em>'.
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar
	 * @generated
	 */
	EClass getEmfBar();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getName()
	 * @see #getEmfBar()
	 * @generated
	 */
	EAttribute getEmfBar_Name();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getMyfoo
	 * <em>Myfoo</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Myfoo</em>'.
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar#getMyfoo()
	 * @see #getEmfBar()
	 * @generated
	 */
	EReference getEmfBar_Myfoo();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfFooImpl
		 * <em>Emf Foo</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfFooImpl
		 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelPackageImpl#getEmfFoo()
		 * @generated
		 */
		EClass EMF_FOO = eINSTANCE.getEmfFoo();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EMF_FOO__NAME = eINSTANCE.getEmfFoo_Name();

		/**
		 * The meta object literal for the '
		 * {@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl
		 * <em>Emf Bar</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl
		 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.ModelPackageImpl#getEmfBar()
		 * @generated
		 */
		EClass EMF_BAR = eINSTANCE.getEmfBar();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EMF_BAR__NAME = eINSTANCE.getEmfBar_Name();

		/**
		 * The meta object literal for the '<em><b>Myfoo</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference EMF_BAR__MYFOO = eINSTANCE.getEmfBar_Myfoo();

	}

} // ModelPackage
