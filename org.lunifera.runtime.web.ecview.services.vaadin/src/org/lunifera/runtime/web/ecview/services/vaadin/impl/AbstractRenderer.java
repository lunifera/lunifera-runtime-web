package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.eclipse.emf.ecp.ecview.common.model.core.YLayout;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class AbstractRenderer {

	protected static final Set<String> numericTypes = new HashSet<String>();
	static {
		numericTypes.add(Byte.class.getName());
		numericTypes.add(Long.class.getName());
		numericTypes.add(Short.class.getName());
		numericTypes.add(Integer.class.getName());

		numericTypes.add("byte");
		numericTypes.add("long");
		numericTypes.add("short");
		numericTypes.add("int");
	}

	protected static final Set<String> decimalTypes = new HashSet<String>();
	static {
		decimalTypes.add(Double.class.getName());
		decimalTypes.add(Float.class.getName());

		numericTypes.add("double");
		numericTypes.add("float");
	}

	protected Stack<Context> contexts = new Stack<Context>();
	protected Bundle modelBundle;

	public void init(YLayout yLayout, Class<?> clazz) {
		modelBundle = FrameworkUtil.getBundle(clazz);
		contexts.push(new Context(null, null, yLayout));
	}

	/**
	 * Returns the currently active layout.
	 * 
	 * @return
	 */
	public YLayout getLayout() {
		return contexts.peek().getLayout();
	}

	/**
	 * Returns the binding path under respect of nested attributes.
	 * 
	 * @return
	 */
	public String getBindingPath(String attribute) {
		return contexts.peek().getBindingPathFor(attribute);
	}

	public static boolean isString(LDataType object) {
		String fqn = object.getJvmTypeReference().getQualifiedName();
		return "java.lang.String".equals(fqn);
	}

	public static boolean isBoolean(LDataType object) {
		String name = object.getJvmTypeReference().getQualifiedName();
		return "java.lang.Boolean".equals(name);
	}

	public static boolean isNumeric(LDataType object) {
		String name = object.getJvmTypeReference().getQualifiedName();
		return numericTypes.contains(name);
	}

	public static boolean isDecimal(LDataType object) {
		String name = object.getJvmTypeReference().getQualifiedName();
		return decimalTypes.contains(name);
	}

	protected static class Context {
		protected final Context parent;
		protected final String bindingAttribute;
		protected final YLayout layout;

		public Context(Context parent, String bindingAttribute, YLayout layout) {
			super();
			this.parent = parent;
			this.bindingAttribute = bindingAttribute;
			this.layout = layout;
		}

		/**
		 * @return the parentLayout
		 */
		public YLayout getLayout() {
			if (layout != null) {
				return layout;
			}

			if (parent != null) {
				return parent.getLayout();
			}

			throw new IllegalStateException("No layout found");
		}

		/**
		 * 
		 * @return
		 */
		public String getBindingPath() {
			String path = "";
			if (parent != null) {
				path = parent.getBindingPath();
			}

			if (bindingAttribute != null) {
				if (path != null && !path.equals("")) {
					if (!bindingAttribute.equals("")) {
						// attache the binding attribute separated by a "."
						path = path + "." + bindingAttribute;
					}
				} else {
					path = bindingAttribute;
				}
			}

			return path;
		}

		/**
		 * 
		 * @return
		 */
		public String getBindingPathFor(String attribute) {
			String path = getBindingPath();
			if (path != null && !path.equals("")) {
				path = path + "." + attribute;
			} else {
				path = attribute;
			}
			return path;
		}

	}
}
