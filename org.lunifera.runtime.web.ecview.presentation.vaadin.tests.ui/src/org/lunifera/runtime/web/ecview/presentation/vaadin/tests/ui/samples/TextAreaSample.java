package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YTextAreaDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextArea;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YVerticalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class TextAreaSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public TextAreaSample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {
		
		// TODO - fix me FP

		yView = factory.createView();
		yLayout = factory.createVerticalLayout();
		yView.setContent(yLayout);

		yBindingSet = yView.getOrCreateBindingSet();

		row1();

		row2();

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}
	}

	public void row2() {
		// test row 2
		YHorizontalLayout row2 = factory.createHorizontalLayout();
		yLayout.addElement(row2);

		YTextArea yText2_1 = factory.createTextArea();
		yText2_1.setLabel("minLength 3");
		row2.addElement(yText2_1);
		YTextAreaDatatype yDt2_2 = factory.createTextAreaDatatype();
		yDt2_2.setMinLength(3);
		yText2_1.setDatatype(yDt2_2);

		YTextArea yText2_2 = factory.createTextArea();
		yText2_2.setLabel("maxLength 10:");
		row2.addElement(yText2_2);
		YTextAreaDatatype yDt2_1 = factory.createTextAreaDatatype();
		yDt2_1.setMaxLength(10);
		yText2_2.setDatatype(yDt2_1);

		YTextArea yText2_3 = factory.createTextArea();
		yText2_3.setLabel("regexp: \\d+");
		row2.addElement(yText2_3);
		YTextAreaDatatype yDt2_3 = factory.createTextAreaDatatype();
		yDt2_3.setRegExpression("\\d+");
		yText2_3.setDatatype(yDt2_3);
	}

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);
		YTextArea yText1_1 = factory.createTextArea();
		yText1_1.setLabel("Field1");
		YTextArea yText1_2 = factory.createTextArea();
		yText1_2.setLabel("Field2");
		row1.addElement(yText1_1);
		row1.addElement(yText1_2);

		yBindingSet.addBinding(yText1_1.createValueEndpoint(),
				yText1_2.createValueEndpoint());
	}
}
