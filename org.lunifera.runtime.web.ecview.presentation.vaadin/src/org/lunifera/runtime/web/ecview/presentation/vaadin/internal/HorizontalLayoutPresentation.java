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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.ILayoutEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YAlignment;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayoutCellStyle;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class HorizontalLayoutPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HorizontalLayoutPresentation.class);

	private CssLayout componentBase;
	private HorizontalLayout horizontalLayout;
	private ModelAccess modelAccess;

	private CssLayout fillerLayout;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public HorizontalLayoutPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess(
				(YHorizontalLayout) editpart.getModel());
	}

	@Override
	public void add(IWidgetPresentation<?> presentation) {
		super.add(presentation);

		YEmbeddable yChild = (YEmbeddable) presentation.getModel();
		addChild(presentation, modelAccess.getCellStyle(yChild));
	}

	@Override
	public void remove(IWidgetPresentation<?> presentation) {
		super.remove(presentation);

		horizontalLayout.removeComponent((Component) presentation.getWidget());
	}

	@Override
	public void insert(IWidgetPresentation<?> presentation, int index) {
		super.insert(presentation, index);

		refreshUI();
	}

	@Override
	public void move(IWidgetPresentation<?> presentation, int index) {
		super.move(presentation, index);

		refreshUI();
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// no need to set the locale to the ui elements. Is handled by vaadin
		// internally.

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
	}

	/**
	 * Is called to refresh the UI. The element will be removed from the grid
	 * layout and added to it again afterwards.
	 */
	protected void refreshUI() {
		horizontalLayout.removeAllComponents();

		// create a map containing the style for the embeddable
		//
		Map<YEmbeddable, YHorizontalLayoutCellStyle> yStyles = new HashMap<YEmbeddable, YHorizontalLayoutCellStyle>();
		for (YHorizontalLayoutCellStyle style : modelAccess.getCellStyles()) {
			if (yStyles.containsKey(style.getTarget())) {
				LOGGER.warn("Multiple style for element {}", style.getTarget());
			}
			yStyles.put(style.getTarget(), style);
		}

		// iterate all elements and build the child element
		//
		for (IEmbeddableEditpart editPart : getEditpart().getElements()) {
			IWidgetPresentation<?> childPresentation = editPart
					.getPresentation();
			YEmbeddable yChild = (YEmbeddable) childPresentation.getModel();
			addChild(childPresentation, yStyles.get(yChild));
		}

		if (!modelAccess.isFillHorizontal()) {
			fillerLayout = new CssLayout();
			fillerLayout.setSizeFull();
			horizontalLayout.addComponent(fillerLayout);
			horizontalLayout.setExpandRatio(fillerLayout, 1.0f);
		}

	}

	/**
	 * Is called to create the child component and apply layouting defaults to
	 * it.
	 * 
	 * @param presentation
	 * @param yStyle
	 * @return
	 */
	protected Cell addChild(IWidgetPresentation<?> presentation,
			YHorizontalLayoutCellStyle yStyle) {

		Component child = (Component) presentation
				.createWidget(horizontalLayout);

		// calculate and apply the alignment to be used
		//
		YAlignment yAlignment = yStyle != null && yStyle.getAlignment() != null ? yStyle
				.getAlignment() : null;
		if (yAlignment == null) {
			// use default
			yAlignment = YAlignment.TOP_LEFT;

			if (modelAccess.isFillHorizontal()) {
				// ensure that horizontal alignment is FILL
				yAlignment = mapToHorizontalFill(yAlignment);
			}
		}

		horizontalLayout.addComponent(child);
		applyAlignment(child, yAlignment);

		return new Cell(child, yAlignment);
	}

	/**
	 * Sets the alignment to the component.
	 * 
	 * @param child
	 * @param yAlignment
	 */
	protected void applyAlignment(Component child, YAlignment yAlignment) {

		if (yAlignment != null) {
			child.setWidth("-1%");
			child.setHeight("-1%");
			switch (yAlignment) {
			case BOTTOM_CENTER:
				horizontalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_CENTER);
				break;
			case BOTTOM_FILL:
				horizontalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_LEFT);
				child.setWidth("100%");
				break;
			case BOTTOM_LEFT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_LEFT);
				break;
			case BOTTOM_RIGHT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_RIGHT);
				break;
			case MIDDLE_CENTER:
				horizontalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_CENTER);
				break;
			case MIDDLE_FILL:
				horizontalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_LEFT);
				child.setWidth("100%");
				break;
			case MIDDLE_LEFT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_LEFT);
				break;
			case MIDDLE_RIGHT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_RIGHT);
				break;
			case TOP_CENTER:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_CENTER);
				break;
			case TOP_FILL:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_LEFT);
				child.setWidth("100%");
				break;
			case TOP_LEFT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_LEFT);
				break;
			case TOP_RIGHT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_RIGHT);
				break;
			case FILL_CENTER:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_CENTER);
				child.setHeight("100%");
				break;
			case FILL_FILL:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_LEFT);
				child.setWidth("100%");
				child.setHeight("100%");
				break;
			case FILL_LEFT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_LEFT);
				child.setHeight("100%");
				break;
			case FILL_RIGHT:
				horizontalLayout.setComponentAlignment(child,
						Alignment.TOP_RIGHT);
				child.setHeight("100%");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Maps the vertical part of the alignment to FILL.
	 * 
	 * @param yAlignment
	 *            the alignment
	 * @return alignment the mapped alignment
	 */
	// BEGIN SUPRESS CATCH EXCEPTION
	protected YAlignment mapToVerticalFill(YAlignment yAlignment) {
		// END SUPRESS CATCH EXCEPTION
		if (yAlignment != null) {
			switch (yAlignment) {
			case BOTTOM_CENTER:
			case MIDDLE_CENTER:
			case TOP_CENTER:
				return YAlignment.FILL_CENTER;
			case BOTTOM_FILL:
			case MIDDLE_FILL:
			case TOP_FILL:
				return YAlignment.FILL_FILL;
			case BOTTOM_LEFT:
			case MIDDLE_LEFT:
			case TOP_LEFT:
				return YAlignment.FILL_LEFT;
			case BOTTOM_RIGHT:
			case MIDDLE_RIGHT:
			case TOP_RIGHT:
				return YAlignment.FILL_RIGHT;
			case FILL_FILL:
			case FILL_LEFT:
			case FILL_RIGHT:
			case FILL_CENTER:
				return YAlignment.FILL_FILL;
			default:
				break;
			}
		}
		return YAlignment.FILL_FILL;
	}

	/**
	 * Maps the horizontal part of the alignment to FILL.
	 * 
	 * @param yAlignment
	 *            the alignment
	 * @return alignment the mapped alignment
	 */
	// BEGIN SUPRESS CATCH EXCEPTION
	protected YAlignment mapToHorizontalFill(YAlignment yAlignment) {
		// END SUPRESS CATCH EXCEPTION
		if (yAlignment != null) {
			switch (yAlignment) {
			case BOTTOM_CENTER:
			case BOTTOM_FILL:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				return YAlignment.BOTTOM_FILL;
			case MIDDLE_CENTER:
			case MIDDLE_FILL:
			case MIDDLE_LEFT:
			case MIDDLE_RIGHT:
				return YAlignment.MIDDLE_FILL;
			case TOP_CENTER:
			case TOP_FILL:
			case TOP_LEFT:
			case TOP_RIGHT:
				return YAlignment.TOP_FILL;
			case FILL_FILL:
			case FILL_LEFT:
			case FILL_RIGHT:
			case FILL_CENTER:
				return YAlignment.FILL_FILL;
			default:
				break;
			}
		}
		return YAlignment.FILL_FILL;
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			componentBase.setImmediate(true);

			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			horizontalLayout = new HorizontalLayout();
			componentBase.addComponent(horizontalLayout);

			if (modelAccess.isMargin()) {
				horizontalLayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				horizontalLayout.setMargin(true);
			}

			if (!modelAccess.isSpacing()) {
				horizontalLayout.setSpacing(false);
			} else {
				horizontalLayout.setData(IConstants.CSS_CLASS_SPACING);
				horizontalLayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				horizontalLayout.addStyleName(modelAccess.getCssClass());
			} else {
				horizontalLayout.addStyleName(CSS_CLASS_CONTROL);
			}

			renderChildren(false);
		}

		return componentBase;
	}

	@Override
	public ComponentContainer getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	@Override
	public void doUnrender() {
		if (componentBase != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			horizontalLayout = null;

			// unrender the childs
			for (IWidgetPresentation<?> child : getChildren()) {
				child.unrender();
			}
		}
	}

	@Override
	public void renderChildren(boolean force) {
		if (force) {
			unrenderChildren();
		}

		refreshUI();
	}

	/**
	 * Will unrender all children.
	 */
	protected void unrenderChildren() {
		for (IWidgetPresentation<?> presentation : getChildren()) {
			if (presentation.isRendered()) {
				presentation.unrender();
			}
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YHorizontalLayout yLayout;

		public ModelAccess(YHorizontalLayout yLayout) {
			super();
			this.yLayout = yLayout;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yLayout.getCssClass();
		}

		/**
		 * Returns true, if the css class is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssClassValid() {
			return getCssClass() != null && !getCssClass().equals("");
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YHorizontalLayout#isSpacing()
		 */
		public boolean isSpacing() {
			return yLayout.isSpacing();
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yLayout.getCssID();
		}

		/**
		 * Returns true, if the css id is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssIdValid() {
			return getCssID() != null && !getCssID().equals("");
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YHorizontalLayout#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YHorizontalLayout#getCellStyles()
		 */
		public EList<YHorizontalLayoutCellStyle> getCellStyles() {
			return yLayout.getCellStyles();
		}

		public YHorizontalLayoutCellStyle getCellStyle(YEmbeddable element) {
			return yLayout.getCellStyle(element);
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YHorizontalLayout#isFillHorizontal()
		 */
		public boolean isFillHorizontal() {
			return yLayout.isFillHorizontal();
		}
	}

	public static class Cell {
		private final Component component;
		private final YAlignment alignment;

		public Cell(Component component, YAlignment alignment) {
			super();
			this.component = component;
			this.alignment = alignment;
		}

		/**
		 * @return the component
		 */
		protected Component getComponent() {
			return component;
		}

		/**
		 * @return the alignment
		 */
		protected YAlignment getAlignment() {
			return alignment;
		}

	}
}
