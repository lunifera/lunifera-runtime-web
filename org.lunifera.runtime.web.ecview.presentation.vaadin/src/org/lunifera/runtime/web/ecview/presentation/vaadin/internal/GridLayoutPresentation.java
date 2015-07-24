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
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.YGridLayoutCellStyle;
import org.lunifera.ecview.core.extension.model.extension.YSpanInfo;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractLayoutPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.GridLayout.Area;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class GridLayoutPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GridLayoutPresentation.class);

	private GridLayout gridlayout;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public GridLayoutPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess((YGridLayout) editpart.getModel());
	}

	@Override
	public void add(IEmbeddableEditpart editpart) {
		super.add(editpart);

		refreshUI();
	}

	@Override
	public void remove(IEmbeddableEditpart editpart) {
		super.remove(editpart);

		refreshUI();
	}

	@Override
	public void insert(IEmbeddableEditpart editpart, int index) {
		super.insert(editpart, index);

		refreshUI();
	}

	@Override
	public void move(IEmbeddableEditpart editpart, int index) {
		super.move(editpart, index);

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
		if (!isRendered()) {
			return;
		}
		gridlayout.removeAllComponents();

		// create a map containing the style for the embeddable
		//
		Map<YEmbeddable, YGridLayoutCellStyle> yStyles = new HashMap<YEmbeddable, YGridLayoutCellStyle>();
		for (YGridLayoutCellStyle style : modelAccess.getCellStyles()) {
			if (yStyles.containsKey(style.getTarget())) {
				LOGGER.warn("Multiple style for element {}", style.getTarget());
			}
			yStyles.put(style.getTarget(), style);
		}

		// iterate all elements and build the child element
		//
		List<Cell> cells = new ArrayList<Cell>();
		for (IEmbeddableEditpart editPart : getEditpart().getElements()) {
			YEmbeddable yChild = (YEmbeddable) editPart.getModel();
			Cell cell = addChild(editPart, yStyles.get(yChild));
			cells.add(cell);
		}

		// Build a model of rows and columns.
		// Each coordinate (row/column) has an assigned cell. If a cell is
		// spanned, it will be assigned to many coordinates.
		List<Row> rows = new ArrayList<Row>();
		for (int i = 0; i < gridlayout.getRows(); i++) {
			rows.add(new Row(i, gridlayout.getColumns()));
		}
		List<Column> columns = new ArrayList<Column>();
		for (int i = 0; i < gridlayout.getColumns(); i++) {
			columns.add(new Column(i, gridlayout.getRows()));
		}

		for (Cell cell : cells) {
			for (int r = cell.area.getRow1(); r <= cell.area.getRow2(); r++) {
				for (int c = cell.area.getColumn1(); c <= cell.area
						.getColumn2(); c++) {
					Row row = rows.get(r);
					row.addCell(c, cell);
					Column col = columns.get(c);
					col.addCell(r, cell);
				}
			}
		}

		for (Row row : rows) {
			if (row.isShouldExpandVertical()) {
				gridlayout.setRowExpandRatio(row.getRowindex(), 1.0f);
			}
		}

		for (Column col : columns) {
			if (col.isShouldExpandHorizontal()) {
				gridlayout.setColumnExpandRatio(col.getColumnindex(), 1.0f);
			}
		}

		gridlayout.setSizeUndefined();
	}

	/**
	 * Is called to create the child component and apply layouting defaults to
	 * it.
	 * 
	 * @param editpart
	 * @param yStyle
	 * @return
	 */
	protected Cell addChild(IEmbeddableEditpart editpart,
			YGridLayoutCellStyle yStyle) {

		Component child = (Component) editpart.render(gridlayout);

		// calculate the spanning of the element
		// and adds the child to the grid layout
		//
		int col1 = -1;
		int row1 = -1;
		int col2 = -1;
		int row2 = -1;
		if (yStyle != null) {
			YSpanInfo ySpanInfo = yStyle.getSpanInfo();
			if (ySpanInfo != null) {
				col1 = ySpanInfo.getColumnFrom();
				row1 = ySpanInfo.getRowFrom();
				col2 = ySpanInfo.getColumnTo();
				row2 = ySpanInfo.getRowTo();
			}
		}

		// calculate and apply the alignment to be used
		//
		YAlignment yAlignment = yStyle != null && yStyle.getAlignment() != null ? yStyle
				.getAlignment() : YAlignment.TOP_LEFT;

		// add the element to the grid layout
		//
		if (col1 >= 0 && row1 >= 0 && (col1 < col2 || row1 < row2)) {
			if (gridlayout.getRows() < row2 + 1) {
				gridlayout.setRows(row2 + 1);
			}
			gridlayout.addComponent(child, col1, row1, col2, row2);
		} else if (col1 < 0 || row1 < 0) {
			gridlayout.addComponent(child);
		} else {
			gridlayout.addComponent(child);
			LOGGER.warn("Invalid span: col1 {}, row1 {}, col2 {}, row2{}",
					new Object[] { col1, row1, col2, row2 });
		}
		applyAlignment(child, yAlignment);

		GridLayout.Area area = gridlayout.getComponentArea(child);

		return new Cell(child, yAlignment, area);
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
				gridlayout
						.setComponentAlignment(child, Alignment.BOTTOM_CENTER);
				break;
			case BOTTOM_FILL:
				gridlayout.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				child.setWidth("100%");
				break;
			case BOTTOM_LEFT:
				gridlayout.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				break;
			case BOTTOM_RIGHT:
				gridlayout.setComponentAlignment(child, Alignment.BOTTOM_RIGHT);
				break;
			case MIDDLE_CENTER:
				gridlayout
						.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
				break;
			case MIDDLE_FILL:
				gridlayout.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				child.setWidth("100%");
				break;
			case MIDDLE_LEFT:
				gridlayout.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				break;
			case MIDDLE_RIGHT:
				gridlayout.setComponentAlignment(child, Alignment.MIDDLE_RIGHT);
				break;
			case TOP_CENTER:
				gridlayout.setComponentAlignment(child, Alignment.TOP_CENTER);
				break;
			case TOP_FILL:
				gridlayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				break;
			case TOP_LEFT:
				gridlayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				break;
			case TOP_RIGHT:
				gridlayout.setComponentAlignment(child, Alignment.TOP_RIGHT);
				break;
			case FILL_CENTER:
				gridlayout.setComponentAlignment(child, Alignment.TOP_CENTER);
				child.setHeight("100%");
				break;
			case FILL_FILL:
				gridlayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setSizeFull();
				// child.setHeight("100%");
				break;
			case FILL_LEFT:
				gridlayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setHeight("100%");
				break;
			case FILL_RIGHT:
				gridlayout.setComponentAlignment(child, Alignment.TOP_RIGHT);
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
		if (gridlayout == null) {

			gridlayout = new GridLayout(modelAccess.getColumns(), 1);
			gridlayout.setSpacing(false);
			gridlayout.setImmediate(true);
			setupComponent(gridlayout, getCastedModel());

			associateWidget(gridlayout, modelAccess.yLayout);

			if (modelAccess.isCssIdValid()) {
				gridlayout.setId(modelAccess.getCssID());
			} else {
				gridlayout.setId(getEditpart().getId());
			}

			if (modelAccess.isMargin()) {
				gridlayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				gridlayout.setMargin(true);
			}

			if (modelAccess.isSpacing()) {
				gridlayout.addStyleName(IConstants.CSS_CLASS_SPACING);
				gridlayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				gridlayout.addStyleName(modelAccess.getCssClass());
			} else {
				gridlayout.addStyleName(CSS_CLASS_CONTROL);
			}
			gridlayout.addStyleName(IConstants.CSS_CLASS_GRIDLAYOUT);

			// creates the binding for the field
			createBindings(modelAccess.yLayout, gridlayout, null);

			initializeChildren();

			// and now render children
			renderChildren(false);
		}

		return gridlayout;
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
		return gridlayout;
	}

	@Override
	public boolean isRendered() {
		return gridlayout != null;
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
		if (gridlayout != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) gridlayout
					.getParent());
			if (parent != null) {
				parent.removeComponent(gridlayout);
			}

			// remove assocations
			unassociateWidget(gridlayout);

			gridlayout = null;

			// unrender the childs
			for (IEmbeddableEditpart child : getChildren()) {
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
		private final YGridLayout yLayout;

		public ModelAccess(YGridLayout yLayout) {
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YGridLayout#isSpacing()
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YGridLayout#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YGridLayout#getColumns()
		 */
		public int getColumns() {
			int columns = yLayout.getColumns();
			return columns <= 0 ? 2 : columns;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YGridLayout#getCellStyles()
		 */
		public EList<YGridLayoutCellStyle> getCellStyles() {
			return yLayout.getCellStyles();
		}

	}

	private static class Row {
		private int rowindex;
		private List<Cell> cells;
		private boolean shouldExpandVertical;

		private Row(int rowindex, int columns) {
			this.rowindex = rowindex;
			cells = new ArrayList<Cell>(columns);
		}

		public void addCell(int column, Cell cell) {
			cells.add(column, cell);

			YAlignment alignment = cell.getAlignment();
			// if not already sure, that it should expand
			// try to find out
			if (!shouldExpandVertical) {
				// If the cell should FILL-vertical, then we test if the cell is
				// spanned.
				// --> If not spanned, then "shouldExpandVertical" is true.
				// --> Otherwise we test, if the cell is the most bottom cell.
				// ----> If not most bottom, then no span.
				// ----> Otherwise "shouldExpandVertical" is true.
				switch (alignment) {
				case FILL_LEFT:
				case FILL_CENTER:
				case FILL_RIGHT:
				case FILL_FILL:
					if (!cell.isSpanned()) {
						// if the cell is not spanned, then
						// "shouldExpandHorizontal" is true
						shouldExpandVertical = true;
					} else {
						if (cell.getArea().getRow2() == rowindex) {
							// if the cell is the most right one, then
							// "shouldExpandHorizontal" is true
							shouldExpandVertical = true;
						}
					}
					break;
				default:
					// nothing to do
					break;
				}
			}
		}

		/**
		 * @return the rowindex
		 */
		protected int getRowindex() {
			return rowindex;
		}

		/**
		 * @return the shouldExpandVertical
		 */
		protected boolean isShouldExpandVertical() {
			return shouldExpandVertical;
		}

	}

	private static class Column {
		private final int columnindex;
		private List<Cell> cells;
		private boolean shouldExpandHorizontal;

		private Column(int columnindex, int rows) {
			this.columnindex = columnindex;
			cells = new ArrayList<Cell>(rows);
		}

		public void addCell(int row, Cell cell) {
			try {
				cells.add(row, cell);
			} catch (Exception e) {
				System.out.println(e);
			}
			YAlignment alignment = cell.getAlignment();
			// if not already sure, that it should expand
			// try to find out
			if (!shouldExpandHorizontal) {
				// If the cell should FILL-horizontal, then we test if the cell
				// is spanned.
				// --> If not spanned, then "shouldExpandHorizontal" is true.
				// --> Otherwise we test, if the cell is the most right cell.
				// ----> If not most right, then no span.
				// ----> Otherwise "shouldExpandHorizontal" is true.
				switch (alignment) {
				case BOTTOM_FILL:
				case MIDDLE_FILL:
				case TOP_FILL:
				case FILL_FILL:
					if (!cell.isSpanned()) {
						// if the cell is not spanned, then
						// "shouldExpandHorizontal" is true
						shouldExpandHorizontal = true;
					} else {
						if (cell.getArea().getColumn2() == cells.size() - 1) {
							// if the cell is the most right one, then
							// "shouldExpandHorizontal" is true
							shouldExpandHorizontal = true;
						}
					}
					break;
				default:
					// nothing to do
					break;
				}
			}
		}

		/**
		 * @return the columnindex
		 */
		protected int getColumnindex() {
			return columnindex;
		}

		/**
		 * @return the shouldExpandHorizontal
		 */
		protected boolean isShouldExpandHorizontal() {
			return shouldExpandHorizontal;
		}

	}

	public static class Cell {
		private final Component component;
		private final YAlignment alignment;
		private final Area area;

		public Cell(Component component, YAlignment alignment,
				GridLayout.Area area) {
			super();
			this.component = component;
			this.alignment = alignment;
			this.area = area;
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

		/**
		 * @return the area
		 */
		protected Area getArea() {
			return area;
		}

		/**
		 * Returns true, if the cell is spanned.
		 * 
		 * @return
		 */
		public boolean isSpanned() {
			return area.getRow1() != area.getRow2()
					|| area.getColumn1() != area.getColumn2();
		}

	}
}
