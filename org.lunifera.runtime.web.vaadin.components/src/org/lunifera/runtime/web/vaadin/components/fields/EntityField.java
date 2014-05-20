package org.lunifera.runtime.web.vaadin.components.fields;

import org.lunifera.runtime.web.vaadin.components.fields.shared.EntityFieldState;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public class EntityField extends CustomField<String> {

	public static final String CLASSNAME = "entityfield";

	private Class<?> entityClass;
	private final boolean isUUID;

	public EntityField() {
		this(true);
	}

	public EntityField(boolean isUUID) {
		setStyleName(CLASSNAME);
		this.isUUID = isUUID;
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
		if (isUUID) {
			throw new IllegalStateException(
					"The entity field was defined as a UUID field. Can not be cast to long!");
		}
		try {
			return (Long) getState().entityId;
		} catch (Exception e) {
			throw new IllegalStateException(getState().entityId
					+ " can not be cast to Long!");
		}
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(long entityId) {
		getState().entityId = entityId;
	}

	/**
	 * @return the entityUUID
	 */
	public String getEntityUUID() {
		try {
			return (String) getState().entityId;
		} catch (Exception e) {
			throw new IllegalStateException(getState().entityId
					+ " can not be cast to String!");
		}
	}

	/**
	 * @param entityUUID
	 *            the entityUUID to set
	 */
	public void setEntityUUID(String entityUUID) {
		getState().entityId = entityUUID;
	}

	/**
	 * @return the entityClass
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass
	 *            the entityClass to set
	 */
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the searchEnabled
	 */
	public boolean isSearchEnabled() {
		return getState().searchEnabled;
	}

	/**
	 * @param searchEnabled
	 *            the searchEnabled to set
	 */
	public void setSearchEnabled(boolean searchEnabled) {
		getState().searchEnabled = searchEnabled;
	}

	/**
	 * Returns true, if the id of the field is an UUID.
	 * 
	 * @return the isUUID
	 */
	public boolean isUUID() {
		return isUUID;
	}

}