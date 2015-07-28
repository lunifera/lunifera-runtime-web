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

package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples;

import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.model.binding.BindingFactory;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.binding.YDetailValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.binding.YEnumListBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.extension.model.extension.YCheckBox;
import org.lunifera.ecview.core.extension.model.extension.YComboBox;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.YNumericField;
import org.lunifera.ecview.core.extension.model.extension.YTable;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class MasterDetailSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public MasterDetailSample() {
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

		YVerticalLayout row1 = factory.createVerticalLayout();
		yLayout.addElement(row1);

		YTable yTable1_1 = factory.createTable();
		yTable1_1.setLabel("Master");
		yTable1_1.setType(Person.class);
		row1.addElement(yTable1_1);

		yTable1_1.getCollection().add(
				new Person("Sabrina", 21, "1220", "Am Nordseeteich", false,
						Gender.FEMALE));
		yTable1_1.getCollection().add(
				new Person("Klemens", 36, "1220", "Irgendwo hinterm Ort", true,
						Gender.MALE));
		yTable1_1.getCollection().add(
				new Person("Flo", 35, "2301", "Hinterm Mond", false,
						Gender.MALE));

		YGridLayout yGrid = factory.createGridLayout();
		yLayout.addElement(yGrid);

		YTextField name = factory.createTextField();
		name.setLabel("Name");
		yGrid.addElement(name);

		YNumericField age = factory.createNumericField();
		age.setLabel("Age");
		yGrid.addElement(age);

		YTextField postalcode = factory.createTextField();
		postalcode.setLabel("Postalcode");
		yGrid.addElement(postalcode);

		YTextField street = factory.createTextField();
		street.setLabel("Street");
		yGrid.addElement(street);

		YCheckBox hasChild = factory.createCheckBox();
		hasChild.setLabel("Has child");
		yGrid.addElement(hasChild);

		YComboBox gender = factory.createComboBox();
		gender.setLabel("Gender");
		gender.setType(Gender.class);
		yGrid.addElement(gender);

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}

		yBindingSet.addBinding(name.createValueEndpoint(),
				selectionEndpoint(yTable1_1, "name"));
		yBindingSet.addBinding(age.createValueEndpoint(),
				selectionEndpoint(yTable1_1, "age"));
		yBindingSet.addBinding(postalcode.createValueEndpoint(),
				selectionEndpoint(yTable1_1, "postalcode"));
		yBindingSet.addBinding(street.createValueEndpoint(),
				selectionEndpoint(yTable1_1, "street"));
		yBindingSet.addBinding(hasChild.createValueEndpoint(),
				selectionEndpoint(yTable1_1, "hasChild"));
		yBindingSet.addBinding(gender.createSelectionEndpoint(),
				selectionEndpoint(yTable1_1, "gender"));
		YEnumListBindingEndpoint enumBinding = BindingFactory.eINSTANCE
				.createYEnumListBindingEndpoint();
		enumBinding.setEnum(Gender.class);
		yBindingSet.addBinding(gender.createCollectionEndpoint(), enumBinding);
	}

	protected YDetailValueBindingEndpoint selectionEndpoint(YTable yTable1_1,
			String path) {
		YEmbeddableSelectionEndpoint result = yTable1_1
				.createSelectionEndpoint();
		YDetailValueBindingEndpoint detailBinding = BindingFactory.eINSTANCE
				.createYDetailValueBindingEndpoint();
		detailBinding.setMasterObservable(result);
		detailBinding.setPropertyPath(path);
		detailBinding.setType(Person.class);

		return detailBinding;
	}

	public static class Person extends AbstractBean {

		private String name;
		private int age;
		private String postalcode;
		private String street;
		private boolean hasChild;
		private Gender gender;

		public Person(String name, int age, String postalcode, String street,
				boolean hasChild, Gender gender) {
			super();
			this.name = name;
			this.age = age;
			this.postalcode = postalcode;
			this.street = street;
			this.hasChild = hasChild;
			this.gender = gender;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			firePropertyChanged("name", this.name, this.name = name);
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			firePropertyChanged("age", this.age, this.age = age);
		}

		public String getPostalcode() {
			return postalcode;
		}

		public void setPostalcode(String postalcode) {
			firePropertyChanged("postalcode", this.postalcode,
					this.postalcode = postalcode);
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			firePropertyChanged("street", this.street, this.street = street);
		}

		public boolean isHasChild() {
			return hasChild;
		}

		public void setHasChild(boolean hasChild) {
			firePropertyChanged("hasChild", this.hasChild,
					this.hasChild = hasChild);
		}

		public Gender getGender() {
			return gender;
		}

		public void setGender(Gender gender) {
			firePropertyChanged("gender", this.gender, this.gender = gender);
		}
	}

	public static enum Gender {
		MALE, FEMALE
	}
}
