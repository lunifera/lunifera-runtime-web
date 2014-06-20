/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Console commands for the http bundle.
 */
public class ConsoleCommands implements CommandProvider {

	private final static String TAB = "\t"; //$NON-NLS-1$
	private final static String NEW_LINE = "\r\n"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConsoleCommands.class);

	private static final Set<Command> COMMANDS = new HashSet<ConsoleCommands.Command>();
	static {
		COMMANDS.add(new Command("<ls>", "",
				"Lists all registered http applications"));
		COMMANDS.add(new Command("<properties>", "",
				"Lists all available service properties"));
		COMMANDS.add(new Command("<start|stop>", "[application id]",
				"Starts or stops the http application with the given id"));
		COMMANDS.add(new Command("<dlt>", "[application id]",
				"Deletes the http application with the given id"));
	}

	private BundleContext bundleContext;
	private ConfigurationAdmin configAdmin;

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {
		this.bundleContext = context.getBundleContext();
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		bundleContext = null;
	}

	public void _lhttp(final CommandInterpreter ci) throws Exception {
		String argument = ci.nextArgument();
		if (argument == null) {
			ci.println(getHelp());
		} else if (argument.equals("ls")) {
			printApplication(ci);
		} else if (argument.equals("properties")) {
			printFilterProperties(ci);
		} else if (argument.equals("start")) {
			startApplication(ci);
		} else if (argument.equals("stop")) {
			stopApplication(ci);
		} else if (argument.equals("dlt")) {
			deleteApplication(ci);
		} else {
			ci.println("ERROR - not a valid command!");
			ci.println(getHelp());
		}
	}

	/**
	 * 
	 * @param ci
	 */
	private void printApplication(CommandInterpreter ci) {
		try {
			ci.println("\t---- Available http application instances ----");
			for (ServiceReference<IHttpApplication> reference : bundleContext
					.getServiceReferences(IHttpApplication.class, null)) {
				IHttpApplication service = bundleContext.getService(reference);
				printApplication(ci, service);
			}
		} catch (InvalidSyntaxException e) {
			LOGGER.error("{}", e);
		}
	}

	public void printApplication(CommandInterpreter ci, IHttpApplication service) {
		ci.println(String
				.format("\t id: %s \t name: %s \t context path: %s \t jetty server: %s \t started: %s \t pid: %s",
						service.getId(), service.getName(),
						service.getContextPath(), service.getJettyServer(),
						Boolean.toString(service.isStarted()),
						findHttpApplicationPID(service.getId())));
	}

	/**
	 * Prints the available OSGi properties to filter.
	 * 
	 * @param ci
	 */
	private void printFilterProperties(CommandInterpreter ci) {
		ci.println("\t---- Available OSGi properties ----");
		ci.println("\t" + HttpConstants.APPLICATION_ID);
		ci.println("\t" + HttpConstants.APPLICATION_NAME);
		ci.println("\t" + HttpConstants.CONTEXT_PATH);
	}

	private void stopApplication(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		HttpApplication application = (HttpApplication) findHttpApplication(id);
		if (application == null) {
			ci.println("\tERROR: Application not found!");
			return;
		}

		if (!application.isStarted()) {
			ci.println("\tApplication already stopped!");
		} else {
			application.stop();
			ci.println("\tApplication was stopped.");
		}
		printApplication(ci, application);

	}

	/**
	 * Deletes the application with the given id.
	 * 
	 * @param ci
	 */
	private void deleteApplication(CommandInterpreter ci) {

		if (configAdmin == null) {
			ci.println("\tERROR: ConfigAdmin not available. Start equinox.cm!");
			return;
		}

		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		HttpApplication application = (HttpApplication) findHttpApplication(id);
		if (application == null) {
			ci.println("\tERROR: Application not found!");
			return;
		}

		String pid = findHttpApplicationPID(id);
		Configuration config;
		try {
			config = configAdmin.getConfiguration(pid);
			if (config != null) {
				config.delete();
			}
		} catch (IOException e) {
			LOGGER.error("{}", e);
		}

		ci.println("\tHttpApplication successfully deleted!");
	}

	/**
	 * Looks for the application with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public IHttpApplication findHttpApplication(String id) {
		IHttpApplication application = null;
		try {
			Collection<ServiceReference<IHttpApplication>> refs = bundleContext
					.getServiceReferences(IHttpApplication.class,
							String.format("(%s=%s)",
									HttpConstants.APPLICATION_ID, id));
			if (refs.size() == 1) {
				application = bundleContext.getService(refs.iterator().next());
			}
		} catch (InvalidSyntaxException e) {
			LOGGER.error("{}", e);
		}
		return application;
	}

	/**
	 * Looks for the application pid with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public String findHttpApplicationPID(String id) {
		String pid = null;
		try {
			Collection<ServiceReference<IHttpApplication>> refs = bundleContext
					.getServiceReferences(IHttpApplication.class,
							String.format("(%s=%s)",
									HttpConstants.APPLICATION_ID, id));
			if (refs.size() == 1) {
				pid = (String) refs.iterator().next()
						.getProperty(Constants.SERVICE_PID);
			}
		} catch (InvalidSyntaxException e) {
			LOGGER.error("{}", e);
		}
		return pid;
	}

	private void startApplication(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		HttpApplication application = (HttpApplication) findHttpApplication(id);
		if (application == null) {
			ci.println("\tERROR: Application not found!");
			return;
		}

		if (application.isStarted()) {
			ci.println("\tApplication already started!");
		} else {
			application.start();
			ci.println("\tApplication was started.");
		}
		printApplication(ci, application);
	}

	@Override
	public String getHelp() {
		StringBuilder builder = new StringBuilder();
		builder.append("---- Lunifera http application commands ----");
		builder.append(NEW_LINE);

		builder.append(TAB);
		builder.append("lhttp <cmd> [args]");
		builder.append(NEW_LINE);

		for (Command command : COMMANDS) {
			command.writeTo(builder);
		}
		return builder.toString();
	}

	/**
	 * Called by OSGi DS
	 * 
	 * @param configAdmin
	 */
	protected void bindConfigAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	/**
	 * Called by OSGi DS
	 * 
	 * @param configAdmin
	 */
	protected void unbindConfigAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = null;
	}

	private static class Command {

		private String command;
		private String description;
		private String parameter;

		public Command(String command, String parameter, String description) {
			super();
			this.command = command;
			this.parameter = parameter;
			this.description = description;
		}

		/**
		 * Write the command to the given builder
		 * 
		 * @param builder
		 */
		public void writeTo(StringBuilder builder) {
			builder.append(TAB);
			builder.append(TAB);
			builder.append(command);
			builder.append(" ");
			builder.append(parameter);
			builder.append(" - ");
			builder.append(description);
			builder.append(NEW_LINE);
		}

	}

}
