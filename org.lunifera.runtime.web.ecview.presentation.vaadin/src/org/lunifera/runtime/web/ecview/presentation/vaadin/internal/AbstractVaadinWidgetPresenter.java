/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.internal.databinding.BindingStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.context.ILocaleChangedService;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.disposal.AbstractDisposable;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.datatypes.IDatatypeEditpart.DatatypeChangeEvent;
import org.lunifera.ecview.core.common.editpart.visibility.IVisibilityPropertiesEditpart;
import org.lunifera.ecview.core.common.model.core.YEditable;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.model.core.YEmbeddable;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEnable;
import org.lunifera.ecview.core.common.model.core.YVisibleable;
import org.lunifera.ecview.core.common.model.core.util.CoreModelUtil;
import org.lunifera.ecview.core.common.model.datatypes.YDatatype;
import org.lunifera.ecview.core.common.model.visibility.YColor;
import org.lunifera.ecview.core.common.model.visibility.YVisibilityProperties;
import org.lunifera.ecview.core.common.notification.ILifecycleEvent;
import org.lunifera.ecview.core.common.notification.ILifecycleService;
import org.lunifera.ecview.core.common.notification.LifecycleEvent;
import org.lunifera.ecview.core.common.presentation.IInitializerService;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.common.services.IWidgetAssocationsService;
import org.lunifera.ecview.core.databinding.emf.common.ECViewUpdateValueStrategy;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableList;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * An abstract implementation of the {@link IWidgetPresentation}.
 */
@SuppressWarnings("restriction")
public abstract class AbstractVaadinWidgetPresenter<A extends Component>
		extends AbstractDisposable implements IWidgetPresentation<A>,
		ILocaleChangedService.LocaleListener {

	/**
	 * See {@link IConstants#CSS_CLASS_CONTROL_BASE}.
	 */
	public static final String CSS_CLASS_CONTROL_BASE = IConstants.CSS_CLASS_CONTROL_BASE;

	/**
	 * See {@link IConstants#CSS_CLASS_CONTROL}.
	 */
	public static final String CSS_CLASS_CONTROL = IConstants.CSS_CLASS_CONTROL;

	/**
	 * See {@link IConstants#CSS_CLASS_COMPRESSOR}.
	 */
	public static final String CSS_CLASS_COMPRESSOR = IConstants.CSS_CLASS_COMPRESSOR;

	private IViewContext viewContext;

	private final IEmbeddableEditpart editpart;

	private IBindingManager bindingManger;
	private Set<Binding> bindings = new HashSet<Binding>();

	private VisibilityOptionsApplier visibilityOptionsApplier;

	public AbstractVaadinWidgetPresenter(IEmbeddableEditpart editpart) {
		this.editpart = editpart;
		viewContext = editpart.getView().getContext();
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return the editpart
	 */
	protected IEmbeddableEditpart getEditpart() {
		return editpart;
	}

	@Override
	public Object getModel() {
		return getEditpart().getModel();
	}

	protected YElement getCastedModel() {
		return (YElement) getModel();
	}

	/**
	 * Returns the view context.
	 * 
	 * @return viewContext
	 */
	public IViewContext getViewContext() {
		return viewContext;
	}

	/**
	 * Returns the active locale for the view.
	 * 
	 * @return
	 */
	protected Locale getLocale() {
		return viewContext.getLocale();
	}

	/**
	 * Returns the i18n service or <code>null</code> if no service is available.
	 * 
	 * @return
	 */
	protected II18nService getI18nService() {
		return viewContext.getService(II18nService.ID);
	}

	@Override
	public void localeChanged(Locale locale) {
		doUpdateLocale(locale);
	}

	/**
	 * Needs to be overridden by subclasses to update the locale.
	 * 
	 * @param locale
	 */
	protected abstract void doUpdateLocale(Locale locale);

	@Override
	public void apply(IVisibilityPropertiesEditpart properties) {
		if (visibilityOptionsApplier == null) {
			visibilityOptionsApplier = createVisibilityOptionsApplier();
		}

		visibilityOptionsApplier.apply((YVisibilityProperties) properties
				.getModel());
	}

	@Override
	public void notifyDatatypeChanged(DatatypeChangeEvent event) {
		if (event.isUnsetEvent()) {
			doApplyDatatype(null);
		} else {
			doApplyDatatype((YDatatype) event.getEditpart().getModel());
		}
	}

	protected void doApplyDatatype(YDatatype yDt) {
	}

	@Override
	public void resetVisibilityProperties() {
		visibilityOptionsApplier.apply(null);
	}

	/**
	 * Creates a new instance of the visibility options applier.
	 * 
	 * @return
	 */
	protected VisibilityOptionsApplier createVisibilityOptionsApplier() {
		return new VisibilityOptionsApplier(getWidget());
	}

	/**
	 * Is called to initialize the newly created component.
	 * 
	 * @param component
	 */
	protected void setupComponent(Component component, YElement model) {
		IInitializerService service = getViewContext().getService(
				IInitializerService.class.getName());
		if (service != null) {
			service.initialize(component, model);
		}
	}

	/**
	 * Creates the bindings from the ECView EMF model to the given UI element.
	 * 
	 * @param yEmbeddable
	 * @param container
	 * @param field
	 * @return Binding - the created binding
	 */
	protected void createBindings(YEmbeddable yEmbeddable,
			AbstractComponent widget, AbstractComponent container) {

		bindingManger = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		applyDefaults(yEmbeddable);

		if (container != null) {
			registerBinding(createBindingsVisiblility(yEmbeddable, container));
		} else {
			registerBinding(createBindingsVisiblility(yEmbeddable, widget));
		}

		if (yEmbeddable instanceof YEnable) {
			registerBinding(createBindingsEnabled((YEnable) yEmbeddable, widget));
		}

		if (yEmbeddable instanceof YEditable) {
			registerBinding(createBindingsEditable((YEditable) yEmbeddable,
					widget));
		}

	}

	/**
	 * Creates the binding.
	 * 
	 * @param target
	 * @param model
	 * 
	 * @return binding - the created binding
	 */
	protected Binding createBindings(IObservableValue target,
			IObservableValue model) {
		bindingManger = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());

		return bindingManger.bindValue(target, model);
	}

	/**
	 * Creates the binding.
	 * 
	 * @param target
	 * @param model
	 * @param targetToModel
	 * @param modelToTarget
	 * @return binding - the created binding
	 */
	protected Binding createBindings(IObservableValue target,
			IObservableValue model, UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget) {
		bindingManger = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		return bindingManger.bindValue(target, model, targetToModel,
				modelToTarget);
	}

	/**
	 * Creates the binding.
	 * 
	 * @param target
	 * @param model
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindings(IObservableList target,
			IObservableList model) {
		bindingManger = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());

		return bindingManger.bindList(target, model);
	}

	/**
	 * Binds the editable flag from the ecview model to the ui element.
	 * 
	 * @param yEditable
	 * @param abstractComponent
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsEditable(YEditable yEditable,
			AbstractComponent abstractComponent) {

		if (abstractComponent instanceof Property.ReadOnlyStatusChangeNotifier) {
			return bindingManger.bindReadonly(yEditable,
					(Property.ReadOnlyStatusChangeNotifier) abstractComponent);
		} else {
			return bindingManger.bindReadonlyOneway(yEditable,
					abstractComponent);
		}
	}

	/**
	 * Applies the defaults to the ecview model. Transient values will be
	 * configured properly.
	 * 
	 * @param yEmbeddable
	 */
	protected void applyDefaults(YEmbeddable yEmbeddable) {
		// initialize the transient values
		//
		CoreModelUtil.initTransientValues(yEmbeddable);
	}

	/**
	 * Binds the visible flag from the ecview model to the ui element.
	 * 
	 * @param yVisibleable
	 * @param abstractComponent
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsVisiblility(YVisibleable yVisibleable,
			AbstractComponent abstractComponent) {

		return bindingManger.bindVisible(yVisibleable, abstractComponent);
	}

	/**
	 * Binds the enabled flag from the ecview model to the ui element.
	 * 
	 * @param yEnable
	 * @param abstractComponent
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsEnabled(YEnable yEnable,
			AbstractComponent abstractComponent) {
		IBindingManager bindingManger = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());

		// bind enabled
		return bindingManger.bindEnabled(yEnable, abstractComponent);
	}

	/**
	 * Creates a binding for the value attribute from the ECView-UI-model to the
	 * UI element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * @@return Binding - the created binding
	 */
	protected Binding createBindings_Value(EObject model,
			EStructuralFeature modelFeature, Field<?> field) {
		return createBindingsValue(model, modelFeature, field, null, null);
	}

	/**
	 * Binds the value attribute from the ecview model to the ui element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsValue(EObject model,
			EStructuralFeature modelFeature, Field<?> field,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeValue(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	/**
	 * Creates a binding for the value attribute from the ECView-UI-model to the
	 * UI element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * @@return Binding - the created binding
	 */
	protected Binding createBindingsButtonClick(EObject model,
			EStructuralFeature modelFeature, Button field) {
		return createBindingsButtonClick(model, modelFeature, field, null, null);
	}

	/**
	 * Binds the value attribute from the ecview model to the ui element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsButtonClick(EObject model,
			EStructuralFeature modelFeature, Button field,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeButtonClick(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	/**
	 * Creates a binding for the value attribute from the ECView-UI-model to the
	 * UI element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * @@return Binding - the created binding
	 */
	protected Binding createBindings_Value(EObject model,
			EStructuralFeature modelFeature, ValueChangeNotifier field) {
		return createBindingsValue(model, modelFeature, field, null, null);
	}

	/**
	 * Binds the value attribute from the ecview model to the ui element.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsValue(EObject model,
			EStructuralFeature modelFeature, ValueChangeNotifier field,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeValue(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	/**
	 * Binds the selection of the selectable to the ECView model. It uses an
	 * validator to detect problems setting a not allowed selection. In that
	 * case, the current selection of the UI-model is passed to the ECView model
	 * again.
	 * 
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @param type
	 *            the type of selected object
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsSelection(EObject model,
			EStructuralFeature modelFeature, final AbstractSelect field,
			Class<?> type) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeSingleSelection(field, type);

			// create a modelToTarget update strategy with a validator
			//
			ECViewUpdateValueStrategy modelToTarget = new ECViewUpdateValueStrategy(
					ECViewUpdateValueStrategy.POLICY_UPDATE);
			modelToTarget.setBeforeSetValidator(new IValidator() {
				@Override
				public IStatus validate(Object value) {
					if (value != null && !value.equals("")
							&& !field.containsId(value)) {
						return Status.CANCEL_STATUS;
					}

					return Status.OK_STATUS;
				}
			});

			final Binding binding = bindingManager.bindValue(uiObservable,
					modelObservable, null, modelToTarget);
			registerBinding(binding);

			// now bind the validation state to an observable value. If the
			// doSetValue is called, we check whether the set operation was
			// successfully. Otherwise we send the target value back to the
			// model.
			Binding validationBinding = bindingManager.bindValue(
					binding.getValidationStatus(),
					new AbstractObservableValue() {

						@Override
						public Object getValueType() {
							return null;
						}

						@Override
						protected Object doGetValue() {
							return null;
						}

						@Override
						protected void doSetValue(Object value) {
							BindingStatus status = (BindingStatus) value;
							if (status.getSeverity() == BindingStatus.CANCEL) {
								binding.updateTargetToModel();
							}
						}
					});
			registerBinding(validationBinding);

			return binding;
		}
		return null;
	}

	/**
	 * Binds the multi selection of the selectable to the ECView model. It uses
	 * an validator to detect problems setting a not allowed selection. In that
	 * case, the current selection of the UI-model is passed to the ECView model
	 * again.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @param collectionType
	 *            the type contained in the selection result
	 * 
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsMultiSelection(EObject model,
			EStructuralFeature modelFeature, final AbstractSelect field,
			Class<?> collectionType) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableList modelObservable = EMFProperties.list(modelFeature)
					.observe(getModel());
			IVaadinObservableList uiObservable = VaadinObservables
					.observeMultiSelectionAsList(field, collectionType);

			final Binding binding = bindingManager.bindList(uiObservable,
					modelObservable, null, null);
			registerBinding(binding);

			return binding;
		}
		return null;
	}

	/**
	 * Creates a binding for the contents of the vaadin container from the
	 * ECView-UI-model to the UI element.
	 * 
	 * @param model
	 *            the ECView model
	 * @param modelFeature
	 *            the eFeature of the model
	 * @param field
	 *            the ui field
	 * @param collectionType
	 *            the type of the collection contents
	 * @return Binding - the created binding
	 */
	protected Binding createBindings_ContainerContents(EObject model,
			EStructuralFeature modelFeature,
			Container.ItemSetChangeNotifier field, Class<?> collectionType) {
		return createBindingsContainerContents(model, modelFeature, field,
				collectionType, null, null);
	}

	/**
	 * Creates a binding for the contents of the vaadin container from the
	 * ECView-UI-model to the UI element.
	 * 
	 * @param model
	 *            the ECView model
	 * @param modelFeature
	 *            the eFeature of the model
	 * @param field
	 *            the ui field
	 * @param collectionType
	 *            the type of the collection contents
	 * @param targetToModel
	 *            the update strategy
	 * @param modelToTarget
	 *            the update strategy
	 * @return Binding - the created binding
	 */
	protected Binding createBindingsContainerContents(EObject model,
			EStructuralFeature modelFeature,
			Container.ItemSetChangeNotifier field, Class<?> collectionType,
			UpdateListStrategy targetToModel, UpdateListStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableList modelObservable = EMFProperties.list(modelFeature)
					.observe(getModel());
			IObservableList uiObservable = VaadinObservables
					.observeContainerItemSetContents(field, collectionType);
			return bindingManager.bindList(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	/**
	 * Registers the given binding to be managed by the presenter. If the widget
	 * becomes disposed or unrendered, all the bindings will become disposed.
	 * 
	 * @param binding
	 */
	protected void registerBinding(Binding binding) {
		bindings.add(binding);
	}

	/**
	 * Unbinds all currently active bindings.
	 */
	protected void unbind() {
		for (Binding binding : bindings) {
			binding.dispose();
		}
		bindings.clear();
	}

	@Override
	public IObservable getObservableValue(Object model) {
		return internalGetObservableEndpoint((YEmbeddableBindingEndpoint) model);
	}

	/**
	 * Has to provide an instance of IObservable for the given bindableValue.
	 * 
	 * @param bindableValue
	 * @return
	 */
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		throw new UnsupportedOperationException("Must be overridden!");
	}

	protected EObject castEObject(Object model) {
		return (EObject) model;
	}

	@Override
	protected void internalDispose() {
		unregisterFromLocaleChangedService();
	}

	/**
	 * Uses the {@link IWidgetAssocationsService} to register the widget.
	 * 
	 * @param component
	 * @param yElement
	 */
	protected void associateWidget(Component component, EObject yElement) {
		IWidgetAssocationsService service = getViewContext().getService(
				IWidgetAssocationsService.ID);
		service.associate(component, yElement);
	}

	/**
	 * Uses the {@link IWidgetAssocationsService} to unregister the widget.
	 * 
	 * @param component
	 */
	protected void unassociateWidget(Component component) {
		IWidgetAssocationsService service = getViewContext().getService(
				IWidgetAssocationsService.ID);
		service.remove(component);
	}

	/**
	 * Locale change events are catched by that class.
	 */
	protected void registerAtLocaleChangedService() {
		ILocaleChangedService service = getViewContext().getService(
				ILocaleChangedService.ID);
		service.addLocaleListener(this);
	}

	/**
	 * Locale change events are not catched by that class.
	 */
	protected void unregisterFromLocaleChangedService() {
		ILocaleChangedService service = getViewContext().getService(
				ILocaleChangedService.ID);
		service.removeLocaleListener(this);
	}

	@Override
	protected void notifyDisposeListeners() {
		super.notifyDisposeListeners();
		sendDisposedLifecycleEvent();
	}

	/**
	 * Send a dispose lifecycle event to all registered listeners.
	 */
	protected void sendDisposedLifecycleEvent() {
		ILifecycleService service = getViewContext().getService(
				ILifecycleService.class.getName());
		if (service != null) {
			service.notifyLifecycle(new LifecycleEvent(getEditpart(),
					ILifecycleEvent.TYPE_DISPOSED));
		}
	}

	/**
	 * Send a rendered lifecycle event to all registered listeners.
	 */
	protected void sendRenderedLifecycleEvent() {
		ILifecycleService service = getViewContext().getService(
				ILifecycleService.class.getName());
		if (service != null) {
			service.notifyLifecycle(new LifecycleEvent(getEditpart(),
					ILifecycleEvent.TYPE_RENDERED));
		}
	}

	/**
	 * Send a rendered lifecycle event to all registered listeners.
	 */
	protected void sendUnrenderedLifecycleEvent() {
		ILifecycleService service = getViewContext().getService(
				ILifecycleService.class.getName());
		if (service != null) {
			service.notifyLifecycle(new LifecycleEvent(getEditpart(),
					ILifecycleEvent.TYPE_UNRENDERED));
		}
	}

	/**
	 * For testing purposes.
	 * 
	 * @return
	 */
	@Override
	public Set<Binding> getUIBindings() {
		return bindings;
	}

	@Override
	public A createWidget(Object parent) {
		A result = doCreateWidget(parent);

		registerAtLocaleChangedService();

		return result;
	}

	/**
	 * Needs to be implemented by subclasses to render the widget.
	 * 
	 * @param parent
	 *            - The parent ui component
	 */
	protected abstract A doCreateWidget(Object parent);

	@Override
	public void unrender() {
		unregisterFromLocaleChangedService();
		doUnrender();
	}

	/**
	 * Needs to be implemented by subclasses to unrender the widget.
	 */
	protected abstract void doUnrender();

	/**
	 * Applies the visibility options to the component.
	 */
	protected static class VisibilityOptionsApplier {

		protected final Component component;

		public VisibilityOptionsApplier(Component component) {
			this.component = component;
		}

		public void resetStylenames() {
			component.removeStyleName("l-strikethrough");
			component.removeStyleName("l-border");
			component.removeStyleName("l-italic");
			component.removeStyleName("l-underline");
			component.removeStyleName("l-foreground");
			component.removeStyleName("l-background");
		}

		/**
		 * Applies the visibility options to the component. Passing
		 * <code>null</code> as argument means a reset to default values.
		 * 
		 * @param yProps
		 */
		public void apply(YVisibilityProperties yProps) {

			resetStylenames();

			applyVisible(yProps);
			applyEnabled(yProps);
			applyReadOnly(yProps);

			applyStrikeThrough(yProps);

			applyBorder(yProps);

			applyItalic(yProps);

			applyUnderline(yProps);

			applyForegroundColor(yProps);

			applyBackgroundColor(yProps);
		}

		public void applyUnderline(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			if (yProps.isUnderline()) {
				component.addStyleName("l-underline");
			}
		}

		public void applyItalic(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			if (yProps.isItalic()) {
				component.addStyleName("l-italic");
			}
		}

		public void applyBorder(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			if (yProps.isBorder()) {
				component.addStyleName("l-border");
			}
		}

		public void applyStrikeThrough(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			if (yProps.isStrikethrough()) {
				component.addStyleName("l-strikethrough");
			}
		}

		public void applyReadOnly(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			component.setReadOnly(!yProps.isEditable());
		}

		public void applyEnabled(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			component.setEnabled(yProps.isEnabled());
		}

		public void applyVisible(YVisibilityProperties yProps) {
			if (yProps == null) {
				return;
			}
			component.setVisible(yProps.isVisible());
		}

		public void applyForegroundColor(YVisibilityProperties yProps) {
			YColor yColor = yProps.getForegroundColor();
			if (yColor != null) {
				Color c = new Color(yColor.getRed(), yColor.getGreen(),
						yColor.getBlue());
				component.addStyleName("l-foreground:" + c.getCSS());
			}
		}

		public void applyBackgroundColor(YVisibilityProperties yProps) {
			YColor yColor = yProps.getBackgroundColor();
			if (yColor != null) {
				Color c = new Color(yColor.getRed(), yColor.getGreen(),
						yColor.getBlue());
				component.addStyleName("l-background:" + c.getCSS());
			}
		}

	}

}
