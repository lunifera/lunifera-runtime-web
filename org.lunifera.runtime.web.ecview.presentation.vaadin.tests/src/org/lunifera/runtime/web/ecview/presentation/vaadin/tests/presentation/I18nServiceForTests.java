package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import java.util.Locale;

import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.common.dispose.AbstractDisposable;

/**
 * A test I18n service.
 */
public class I18nServiceForTests extends AbstractDisposable implements
		II18nService {

	public static final String KEY__NAME = "tests.name";
	public static final String KEY__AGE = "tests.age";
	public static final String KEY__VALUE = "tests.value";

	@Override
	public String getValue(String i18nKey, Locale locale) {

		if (locale == Locale.GERMAN) {
			return getGerman(i18nKey);
		} else if (locale == Locale.ENGLISH) {
			return getEnglish(i18nKey);
		}

		return getEnglish(i18nKey);
	}

	private String getEnglish(String i18nKey) {
		if (i18nKey.equals(KEY__NAME)) {
			return "Name";
		} else if (i18nKey.equals(KEY__AGE)) {
			return "Age";
		} else if (i18nKey.equals(KEY__VALUE)) {
			return "Value";
		} else
			return "";
	}

	private String getGerman(String i18nKey) {
		if (i18nKey.equals(KEY__NAME)) {
			return "Name";
		} else if (i18nKey.equals(KEY__AGE)) {
			return "Alter";
		} else if (i18nKey.equals(KEY__VALUE)) {
			return "Wert";
		} else
			return "";
	}

	@Override
	protected void internalDispose() {

	}

}
