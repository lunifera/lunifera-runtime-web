package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YBeanSlot;
import org.eclipse.emf.ecp.ecview.common.model.core.YBeanSlotBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.model.core.YLayout;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.ExtDatatypesFactory;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDateTimeDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDateTimeFormat;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDecimalDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YNumericDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YTextDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YCheckBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDateTime;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YNumericField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LScalarType;
import org.lunifera.dsl.semantic.common.types.util.LunTypesSwitch;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LDtoAttribute;
import org.lunifera.dsl.semantic.dto.LDtoInheritedAttribute;
import org.lunifera.dsl.semantic.dto.util.LunDtoSwitch;

public class CopyOfDtoModelRenderer extends LunDtoSwitch<Boolean> {

	private final YLayout yLayout;
	private YLayout yCurrent;

	public CopyOfDtoModelRenderer(YLayout yLayout) {
		super();
		this.yLayout = yLayout;
		yCurrent = yLayout;
	}

	@Override
	public Boolean caseLDtoInheritedAttribute(LDtoInheritedAttribute object) {

		LScalarType lType = object.getInheritedType();
		LAttribute inheritedAttribute = object.getInheritedFeature();
		YEmbeddable yEmbeddable = new FieldProcessor(
				object.getInheritedFeature(), yLayout.getView())
				.doSwitch(lType);
		yEmbeddable.setLabel(inheritedAttribute.getName());
		yCurrent.getElements().add(yEmbeddable);

		return Boolean.TRUE;
	}

	@Override
	public Boolean caseLDtoAttribute(LDtoAttribute object) {
		YEmbeddable yEmbeddable = new FieldProcessor(object, yLayout.getView())
				.doSwitch(object.getType());
		yEmbeddable.setLabel(object.getName());
		yCurrent.getElements().add(yEmbeddable);

		return Boolean.TRUE;
	}

	@Override
	public Boolean caseLDto(LDto lDto) {

		for (LFeature lFeature : lDto.getAllFeatures()) {
			doSwitch(lFeature);
		}

		return Boolean.TRUE;
	}

	/**
	 * Is responsible to create fields based on the datatype.
	 */
	protected static class FieldProcessor extends LunTypesSwitch<YEmbeddable> {

		private static final Set<String> numericTypes = new HashSet<String>();
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

		private static final Set<String> decimalTypes = new HashSet<String>();
		static {
			decimalTypes.add(Double.class.getName());
			decimalTypes.add(Float.class.getName());

			numericTypes.add("double");
			numericTypes.add("float");
		}

		private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
		private final LAttribute lAttribute;
		private final YView yView;

		public FieldProcessor(LAttribute lAttribute, YView yView) {
			this.lAttribute = lAttribute;
			this.yView = yView;
		}

		@Override
		public YEmbeddable caseLScalarType(LScalarType object) {
			return super.caseLScalarType(object);
		}

		@Override
		public YEmbeddable caseLDataType(LDataType object) {
			YEmbeddable result = null;
			if (object.isDate()) {
				result = createDateField(object);
			} else if (isString(object)) {
				result = createTextField(object);
			} else if (isNumeric(object)) {
				result = createNumericField(object);
			} else if (isDecimal(object)) {
				result = createDecimalField(object);
			} else if (isBoolean(object)) {
				result = createCheckbox(object);
			}

			return result;
		}

		/**
		 * Creates the date field.
		 * 
		 * @param object
		 * @return
		 */
		public YDateTime createDateField(LDataType object) {
			YDateTime yDate = factory.createDateTime();

			// create the datatype
			YDateTimeDatatype yDt = ExtDatatypesFactory.eINSTANCE
					.createYDateTimeDatatype();
			yDate.setDatatype(yDt);
			switch (object.getDateType()) {
			case DATE:
				yDt.setFormat(YDateTimeFormat.DATE);
				break;
			case TIME:
				yDt.setFormat(YDateTimeFormat.TIME);
				break;
			case TIMESTAMP:
				yDt.setFormat(YDateTimeFormat.DATE_TIME);
				break;
			}

			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yView.getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yView
					.getBeanSlot(IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(lAttribute.getName());
			yBindingSet.addBinding(yDate.createValueEndpoint(),
					yContextBindingEndpoint);

			return yDate;
		}

		/**
		 * Creates the text field.
		 * 
		 * @param object
		 * @return
		 */
		public YTextField createTextField(LDataType object) {
			YTextField yText = factory.createTextField();

			// create the datatype
			YTextDatatype yDt = ExtDatatypesFactory.eINSTANCE
					.createYTextDatatype();
			yText.setDatatype(yDt);
			yDt.setMinLength(3);
			yDt.setMaxLength(20);

			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yView.getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yView
					.getBeanSlot(IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(lAttribute.getName());
			yBindingSet.addBinding(yText.createValueEndpoint(),
					yContextBindingEndpoint);

			return yText;
		}

		/**
		 * Creates the numeric field.
		 * 
		 * @param object
		 * @return
		 */
		public YNumericField createNumericField(LDataType object) {
			YNumericField yNumeric = factory.createNumericField();

			// create the datatype
			YNumericDatatype yDt = ExtDatatypesFactory.eINSTANCE
					.createYNumericDatatype();
			yNumeric.setDatatype(yDt);

			yDt.setGrouping(true);
			yDt.setMarkNegative(true);

			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yView.getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yView
					.getBeanSlot(IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(lAttribute.getName());
			yBindingSet.addBinding(yNumeric.createValueEndpoint(),
					yContextBindingEndpoint);

			return yNumeric;
		}

		/**
		 * Creates the decimal field.
		 * 
		 * @param object
		 * @return
		 */
		public YDecimalField createDecimalField(LDataType object) {
			YDecimalField yDecimal = factory.createDecimalField();

			// create the datatype
			YDecimalDatatype yDt = ExtDatatypesFactory.eINSTANCE
					.createYDecimalDatatype();
			yDecimal.setDatatype(yDt);

			yDt.setGrouping(true);
			yDt.setMarkNegative(true);
			yDt.setPrecision(2);

			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yView.getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yView
					.getBeanSlot(IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(lAttribute.getName());
			yBindingSet.addBinding(yDecimal.createValueEndpoint(),
					yContextBindingEndpoint);

			return yDecimal;
		}

		/**
		 * Creates the decimal field.
		 * 
		 * @param object
		 * @return
		 */
		public YCheckBox createCheckbox(LDataType object) {
			YCheckBox yCheckbox = factory.createCheckBox();

			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yView.getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yView
					.getBeanSlot(IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(lAttribute.getName());
			yBindingSet.addBinding(yCheckbox.createValueEndpoint(),
					yContextBindingEndpoint);

			return yCheckbox;
		}

		private boolean isString(LDataType object) {
			String fqn = object.getJvmTypeReference().getQualifiedName();
			return "java.lang.String".equals(fqn);
		}

		private boolean isBoolean(LDataType object) {
			String name = object.getJvmTypeReference().getQualifiedName();
			return "java.lang.Boolean".equals(name);
		}

		private boolean isNumeric(LDataType object) {
			String name = object.getJvmTypeReference().getQualifiedName();
			return numericTypes.contains(name);
		}

		private boolean isDecimal(LDataType object) {
			String name = object.getJvmTypeReference().getQualifiedName();
			return decimalTypes.contains(name);
		}

	}
}
