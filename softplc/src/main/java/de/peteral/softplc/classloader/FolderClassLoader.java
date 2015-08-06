package de.peteral.softplc.classloader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class loader covers all *.jar files in a folder.
 *
 * @author peteral
 *
 */
public class FolderClassLoader extends URLClassLoader {

	/**
	 * Creates a new instance working with given folder.
	 *
	 * @param folder
	 *            all jars in this folder will be covered by this class loader
	 */
	public FolderClassLoader(File folder) {
		super(new URL[] {});
		addJars(folder);
	}

	private void addJars(File folder) {
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String[] jarFiles = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		for (String jarFile : jarFiles) {
			try {
				File f = new File(folder, jarFile);
				super.addURL(f.toURI().toURL());
			} catch (MalformedURLException e) {
				Logger.getLogger("FolderClassLoader").log(Level.WARNING, "Failed adding [" + jarFile + "]: ", e);
			}
		}
	}
}
