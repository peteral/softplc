package de.peteral.softplc.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.view.error.ErrorDialog;

/**
 * Serializes / de-serializes objects from / to JAR files.
 *
 * @author peteral
 *
 */
public class MemorySerializer {

	private static final String FILE_NAME = "data";

	public static void save(File file, Memory memory) {
		try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(file))) {
			JarEntry entry = new JarEntry(FILE_NAME);
			jos.putNextEntry(entry);

			ObjectOutputStream os = new ObjectOutputStream(jos);
			os.writeObject(memoryToSerializable(memory));
			jos.closeEntry();
			os.flush();

		} catch (Exception e) {
			ErrorDialog.show("Failed serializing snapshot", e);
		}

	}

	private static Serializable memoryToSerializable(Memory memory) {
		MemoryAreaData[] data = new MemoryAreaData[memory.getMemoryAreas().size()];

		int index = 0;
		for (MemoryArea memoryArea : memory.getMemoryAreas()) {
			data[index++] = new MemoryAreaData(memoryArea.getAreaCode().get(),
					memoryArea.readBytes(0, memoryArea.getSize().get()));
		}

		return data;
	}

	public static void load(File file, Memory memory) {
		try (JarInputStream jis = new JarInputStream(new FileInputStream(file))) {
			JarEntry entry = jis.getNextJarEntry();
			if (!entry.getName().equals(FILE_NAME)) {
				ErrorDialog.show("Invalid file contents !!!", null);
				return;
			}

			ObjectInputStream is = new ObjectInputStream(jis);
			MemoryAreaData[] data = (MemoryAreaData[]) is.readObject();
			boolean warning = false;

			for (MemoryAreaData block : data) {
				try {
					memory.writeBytes(block.getAreaCode(), 0, block.getBytes());
				} catch (Exception e) {
					Logger.getLogger("snapshot").log(Level.WARNING,
							"Failed loading memory area [" + block.getAreaCode() + "]: ", e);
				}
			}

			if (warning) {
				ErrorDialog.show("There were warnings - check log file", null);
			}
		} catch (Exception e) {
			ErrorDialog.show("Failed loading snapshot", e);
		}

	}

}
