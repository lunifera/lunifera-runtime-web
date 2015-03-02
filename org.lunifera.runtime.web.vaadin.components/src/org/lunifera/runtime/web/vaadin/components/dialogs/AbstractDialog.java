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
package org.lunifera.runtime.web.vaadin.components.dialogs;

import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public abstract class AbstractDialog {

	protected final DialogConfig config;
	protected final Option[] options;

	protected Window window;
	protected VerticalLayout mainLayout;
	protected HorizontalLayout messageArea;
	protected Embedded messageIcon;
	protected Label messageText;
	protected HorizontalLayout optionsArea;

	/**
	 * Create resource for the given iconPath
	 * 
	 * @param iconPath
	 * @param resourceProvider
	 * @return
	 */
	protected static Resource createResource(String iconPath,
			IResourceProvider resourceProvider) {
		if (resourceProvider == null) {
			return null;
		}
		return resourceProvider.getResource(iconPath);
	}

	protected AbstractDialog(DialogConfig config, Option... options) {
		this.config = config;
		this.options = options;
	}

	@SuppressWarnings("serial")
	protected void open() {

		prepareLayout();

		configDialog();

		prepareOptions();

		window.addCloseListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				close();
			}
		});

		UI.getCurrent().addWindow(window);
	}

	/**
	 * Prepare the options.
	 */
	protected void prepareOptions() {
		if (options != null) {
			@SuppressWarnings("serial")
			Button.ClickListener listener = new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Button button = event.getButton();
					execute((Option) button.getData());
				}
			};

			for (Option option : options) {
				NativeButton button = new NativeButton(option.getName());
				optionsArea.addComponent(button);
				button.setIcon(option.getIcon());
				button.setDescription(option.getDescription());
				button.setData(option);
				button.addClickListener(listener);
			}
		}
	}

	/**
	 * Config the dialog.
	 */
	protected void configDialog() {
		if (config != null) {
			window.setCaption(config.getDialogName());
			window.setDescription(config.getDescription());
			messageIcon.setSource(config.getIcon());
			messageText.setValue(config.getMessage());

			// use callback
			config.config(window);
		}
	}

	/**
	 * Prepare the main layout.
	 */
	protected void prepareLayout() {
		window = new Window();
		window.setModal(true);
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		window.setContent(mainLayout);

		messageArea = new HorizontalLayout();
		messageArea.setSpacing(true);
		messageArea.setSizeFull();
		mainLayout.addComponent(messageArea);
		mainLayout.setExpandRatio(messageArea, 1.0f);

		messageIcon = new Embedded();
		messageArea.addComponent(messageIcon);

		messageText = new Label();
		messageText.setContentMode(ContentMode.TEXT);
		messageArea.addComponent(messageText);
		messageText.setSizeFull();
		messageArea.setExpandRatio(messageText, 1.0f);

		optionsArea = new HorizontalLayout();
		optionsArea.setSpacing(true);
		mainLayout.addComponent(optionsArea);
	}

	/**
	 * Close the window.
	 */
	protected void close() {
		if (window != null) {
			window.close();
			window = null;
		}
	}

	/**
	 * Execute the runnable.
	 * 
	 * @param option
	 */
	protected void execute(Option option) {
		if (option.getRunnable() != null) {
			option.getRunnable().run();
		}

		close();
	}

	/**
	 * Information how to config the dialog. Clients may override
	 * {@link #config(Window)} to get access about the window before opening. So
	 * clients may set size, position,...
	 */
	public static class DialogConfig {

		private final String dialogName;
		private final String message;
		private final String description;
		private final Resource icon;

		public DialogConfig(String dialogName, String message,
				String description, Resource icon) {
			super();
			this.dialogName = dialogName;
			this.message = message;
			this.description = description;
			this.icon = icon;
		}

		/**
		 * Clients may override to config the window before opening.
		 * 
		 * @param window
		 */
		public void config(Window window) {

		}

		/**
		 * @return the dialogName
		 */
		protected String getDialogName() {
			return dialogName;
		}

		/**
		 * @return the message
		 */
		protected String getMessage() {
			return message;
		}

		/**
		 * @return the description
		 */
		protected String getDescription() {
			return description;
		}

		/**
		 * @return the icon
		 */
		protected Resource getIcon() {
			return icon;
		}

	}

	/**
	 * Represents a single option.
	 */
	public static class Option {

		private String name;
		private String description;
		private Resource icon;
		private Runnable runnable;

		public Option(String name, String description, Resource icon,
				Runnable runnable) {
			super();
			this.name = name;
			this.description = description;
			this.icon = icon;
			this.runnable = runnable;
		}

		/**
		 * @return the name
		 */
		protected String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		protected void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the description
		 */
		protected String getDescription() {
			return description;
		}

		/**
		 * @param description
		 *            the description to set
		 */
		protected void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the icon
		 */
		protected Resource getIcon() {
			return icon;
		}

		/**
		 * @param icon
		 *            the icon to set
		 */
		protected void setIcon(Resource icon) {
			this.icon = icon;
		}

		/**
		 * @return the runnable
		 */
		protected Runnable getRunnable() {
			return runnable;
		}

		/**
		 * @param runnable
		 *            the runnable to set
		 */
		protected void setRunnable(Runnable runnable) {
			this.runnable = runnable;
		}

	}

}
