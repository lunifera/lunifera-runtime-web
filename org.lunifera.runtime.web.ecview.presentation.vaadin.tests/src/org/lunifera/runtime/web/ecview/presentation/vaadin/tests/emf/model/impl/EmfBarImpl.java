/**
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Emf Bar</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.impl.EmfBarImpl#getMyfoo <em>Myfoo</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EmfBarImpl extends MinimalEObjectImpl.Container implements EmfBar {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMyfoo() <em>Myfoo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMyfoo()
	 * @generated
	 * @ordered
	 */
	protected EmfFoo myfoo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EmfBarImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.EMF_BAR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.EMF_BAR__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmfFoo getMyfoo() {
		return myfoo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMyfoo(EmfFoo newMyfoo, NotificationChain msgs) {
		EmfFoo oldMyfoo = myfoo;
		myfoo = newMyfoo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.EMF_BAR__MYFOO, oldMyfoo, newMyfoo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMyfoo(EmfFoo newMyfoo) {
		if (newMyfoo != myfoo) {
			NotificationChain msgs = null;
			if (myfoo != null)
				msgs = ((InternalEObject)myfoo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.EMF_BAR__MYFOO, null, msgs);
			if (newMyfoo != null)
				msgs = ((InternalEObject)newMyfoo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.EMF_BAR__MYFOO, null, msgs);
			msgs = basicSetMyfoo(newMyfoo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.EMF_BAR__MYFOO, newMyfoo, newMyfoo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.EMF_BAR__MYFOO:
				return basicSetMyfoo(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.EMF_BAR__NAME:
				return getName();
			case ModelPackage.EMF_BAR__MYFOO:
				return getMyfoo();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.EMF_BAR__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.EMF_BAR__MYFOO:
				setMyfoo((EmfFoo)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.EMF_BAR__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.EMF_BAR__MYFOO:
				setMyfoo((EmfFoo)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.EMF_BAR__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.EMF_BAR__MYFOO:
				return myfoo != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //EmfBarImpl
