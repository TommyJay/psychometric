package cz.cuni.mff.jandeckt.psychometric.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ResourceBundles {
	ResourceBundle getResourceBundle(String identifier);
	ResourceBundle getResourceBundle(String identifier, Locale locale);
}
