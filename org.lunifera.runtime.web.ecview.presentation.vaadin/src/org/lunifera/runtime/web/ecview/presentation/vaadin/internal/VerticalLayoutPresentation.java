/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.ILayoutEditpart;
import org.lunifera.ecview.core.common.model.core.YAlignment;
import org.lunifera.ecview.core.common.model.core.YEmbeddable;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayoutCellStyle;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractLayoutPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class VerticalLayoutPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VerticalLayoutPresentation.class);

	private VerticalLayout verticalLayout;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public VerticalLayoutPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess(
				(YVerticalLayout) editpart.getModel());
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
		verticalLayout.removeAllComponents();

		// create a map containing the style for the embeddable
		//
		Map<YEmbeddable, YVerticalLayoutCellStyle> yStyles = new HashMap<YEmbeddable, YVerticalLayoutCellStyle>();
		for (YVerticalLayoutCellStyle style : modelAccess.getCellStyles()) {
			if (yStyles.containsKey(style.getTarget())) {
				LOGGER.warn("Multiple style for element {}", style.getTarget());
			}
			yStyles.put(style.getTarget(), style);
		}

		// iterate all elements and build the child element
		//
		List<Cell> cells = new ArrayList<Cell>();
		for (IEmbeddableEditpart childPresentation : getChildren()) {
			YEmbeddable yChild = (YEmbeddable) childPresentation.getModel();
			cells.add(addChildComponent(childPresentation, yStyles.get(yChild)));
		}

		for (Cell cell : cells) {
			if (cell.isExpandVertical()) {
				// expandVerticalFound = true;
				verticalLayout.setExpandRatio(cell.getComponent(), 1.0f);
			}
		}

	}

	/**
	 * Is called to create the child component and apply layouting defaults to
	 * it.
	 * 
	 * @param editpart
	 * @param yStyle
	 * @return
	 */
	protected Cell addChildComponent(IEmbeddableEditpart editpart,
			YVerticalLayoutCellStyle yStyle) {

		Component child = (Component) editpart.render(verticalLayout);

		// calculate and apply the alignment to be used
		//
		YAlignment yAlignment = yStyle != null && yStyle.getAlignment() != null ? yStyle
				.getAlignment() : YAlignment.TOP_LEFT;

		verticalLayout.addComponent(child);
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
			child.setSizeUndefined();
			switch (yAlignment) {
			case BOTTOM_CENTER:
				verticalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_CENTER);
				break;
			case BOTTOM_FILL:
				verticalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_LEFT);
				child.setWidth("100%");
				break;
			case BOTTOM_LEFT:
				verticalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_LEFT);
				break;
			case BOTTOM_RIGHT:
				verticalLayout.setComponentAlignment(child,
						Alignment.BOTTOM_RIGHT);
				break;
			case MIDDLE_CENTER:
				verticalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_CENTER);
				break;
			case MIDDLE_FILL:
				verticalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_LEFT);
				child.setWidth("100%");
				break;
			case MIDDLE_LEFT:
				verticalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_LEFT);
				break;
			case MIDDLE_RIGHT:
				verticalLayout.setComponentAlignment(child,
						Alignment.MIDDLE_RIGHT);
				break;
			case TOP_CENTER:
				verticalLayout.setComponentAlignment(child,
						Alignment.TOP_CENTER);
				break;
			case TOP_FILL:
				verticalLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				break;
			case TOP_LEFT:
				verticalLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				break;
			case TOP_RIGHT:
				verticalLayout
						.setComponentAlignment(child, Alignment.TOP_RIGHT);
				break;
			case FILL_CENTER:
				verticalLayout.setComponentAlignment(child,
						Alignment.TOP_CENTER);
				child.setHeight("100%");
				break;
			case FILL_FILL:
				verticalLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				child.setHeight("100%");
				break;
			case FILL_LEFT:
				verticalLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setHeight("100%");
				break;
			case FILL_RIGHT:
				verticalLayout
						.setComponentAlignment(child, Alignment.TOP_RIGHT);
				child.setHeight("100%");
				break;
			default:
				break;
			}
		}
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (verticalLayout == null) {
			verticalLayout = new VerticalLayout();
			setupComponent(verticalLayout, getCastedModel());

			associateWidget(verticalLayout, modelAccess.yLayout);

			if (modelAccess.isCssIdValid()) {
				verticalLayout.setId(modelAccess.getCssID());
			} else {
				verticalLayout.setId(getEditpart().getId());
			}

			if (modelAccess.isMargin()) {
				verticalLayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				verticalLayout.setMargin(true);
			}

			if (!modelAccess.isSpacing()) {
				verticalLayout.setSpacing(false);
			} else {
				verticalLayout.addStyleName(IConstants.CSS_CLASS_SPACING);
				verticalLayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				verticalLayout.addStyleName(modelAccess.getCssClass());
			} else {
				verticalLayout.addStyleName(CSS_CLASS_CONTROL);
			}
			verticalLayout.addStyleName(IConstants.CSS_CLASS_VERTICALLAYOUT);

			// creates the binding for the field
			createBindings(modelAccess.yLayout, verticalLayout, null);

			// initialize all children
			initializeChildren();

			// and now render children
			renderChildren(false);
		}

		return verticalLayout;
	}

	/**
	 * Adds the children to the superclass and prevents rendering.
	 */
	private void initializeChildren() {
		setRenderLock(true);
		try {
			for (IEmbeddableEditpart editPart : getEditpart().getElements()) {
				super.add(editPart);
			}
		} finally {
			setRenderLock(false);
		}
	}

	@Override
	public ComponentContainer getWidget() {
		return verticalLayout;
	}

	@Override
	public boolean isRendered() {
		return verticalLayout != null;
	}

	@Override
	protected void internalDispose() {
		try {
			for (IEmbeddableEditpart child : new ArrayList<IEmbeddableEditpart>(
					getChildren())) {
				child.dispose();
			}
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	@Override
	public void doUnrender() {
		if (verticalLayout != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(verticalLayout);

			// unrender the children
			unrenderChildren();

			verticalLayout = null;
		}
	}

	@Override
	protected void internalAdd(IEmbeddableEditpart editpart) {
		YEmbeddable yChild = (YEmbeddable) editpart.getModel();
		addChildComponent(editpart, modelAccess.getCellStyle(yChild));
	}

	@Override
	protected void internalRemove(IEmbeddableEditpart child) {
		if (verticalLayout != null && child.isRendered()) {
			verticalLayout.removeComponent((Component) child.getWidget());
		}

		child.unrender();
	}

	@Override
	protected void internalInsert(IEmbeddableEditpart editpart, int index) {
		refreshUI();
	}

	@Override
	protected void internalMove(IEmbeddableEditpart editpart, int oldIndex,
			int newIndex) {
		refreshUI();
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
		for (IEmbeddableEditpart editpart : getChildren()) {
			if (editpart.isRendered()) {
				editpart.unrender();
			}
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YVerticalLayout yLayout;

		public ModelAccess(YVerticalLayout yLayout) {
			super();
			this.yLayout = yLayout;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YVerticalLayout#isSpacing()
		 */
		public boolean isSpacing() {
			return yLayout.isSpacing();
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YVerticalLayout#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YVerticalLayout#getCellStyles()
		 */
		public EList<YVerticalLayoutCellStyle> getCellStyles() {
			return yLayout.getCellStyles();
		}

		public YVerticalLayoutCellStyle getCellStyle(YEmbeddable element) {
			return yLayout.getCellStyle(element);
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

		protected boolean isExpandVertical() {
			switch (alignment) {
			case FILL_CENTER:
			case FILL_FILL:
			case FILL_LEFT:
			case FILL_RIGHT:
				return true;
			default:
				return false;
			}
		}

		protected boolean isExpandHorizontal() {
			switch (alignment) {
			case BOTTOM_FILL:
			case FILL_FILL:
			case MIDDLE_FILL:
			case TOP_FILL:
				return true;
			default:
				return false;
			}
		}
	}
}
