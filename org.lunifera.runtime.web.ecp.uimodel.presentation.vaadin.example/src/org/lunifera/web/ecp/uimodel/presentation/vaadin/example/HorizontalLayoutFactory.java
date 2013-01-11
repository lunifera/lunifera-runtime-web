package org.lunifera.web.ecp.uimodel.presentation.vaadin.example;

import org.eclipse.emf.ecp.ui.model.core.datatypes.YDatadescription;
import org.eclipse.emf.ecp.ui.model.core.uimodel.YUiView;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiAlignment;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiCheckBox;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiComboBox;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiHorizontalLayout;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiHorizontalLayoutCellStyle;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiLabel;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiList;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiTable;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiTextArea;
import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiTextField;
import org.eclipse.emf.ecp.ui.model.core.uimodel.util.SimpleModelFactory;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.ContextException;
import org.lunifera.web.ecp.uimodel.presentation.vaadin.VaadinRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class HorizontalLayoutFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(UiModelDemoUI.class);

	private static final long serialVersionUID = 1L;

	private SimpleModelFactory factory = new SimpleModelFactory();

	public Component createComponent() {

		CssLayout renderingContent = new CssLayout();
		renderingContent.setSizeFull();

		// build the view model
		// ...> yView
		// ......> yLayout
		// .........> yText1
		// .........> yText2
		// .........> yText3
		// .........> yText4
		// .........> yText5
		// .........> yText6
		// .........> yText7
		// .........> yText8
		// .........> yText9
		// .........> yText10
		YUiView yView = factory.createView();
		yView.setCssClass("horizontalLayoutExample");

		// create the layout
		YUiHorizontalLayout yLayout = factory.createHorizontalLayout();
		yLayout.setCssClass("horizontalLayout");
		yView.setContent(yLayout);
		yLayout.setSpacing(true);
		yLayout.setMargin(true);

		// add label for textfields
		YUiLabel yLabel1 = newLabel("Textfields");
		yLayout.getElements().add(yLabel1);

		// add some text fields
		//
		YUiTextField yText1 = newText("Text1");
		yLayout.getElements().add(yText1);
		YUiTextField yText2 = newText("Text2");
		yLayout.getElements().add(yText2);
		YUiTextField yText3 = newText("Text3");
		yLayout.getElements().add(yText3);
		YUiTextField yText4 = newText("Text4");
		yLayout.getElements().add(yText4);
		YUiTextField yText5 = newText("Text5");
		yLayout.getElements().add(yText5);
		YUiTextField yText6 = newText("Text6");
		yLayout.getElements().add(yText6);
		YUiTextField yText7 = newText("Text7");
		yLayout.getElements().add(yText7);
		YUiTextField yText8 = newText("Text8");
		yLayout.getElements().add(yText8);
		YUiTextField yText9 = newText("Text9");
		yLayout.getElements().add(yText9);
		YUiTextField yText10 = newText("Text10");
		yLayout.getElements().add(yText10);

		// add label for textAreas
		YUiLabel yLabel2 = newLabel("TextAreas");
		yLayout.getElements().add(yLabel2);

		// add some text areas
		//
		YUiTextArea yTextArea1 = newTextArea("TextArea1");
		yLayout.getElements().add(yTextArea1);

		// add label for check boxes
		YUiLabel yLabel3 = newLabel("CheckBoxes");
		yLayout.getElements().add(yLabel3);

		// add some check boxes
		//
		YUiCheckBox yCheckBox1 = newCheckBox("CheckBox1");
		yLayout.getElements().add(yCheckBox1);
		
		// add label for combo boxes
		YUiLabel yLabel4 = newLabel("ComboBoxes");
		yLayout.getElements().add(yLabel4);
		
		// add some combo boxes
		//
		YUiComboBox yComboBox1 = newComboBox("ComboBox1");
		yLayout.getElements().add(yComboBox1);
		
		// add label for lists
		YUiLabel yLabel5 = newLabel("Lists");
		yLayout.getElements().add(yLabel5);
		
		// add some lists
		//
		YUiList yList1 = newList("List1");
		yLayout.getElements().add(yList1);
		
		// add label for tables
		YUiLabel yLabel6 = newLabel("Tables");
		yLayout.getElements().add(yLabel6);
		
		// add some tables
		//
		YUiTable ytable1 = newTable("Table1");
		yLayout.getElements().add(ytable1);

		// create the styling information
		//
		// // label 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel1 = createCellStyle(yLayout, yLabel1);
		yStyleLabel1.setAlignment(YUiAlignment.TOP_LEFT);
		//
		// text 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyle1 = createCellStyle(yLayout, yText1);
		yStyle1.setAlignment(YUiAlignment.TOP_LEFT);
		// text 2 -> alignment
		YUiHorizontalLayoutCellStyle yStyle2 = createCellStyle(yLayout, yText2);
		yStyle2.setAlignment(YUiAlignment.MIDDLE_CENTER);
		// text 3 -> alignment
		YUiHorizontalLayoutCellStyle yStyle3 = createCellStyle(yLayout, yText3);
		yStyle3.setAlignment(YUiAlignment.BOTTOM_RIGHT);
		// text 4 -> alignment
		YUiHorizontalLayoutCellStyle yStyle4 = createCellStyle(yLayout, yText4);
		yStyle4.setAlignment(YUiAlignment.FILL_LEFT);
		// text 5 -> alignment
		YUiHorizontalLayoutCellStyle yStyle5 = createCellStyle(yLayout, yText5);
		yStyle5.setAlignment(YUiAlignment.MIDDLE_FILL);
		// text 6 -> alignment
		YUiHorizontalLayoutCellStyle yStyle6 = createCellStyle(yLayout, yText6);
		yStyle6.setAlignment(YUiAlignment.MIDDLE_FILL);
		// text 7 -> alignment
		YUiHorizontalLayoutCellStyle yStyle7 = createCellStyle(yLayout, yText7);
		yStyle7.setAlignment(YUiAlignment.FILL_FILL);
		// text 8 -> alignment
		YUiHorizontalLayoutCellStyle yStyle8 = createCellStyle(yLayout, yText8);
		yStyle8.setAlignment(YUiAlignment.BOTTOM_LEFT);

		// label 2 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel2 = createCellStyle(yLayout, yLabel2);
		yStyleLabel2.setAlignment(YUiAlignment.BOTTOM_CENTER);
		// textArea 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleArea1 = createCellStyle(yLayout,
				yTextArea1);
		yStyleArea1.setAlignment(YUiAlignment.BOTTOM_LEFT);

		// label 3 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel3 = createCellStyle(yLayout, yLabel3);
		yStyleLabel3.setAlignment(YUiAlignment.BOTTOM_CENTER);
		// checkBox 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleChBox1 = createCellStyle(yLayout,
				yCheckBox1);
		yStyleChBox1.setAlignment(YUiAlignment.BOTTOM_LEFT);

		// label 4 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel4 = createCellStyle(yLayout, yLabel4);
		yStyleLabel4.setAlignment(YUiAlignment.BOTTOM_CENTER);
		// comboBox 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleComboBox1 = createCellStyle(yLayout,
				yComboBox1);
		yStyleComboBox1.setAlignment(YUiAlignment.BOTTOM_LEFT);
		
		// label 5 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel5 = createCellStyle(yLayout, yLabel5);
		yStyleLabel5.setAlignment(YUiAlignment.BOTTOM_CENTER);
		// list 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleList1 = createCellStyle(yLayout,
				yList1);
		yStyleList1.setAlignment(YUiAlignment.BOTTOM_LEFT);
		
		// label 6 -> alignment
		YUiHorizontalLayoutCellStyle yStyleLabel6 = createCellStyle(yLayout, yLabel6);
		yStyleLabel6.setAlignment(YUiAlignment.BOTTOM_CENTER);
		// comboBox 1 -> alignment
		YUiHorizontalLayoutCellStyle yStyleTable1 = createCellStyle(yLayout,
				ytable1);
		yStyleTable1.setAlignment(YUiAlignment.BOTTOM_LEFT);

		try {
			VaadinRenderer renderer = new VaadinRenderer();
			renderer.render(renderingContent, yView, null);
		} catch (ContextException e) {
			logger.error("{}", e);
		}

		return renderingContent;
	}

	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiLabel yLabel1) {
		return factory.createHorizontalLayoutCellStyle(yLabel1, yHorizontalLayout);
	}

	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiTextField yText1) {
		return factory.createHorizontalLayoutCellStyle(yText1, yHorizontalLayout);
	}

	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiTextArea yTextArea1) {
		return factory.createHorizontalLayoutCellStyle(yTextArea1, yHorizontalLayout);
	}
	
	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiCheckBox yChBox1) {
		return factory.createHorizontalLayoutCellStyle(yChBox1, yHorizontalLayout);
	}
	
	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiComboBox yComboBox1) {
		return factory.createHorizontalLayoutCellStyle(yComboBox1, yHorizontalLayout);
	}
	
	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiList yList1) {
		return factory.createHorizontalLayoutCellStyle(yList1, yHorizontalLayout);
	}
	
	protected YUiHorizontalLayoutCellStyle createCellStyle(YUiHorizontalLayout yHorizontalLayout,
			YUiTable yTable1) {
		return factory.createHorizontalLayoutCellStyle(yTable1, yHorizontalLayout);
	}

	/**
	 * Creates a new label.
	 * 
	 * @param label
	 *            the label to show
	 * @return label
	 */
	protected YUiLabel newLabel(String label) {
		YUiLabel field = factory.createLabel();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			field.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return field;
	}

	/**
	 * Creates a new text field.
	 * 
	 * @param label
	 *            the label to show
	 * @return textField
	 */
	protected YUiTextField newText(String label) {
		YUiTextField field = factory.createTextField();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			field.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return field;
	}

	/**
	 * Creates a new text area.
	 * 
	 * @param label
	 *            the label to show
	 * @return textArea
	 */
	protected YUiTextArea newTextArea(String label) {
		YUiTextArea area = factory.createTextArea();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			area.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return area;
	}

	/**
	 * Creates a new check box.
	 * 
	 * @param label
	 *            the label to show
	 * @return checkBox
	 */
	protected YUiCheckBox newCheckBox(String label) {
		YUiCheckBox chBox = factory.createCheckBox();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			chBox.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return chBox;
	}
	
	/**
	 * Creates a new combo box.
	 * 
	 * @param label
	 *            the label to show
	 * @return checkBox
	 */
	protected YUiComboBox newComboBox(String label) {
		YUiComboBox comboBox = factory.createComboBox();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			comboBox.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return comboBox;
	}
	
	/**
	 * Creates a new list.
	 * 
	 * @param label
	 *            the label to show
	 * @return list
	 */
	protected YUiList newList(String label) {
		YUiList list = factory.createList();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			list.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return list;
	}
	
	/**
	 * Creates a new table.
	 * 
	 * @param label
	 *            the label to show
	 * @return table
	 */
	protected YUiTable newTable(String label) {
		YUiTable table = factory.createTable();
		if (label != null) {
			YDatadescription dtd = factory.createDatadescription();
			table.setDatadescription(dtd);
			dtd.setLabel(label);
		}
		return table;
	}

}