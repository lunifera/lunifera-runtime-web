package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.lib.StringExtensions;

public class OperationExtensions {

	/**
	 * Normalizes the method name.
	 * 
	 * @param simpleName
	 * @return
	 */
	public static String toPropertyName(String simpleName) {
		if (simpleName == null) {
			return null;
		}
		String tempName = null;
		if (isSetter(simpleName)) {
			tempName = StringExtensions.toFirstLower(simpleName.replaceFirst(
					"set", ""));
		} else if (isGetter(simpleName)) {
			if (simpleName.startsWith("get")) {
				tempName = StringExtensions.toFirstLower(simpleName
						.replaceFirst("get", ""));
			} else {
				tempName = StringExtensions.toFirstLower(simpleName
						.replaceFirst("is", ""));
			}
		}
		return tempName;
	}

	public static boolean isGetter(String simpleName) {
		if (simpleName == null) {
			return false;
		}
		return simpleName.startsWith("get") || simpleName.startsWith("is");
	}

	public static boolean isSetter(String simpleName) {
		return simpleName != null && simpleName.startsWith("set");
	}

	/**
	 * Calculates the operation infos for the given type.
	 * 
	 * @param type
	 * @return
	 */
	public static Map<String, OperationInfo> getOperationInfos(
			JvmDeclaredType type) {
		return getOperationInfos(type, null);
	}

	/**
	 * Calculates the operation infos for the given type.
	 * 
	 * @param type
	 * @param filterName
	 *            - is used to filter only methods property names matching the
	 *            filter name.
	 * @return
	 */
	public static Map<String, OperationInfo> getOperationInfos(
			JvmDeclaredType type, String filterName) {
		Map<String, OperationInfo> infos = new HashMap<String, OperationInfo>();
		for (JvmFeature feature : type.getAllFeatures()) {
			if (!(feature instanceof JvmOperation)) {
				continue;
			}

			JvmOperation operation = (JvmOperation) feature;
			if (operation.getVisibility() != JvmVisibility.PUBLIC) {
				continue;
			}
			
			if (!operation.getParameters().isEmpty()) {
				continue;
			}

			String propertyName = toPropertyName(operation.getSimpleName());
			if (filterName != null && !filterName.equals(propertyName)) {
				continue;
			}

			if (!isGetter(operation.getSimpleName())
					&& !isSetter(operation.getSimpleName())) {
				continue;
			}

			String id = calcId(operation.getDeclaringType(),
					operation.getSimpleName());
			if (!infos.containsKey(id)) {
				OperationInfo info = new OperationInfo();
				info.id = id;
				info.name = propertyName;
				infos.put(id, info);
			}

			OperationInfo info = infos.get(id);
			if (isGetter(operation.getSimpleName())) {
				info.getter = operation;
			} else {
				info.setter = operation;
			}
		}

		// apply readonly and create descriptions
		for (OperationInfo info : infos.values()) {
			if (info.getter == null) {
				continue;
			}
			if (info.setter == null) {
				info.readonly = true;
			}
		}
		return infos;
	}

	/**
	 * Normalizes the name.
	 * 
	 * @param declaringType
	 * @param simpleName
	 * @return
	 */
	public static String calcId(JvmDeclaredType declaringType, String simpleName) {
		String tempName = toPropertyName(simpleName);
		if (tempName == null) {
			return null;
		}

		return declaringType.getQualifiedName() + ":" + tempName;
	}

	public static class OperationInfo {

		private String id;
		private String name;
		private boolean readonly;
		private JvmOperation getter;
		private JvmOperation setter;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isReadonly() {
			return readonly;
		}

		public void setReadonly(boolean readonly) {
			this.readonly = readonly;
		}

		public JvmOperation getGetter() {
			return getter;
		}

		public void setGetter(JvmOperation getter) {
			this.getter = getter;
		}

		public JvmOperation getSetter() {
			return setter;
		}

		public void setSetter(JvmOperation setter) {
			this.setter = setter;
		}

	}

}
