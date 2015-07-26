package de.peteral.softplc.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.peteral.softplc.view.error.ErrorDialog;

/**
 * This utility class helps with conversion between relative and absolute file
 * paths.
 *
 * @author peteral
 *
 */
public final class FileUtil {

	private FileUtil() {
	}

	/**
	 * Translates path relative to basePath to absolute path
	 *
	 * @param file
	 *            relative path
	 * @param basePath
	 *            base path (relative path is relative to this file)
	 * @return absolute path, null on error
	 */
	public static String toAbsolute(String file, File basePath) {
		File f = new File(file);
		if ((basePath != null) && !f.isAbsolute()) {
			try {
				Path pathBase = Paths.get(basePath.getCanonicalPath());
				Path pathRelative = Paths.get(f.getPath());
				Path pathAbsolute = pathBase.resolve(pathRelative);
				return pathAbsolute.toString();
			} catch (Exception e) {
				ErrorDialog.show("Failed converting to absolute", e);
			}
		}

		return null;
	}

	/**
	 * Translates absolute path to path relative a base path.
	 *
	 * @param file
	 *            absolute file path
	 * @param basePath
	 *            base path
	 * @return path relative to base path, null on error
	 */
	public static String toRelative(String file, File basePath) {
		File f = new File(file);
		if (f.isAbsolute() && (basePath != null)) {
			try {
				Path pathAbsolute = Paths.get(f.getCanonicalPath());
				Path pathBase = Paths.get(basePath.getCanonicalPath());
				Path pathRelative = pathBase.relativize(pathAbsolute);

				return pathRelative.toString();
			} catch (Exception e) {
				ErrorDialog.show("Failed converting to relative", e);
			}
		}

		return null;
	}
}
