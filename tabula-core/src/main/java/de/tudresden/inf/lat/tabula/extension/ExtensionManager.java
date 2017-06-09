package de.tudresden.inf.lat.tabula.extension;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.common.OptMap;
import de.tudresden.inf.lat.tabula.common.OptMapImpl;
import de.tudresden.inf.lat.tabula.datatype.ParseException;

/**
 * This models an extension that can execute other extensions.
 *
 */
public class ExtensionManager implements Extension {

	public static final String NAME = "ext";
	public static final String HELP = "extension manager";
	public static final int REQUIRED_ARGUMENTS = 1;
	public static final char NEW_LINE = '\n';
	public static final char SPACE = ' ';

	private final List<Extension> extensions = new ArrayList<>();
	private final OptMap<String, Extension> extensionMap = new OptMapImpl<>(new TreeMap<>());

	/**
	 * Constructs an extension manager.
	 * 
	 * @param extensions
	 *            list of extensions
	 */
	public ExtensionManager(List<Extension> extensions) {
		if (Objects.nonNull(extensions)) {
			this.extensions.addAll(extensions);
			extensions.forEach(extension -> {
				String key = extension.getExtensionName();
				if (this.extensionMap.isKeyContained(key)) {
					throw new ExtensionException("Only one implementation is allowed for each extension, and '" + key
							+ "' was at least twice.");
				}
				this.extensionMap.putOpt(key, extension);
			});
		}
	}

	@Override
	public boolean process(List<String> arguments) {
		Objects.requireNonNull(arguments);
		if (arguments.size() < REQUIRED_ARGUMENTS) {
			throw new ExtensionException("No extension name was given.");
		} else {
			String command = arguments.get(0);
			List<String> newArguments = new ArrayList<>();
			newArguments.addAll(arguments);
			newArguments.remove(0);
			Optional<Extension> optExtension = this.extensionMap.getOpt(command);
			if (!optExtension.isPresent()) {
				throw new ExtensionException("Extension '" + command + "' was not found.");
			} else if (newArguments.size() < optExtension.get().getRequiredArguments()) {
				throw new ExtensionException("Insufficient number of arguments for extension '" + command + "'.");
			} else {
				try {
					return optExtension.get().process(newArguments);
				} catch (ParseException | UncheckedIOException e) {
					throw new ExtensionException(e);
				}
			}
		}
	}

	@Override
	public String getExtensionName() {
		return NAME;
	}

	@Override
	public String getHelp() {
		StringBuffer sbuf = new StringBuffer();
		this.extensions.forEach(extension -> {
			sbuf.append(extension.getExtensionName());
			sbuf.append(SPACE);
			sbuf.append(extension.getHelp());
			sbuf.append(NEW_LINE);
		});
		return sbuf.toString();
	}

	@Override
	public int getRequiredArguments() {
		return REQUIRED_ARGUMENTS;
	}

}
