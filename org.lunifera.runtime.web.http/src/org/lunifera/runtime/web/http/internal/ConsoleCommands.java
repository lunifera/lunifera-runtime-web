package org.lunifera.runtime.web.http.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleCommands implements CommandProvider {

	private final static String TAB = "\t"; //$NON-NLS-1$
	private final static String NEW_LINE = "\r\n"; //$NON-NLS-1$

	private static final Logger logger = LoggerFactory
			.getLogger(ConsoleCommands.class);

	private static final Set<Command> commands = new HashSet<ConsoleCommands.Command>();
	static {
		commands.add(new Command("<ls>", "",
				"Lists all registered http applications"));
		commands.add(new Command("<properties>", "",
				"Lists all available service properties"));
		commands.add(new Command("<start|stop>", "[application id]",
				"Starts or stops the http application with the given id"));
	}

	private BundleContext bundleContext;

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
			logger.error("{}", e);
		}
	}

	public void printApplication(CommandInterpreter ci, IHttpApplication service) {
		ci.println(String.format(
				"\tid: %s \t name: %s \t context path: %s \t started: %s",
				service.getId(), service.getName(),
				String.valueOf(service.getContextPath()),
				Boolean.toString(service.isStarted())));
	}

	/**
	 * Prints the available OSGi properties to filter.
	 * 
	 * @param ci
	 */
	private void printFilterProperties(CommandInterpreter ci) {
		ci.println("\t---- Available OSGi properties ----");
		ci.println("\tpid = " + IHttpApplication.OSGI__PID);
		ci.println("\t" + IHttpApplication.OSGI__ID);
		ci.println("\t" + IHttpApplication.OSGI__NAME);
		ci.println("\t" + IHttpApplication.OSGI__CONTEXT_PATH);
	}

	private void stopApplication(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		IHttpApplication application = findHttpApplication(id);
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
	 * Looks for the application with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public IHttpApplication findHttpApplication(String id) {
		IHttpApplication application = null;
		try {
			Collection<ServiceReference<IHttpApplication>> refs = bundleContext
					.getServiceReferences(IHttpApplication.class, String
							.format("(%s=%s)", IHttpApplication.OSGI__ID, id));
			if (refs.size() == 1) {
				application = bundleContext.getService(refs.iterator().next());
			}
		} catch (InvalidSyntaxException e) {
			logger.error("{}", e);
		}
		return application;
	}

	private void startApplication(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		IHttpApplication application = findHttpApplication(id);
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

		for (Command command : commands) {
			command.writeTo(builder);
		}
		return builder.toString();
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
