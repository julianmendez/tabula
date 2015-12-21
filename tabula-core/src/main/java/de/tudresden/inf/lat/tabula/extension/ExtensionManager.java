package de.tudresden.inf.lat.tabula.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This models an extension that can execute other extensions.
 *
 */
public class ExtensionManager implements Extension {

	public static final String Name = "ext";
	public static final String Help = "extension manager";
	public static final int RequiredArguments = 1;
	public static final char NewLine = '\n';
	public static final char Space = ' ';

	private final ArrayList<Extension> extensions = new ArrayList<Extension>();
	private final TreeMap<String, Extension> extensionMap = new TreeMap<String, Extension>();

	/**
	 * Constructs an extension manager.
	 * 
	 * @param extensions
	 *            list of extensions
	 */
	public ExtensionManager(List<Extension> extensions) {
		if (extensions != null) {
			this.extensions.addAll(extensions);
			for (Extension extension : extensions) {
				String key = extension.getExtensionName();
				if (this.extensionMap.containsKey(key)) {
					throw new ExtensionException(
							"Only one implementation is allowed for each extension, and '"
									+ key + "' was at least twice.");
				}
				this.extensionMap.put(key, extension);
			}
		}
	}

	@Override
	public boolean process(List<String> arguments) {
		if (arguments == null || arguments.size() < RequiredArguments) {
			return false;
		} else {
			String command = arguments.get(0);
			ArrayList<String> newArguments = new ArrayList<String>();
			newArguments.addAll(arguments);
			newArguments.remove(0);
			Extension extension = this.extensionMap.get(command);
			if (extension == null) {
				throw new ExtensionException("Extension '" + command
						+ "' was not found.");
			} else {
				extension.process(newArguments);
				return true;
			}
		}
	}

	@Override
	public String getExtensionName() {
		return Name;
	}

	@Override
	public String getHelp() {
		StringBuffer sbuf = new StringBuffer();
		for (Extension extension : this.extensions) {
			sbuf.append(extension.getExtensionName());
			sbuf.append(Space);
			sbuf.append(extension.getHelp());
			sbuf.append(NewLine);
		}
		return sbuf.toString();
	}

	@Override
	public int getRequiredArguments() {
		return RequiredArguments;
	}

}