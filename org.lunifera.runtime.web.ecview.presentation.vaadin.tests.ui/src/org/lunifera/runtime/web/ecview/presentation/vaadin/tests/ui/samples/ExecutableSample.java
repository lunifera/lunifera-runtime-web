package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YButton;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YVerticalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.listener.YButtonClickListener;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class ExecutableSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	private IViewContext context;

	private YTextField yText;

	public ExecutableSample() {
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

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			context = renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}
	}

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);
		yText = factory.createTextField();
		yText.setLabel("ExecType");
		row1.addElement(yText);

		YButton ySyncRunner = factory.createButton();
		ySyncRunner.setLabel("exec");
		row1.addElement(ySyncRunner);
		ySyncRunner.addClickListener(new YButtonClickListener() {
			@Override
			public void clicked(YButton yButton) {
				context.exec(new Runnable() {
					@Override
					public void run() {
						yText.setValue("exec");
					}
				});
			}
		});

		YButton yAsyncRunner = factory.createButton();
		yAsyncRunner.setLabel("asyncexec");
		row1.addElement(yAsyncRunner);
		yAsyncRunner.addClickListener(new YButtonClickListener() {
			@Override
			public void clicked(YButton yButton) {
				context.execAsync(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
						}
						yText.setValue("asyncexec");
					}
				});
			}
		});
	}
}
