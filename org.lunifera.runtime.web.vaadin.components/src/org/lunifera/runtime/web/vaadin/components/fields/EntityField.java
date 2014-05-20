package org.lunifera.runtime.web.vaadin.components.fields;

import org.lunifera.runtime.web.vaadin.components.fields.shared.EntityFieldState;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public class EntityField extends CustomField<String> {

	public static final String CLASSNAME = "entityfield";

	private long entityId;
	private String entityUUID;
	private String value;
	private String description;

	public EntityField() {
		setStyleName(CLASSNAME);
	}

	@Override
	protected Component initContent() {
		return null;
	}

	@Override
	public EntityFieldState getState() {
		return (EntityFieldState) super.getState();
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		setValue("huhu");
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityUUID
	 */
	public String getEntityUUID() {
		return entityUUID;
	}

	/**
	 * @param entityUUID
	 *            the entityUUID to set
	 */
	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}

}