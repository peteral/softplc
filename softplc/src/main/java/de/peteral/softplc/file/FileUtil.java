package de.peteral.softplc.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.peteral.softplc.view.error.ErrorDialog;

public class FileUtil {

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
