package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.binding.BindingFactory;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.binding.YEnumListBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YBeanSlot;
import org.eclipse.emf.ecp.ecview.common.model.core.YBeanSlotBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.model.core.YLayout;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.ExtDatatypesFactory;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDateTimeDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDateTimeFormat;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDecimalDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YNumericDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YTextDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YCheckBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YComboBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDateTime;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YNumericField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YOptionsGroup;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YVerticalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LPackage;
import org.lunifera.dsl.semantic.common.types.LScalarType;
import org.lunifera.dsl.semantic.common.types.util.LunTypesSwitch;
import org.lunifera.dsl.semantic.entity.LBean;
import org.lunifera.dsl.semantic.entity.LBeanAttribute;
import org.lunifera.dsl.semantic.entity.LBeanFeature;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityAttribute;
import org.lunifera.dsl.semantic.entity.LEntityFeature;
import org.lunifera.dsl.semantic.entity.util.LunEntitySwitch;

public class EntityModelRenderer extends AbstractRenderer {

	public EntityModelRenderer() {
		super();
	}

	public YEmbeddable render(LEntity lEntity, YLayout yLayout,
			Class<?> entityClass) {
		super.init(yLayout, entityClass);
		return new EntityTypesProcessor(null).doSwitch(lEntity);
	}

	/**
	 * Is responsible to create fields based on the datatype.
	 */
	protected class CommonTypesProcessor extends LunTypesSwitch<YEmbeddable> {

		private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
		private final LAttribute lAttribute;

		public CommonTypesProcessor(LAttribute lAttribute) {
			this.lAttribute = lAttribute;
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

		@Override
		public YEmbeddable caseLScalarType(LScalarType object) {
			// in case of scalar type, delegate to the specific entity datatype
			// processor
			return new EntityTypesProcessor(lAttribute).doSwitch(object);
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

			YLayout yLayout = getLayout();
			// create the binding -> bind from root slot to the attribute
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yLayout.getView().getBeanSlot(
					IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(getBindingPath(lAttribute.getName()));
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
			YLayout yLayout = getLayout();
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yLayout.getView().getBeanSlot(
					IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(getBindingPath(lAttribute.getName()));
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
			YLayout yLayout = getLayout();
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yLayout.getView().getBeanSlot(
					IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(getBindingPath(lAttribute.getName()));
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
			YLayout yLayout = getLayout();
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yLayout.getView().getBeanSlot(
					IViewContext.ROOTBEAN_SELECTOR);
			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(getBindingPath(lAttribute.getName()));
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
			YLayout yLayout = getLayout();
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YBeanSlot yBeanSlot = yLayout.getView().getBeanSlot(
					IViewContext.ROOTBEAN_SELECTOR);

			YBeanSlotBindingEndpoint yContextBindingEndpoint = yBeanSlot
					.createBindingEndpoint(getBindingPath(lAttribute.getName()));
			yBindingSet.addBinding(yCheckbox.createValueEndpoint(),
					yContextBindingEndpoint);

			return yCheckbox;
		}

		@Override
		public YEmbeddable caseLEnum(LEnum object) {
			LPackage lPkg = (LPackage) object.eContainer();
			String enumClassName = lPkg.getName() + "." + object.getName();
			Class<?> enumClass = null;
			try {
				enumClass = modelBundle.loadClass(enumClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			YLayout yLayout = getLayout();
			YBindingSet yBindingSet = yLayout.getView().getOrCreateBindingSet();
			YEnumListBindingEndpoint enumValuesEndpoint = BindingFactory.eINSTANCE
					.createYEnumListBindingEndpoint();
			enumValuesEndpoint.setEnum(enumClass);

			YEmbeddable result = null;
			if (enumClass.getEnumConstants().length > 3) {
				YComboBox yCombo = factory.createComboBox();
				yCombo.setType(enumClass);
				result = yCombo;
				// create the binding -> bind from root slot to the attribute

				yBindingSet.addBinding(yCombo.createCollectionEndpoint(),
						enumValuesEndpoint);
			} else {
				YOptionsGroup yOptions = factory.createOptionsGroup();
				yOptions.setType(enumClass);
				result = yOptions;
				// create the binding -> bind from root slot to the attribute

				yBindingSet.addBinding(yOptions.createCollectionEndpoint(),
						enumValuesEndpoint);

			}

			return result;
		}

	}

	/**
	 * Is responsible to create fields based on the datatype.
	 */
	protected class EntityTypesProcessor extends LunEntitySwitch<YEmbeddable> {
		private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
		private final LAttribute lAttribute;

		public EntityTypesProcessor(LAttribute lAttribute) {
			this.lAttribute = lAttribute;
		}

		@Override
		public YEmbeddable caseLEntityAttribute(LEntityAttribute object) {

			YEmbeddable yEmbeddable = null;
			LScalarType type = object.getType();
			if (type instanceof LBean) {

				// put a new context on the stack
				contexts.push(new Context(contexts.peek(), object.getName(),
						null));

				yEmbeddable = doSwitch(object.getType());

				contexts.pop();

			} else {
				yEmbeddable = new CommonTypesProcessor(object).doSwitch(object
						.getType());
			}
			if (yEmbeddable != null) {
				yEmbeddable.setLabel(object.getName());
			}

			return yEmbeddable;
		}

		@Override
		public YEmbeddable caseLEntity(LEntity lEntity) {
			YLayout yLayout = getLayout();
			for (LEntityFeature lFeature : lEntity.getAllFeatures()) {
				YEmbeddable result = doSwitch(lFeature);
				if (result != null) {
					yLayout.addElement(result);
				}
			}
			return yLayout;
		}

		@Override
		public YEmbeddable caseLBean(LBean lBean) {
			YVerticalLayout beanLayout = factory.createVerticalLayout();
			beanLayout.setLabel(contexts.peek().bindingAttribute);

			YGridLayout layout = (YGridLayout) getLayout();
			layout.addElement(beanLayout);
			// YGridLayoutCellStyle yStyle = layout
			// .addGridLayoutCellStyle(beanLayout);

			// if (layout.getElements().size() % layout.getColumns() == 0) {
			// int row = (layout.getElements().size() / layout.getColumns());
			// yStyle.addSpanInfo(0, row, 1, row);
			// } else {
			// int row = (layout.getElements().size() / layout.getColumns()) +
			// 1;
			// yStyle.addSpanInfo(0, row, 1, row);
			// }

			// put a new context on the stack
			contexts.push(new Context(contexts.peek(), null, beanLayout));

			for (LBeanFeature lFeature : lBean.getAllFeatures()) {
				YEmbeddable element = doSwitch(lFeature);
				if (element != null) {
					beanLayout.addElement(element);
				}
			}

			// remove the last context from the stack
			contexts.pop();

			return beanLayout;
		}

		@Override
		public YEmbeddable caseLBeanAttribute(LBeanAttribute object) {
			YEmbeddable yEmbeddable = new CommonTypesProcessor(object)
					.doSwitch(object.getType());
			yEmbeddable.setLabel(object.getName());
			return yEmbeddable;
		}
	}

}
