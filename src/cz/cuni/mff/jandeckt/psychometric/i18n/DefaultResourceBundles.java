package cz.cuni.mff.jandeckt.psychometric.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class DefaultResourceBundles implements ResourceBundles {

	public static final String COMMON = "Common"; //$NON-NLS-1$
	public static final String MENU = "Menu"; //$NON-NLS-1$
	public static final String ACTIONS = "Actions"; //$NON-NLS-1$
	public static final String ERRORS = "Errors"; //$NON-NLS-1$
	private Locale locale;
	private String prefix;
	
	public DefaultResourceBundles() {
		this.locale = Locale.getDefault();
	}
	
	public DefaultResourceBundles(Locale locale) {
		if(locale == null) {
			throw new IllegalArgumentException("Argument locale must not be null."); //$NON-NLS-1$
		}
		this.locale = locale;
	}
	
	@Override
	public ResourceBundle getResourceBundle(String identifier) {
		return ResourceBundle.getBundle(this.getFullyQualifiedName(identifier), this.locale);
	}

	@Override
	public ResourceBundle getResourceBundle(String identifier, Locale locale) {
		return ResourceBundle.getBundle(this.getFullyQualifiedName(identifier), locale);
	}
	
	private String getFullyQualifiedName(String identifier) {
		return this.getPrefix() + identifier;
	}

	private String getPrefix() {
		if(this.prefix == null)
			this.prefix = this.getClass().getPackage().getName() + '.';
		return this.prefix;
	}

}
