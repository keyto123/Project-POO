package game.list;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import communication.GameInterface;
import communication.ProjectGame;

/**
 * Main utilities for the POO Project
 * 
 * @author lucas
 */
public class Utils {

	/**
	 * Get the folder where the games should be stored at
	 * 
	 * @return String - games folder path
	 */
	private static String getGameFolderPath() {
		
		URL tmp = Utils.class.getResource("/");
		URI tmp2 = null;
		try {
			tmp2 = tmp.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String path = tmp2.getPath() + "games";

		File gamesDir = new File(path);
		
		System.out.println("path = " + path);

		boolean created = gamesDir.mkdir();
		if (!created && (!gamesDir.exists() || !gamesDir.isDirectory())) {
			JOptionPane.showMessageDialog(null,
					"Failed to find/create games dir. Check if you have permission to create directory in the emulation folder");
			System.exit(1);
		}

		return path;
	}

	/**
	 * Get all jar files from the game folder path
	 * 
	 * @return ArrayList\ltJarFile\gt - Every jars found in the game folder path
	 */
	private static ArrayList<JarFile> getJars() {
		String gameFolderPath = Utils.getGameFolderPath();

		File fs[] = new File(gameFolderPath).listFiles();
		ArrayList<JarFile> jars = new ArrayList<JarFile>();
		
		if(fs.length == 0) {
			return jars; 
		}

		for (File i : fs) {
			String aux = i.getAbsolutePath();
			if (aux.endsWith(".jar")) {
				try {
					JarFile tmpJar = new JarFile(i);
					jars.add(tmpJar);
				} catch (IOException e) {
					System.out.println("Jar file opening error: " + e.getMessage());
				}
			}
		}

		return jars;
	}

	/**
	 * Get every game interface from the jars of the designed path
	 * 
	 * @return ArrayList\ltGameInterface\gt - contains every game interface found
	 */
	public static ArrayList<GameInterface> getAllGames() {
		ArrayList<JarFile> jars = getJars();
		ArrayList<GameInterface> games = new ArrayList<GameInterface>();

		for (JarFile jf : jars) {
			games.add(getGameInterface(jf));
		}

		return games;
	}

	/**
	 * 
	 * @param jarFile - a jar file to search for the interface
	 * @return JarFile - file that should contain the game
	 */
	private static GameInterface getGameInterface(JarFile jarFile) {

		Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();

			if (entry.getName().endsWith(".class")) {

				String className = entry.getName().substring(0, entry.getName().length() - 6);
				className = className.replaceAll("/", ".");

				URL urls[] = null;
				try {
					urls = new URL[] {
							new URL("jar:file:" + jarFile.getName() + "!/")
					};
				} catch (MalformedURLException e1) {
					System.out.println("URL Error: " + e1.getMessage());
					continue;
				}

				URLClassLoader cl = URLClassLoader.newInstance(urls);
				Class<?> c = null;

				try {
					c = cl.loadClass(className);
				} catch (ClassNotFoundException e) {
					System.out.println("Load class error: " + e.getMessage());
					break;
				}

				try {
					Class<?> parent = c.getSuperclass();
					if (parent.equals(ProjectGame.class)) {
						return (GameInterface) c.newInstance();
					}
				} catch (InstantiationException | IllegalAccessException e) {
					System.out.println("Class casting error: " + e.getMessage());
				}
			}
		}

		return null;
	}
}
