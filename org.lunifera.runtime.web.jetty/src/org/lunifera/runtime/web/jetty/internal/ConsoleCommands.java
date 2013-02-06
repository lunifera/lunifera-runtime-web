package org.lunifera.runtime.web.jetty.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.lunifera.runtime.web.jetty.IJettyService;
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
		commands.add(new Command("<list>", "", "Lists all active jetty server"));
		commands.add(new Command("<properties>", "",
				"Lists all available service properties"));
		commands.add(new Command("<start|stop>", "[jetty id]",
				"Starts or stops the jetty with the given id"));
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

	public void _ljetty(final CommandInterpreter ci) throws Exception {
		String argument = ci.nextArgument();
		if (argument == null) {
			ci.println(getHelp());
		} else if (argument.equals("ls")) {
			printJetty(ci);
		} else if (argument.equals("properties")) {
			printFilterProperties(ci);
		} else if (argument.equals("start")) {
			startJetty(ci);
		} else if (argument.equals("stop")) {
			stopJetty(ci);
		} else {
			ci.println("ERROR - not a valid command!");
			ci.println(getHelp());
		}
	}

	/**
	 * 
	 * @param ci
	 */
	private void printJetty(CommandInterpreter ci) {
		try {
			ci.println("\t---- Available jetty instances ----");
			for (ServiceReference<IJettyService> reference : bundleContext
					.getServiceReferences(IJettyService.class, null)) {
				IJettyService service = bundleContext.getService(reference);
				printJetty(ci, service);
			}
		} catch (InvalidSyntaxException e) {
			logger.error("{}", e);
		}
	}

	public void printJetty(CommandInterpreter ci, IJettyService service) {
		ci.println(String.format(
				"\tid: %s \t name: %s \t port: %s \t started: %s",
				service.getId(), service.getName(),
				String.valueOf(service.getPort()),
				Boolean.toString(service.isStarted())));
	}

	/**
	 * Prints the available OSGi properties to filter.
	 * 
	 * @param ci
	 */
	private void printFilterProperties(CommandInterpreter ci) {
		ci.println("\t---- Available OSGi properties ----");
		ci.println("\t pid =" + IJettyService.OSGI__PID);
		ci.println("\t" + IJettyService.OSGI__ID);
		ci.println("\t" + IJettyService.OSGI__NAME);
		ci.println("\t" + IJettyService.OSGI__PORT);
	}

	private void stopJetty(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		IJettyService jetty = findJetty(id);
		if (jetty == null) {
			ci.println("\tERROR: Jetty not found!");
			return;
		}

		if (!jetty.isStarted()) {
			ci.println("\tJetty already stopped!");
		} else {
			jetty.stop();
			ci.println("\tJetty was stopped.");
		}
		printJetty(ci, jetty);

	}

	/**
	 * Looks for the jetty with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public IJettyService findJetty(String id) {
		IJettyService jetty = null;
		try {
			Collection<ServiceReference<IJettyService>> refs = bundleContext
					.getServiceReferences(IJettyService.class, String.format(
							"(%s=%s)", IJettyService.OSGI__ID, id));
			if (refs.size() == 1) {
				jetty = bundleContext.getService(refs.iterator().next());
			}
		} catch (InvalidSyntaxException e) {
			logger.error("{}", e);
		}
		return jetty;
	}

	private void startJetty(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id == null) {
			ci.println("\tERROR: No id specified!");
			return;
		}

		IJettyService jetty = findJetty(id);
		if (jetty == null) {
			ci.println("\tERROR: Jetty not found!");
			return;
		}

		if (jetty.isStarted()) {
			ci.println("\tJetty already started!");
		} else {
			jetty.start();
			ci.println("\tJetty was started.");
		}
		printJetty(ci, jetty);
	}

	@Override
	public String getHelp() {
		StringBuilder builder = new StringBuilder();
		builder.append("---- Lunifera jetty commands ----");
		builder.append(NEW_LINE);

		builder.append(TAB);
		builder.append("ljetty <cmd> [args]");
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
