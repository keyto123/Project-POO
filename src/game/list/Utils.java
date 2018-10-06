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

	private static String gameFolderPath;
	private static ArrayList<GameInterface> gameList;

	/**
	 * @return - ArrayList of GameInterface
	 */
	public static ArrayList<GameInterface> getGameList() {
		return gameList;
	}

	/**
	 * Check if between a dir and a file
	 * 
	 * @param f      - file selected by user
	 * @param insert - true if should insert file into games folder, false otherwise
	 * @return false if not a dir and not a file, true otherwise
	 */
	public static boolean updateGameList(File f, boolean insert) {
		if (f.isDirectory()) {
			return updateGameListFromDir(f, insert);
		} else if (f.isFile()) {
			return updateGameListFromFile(f, insert);
		} else {
			return false;
		}
	}
	
	public static ProjectGame getGameInstance(int index) {
		try {
			return (ProjectGame)gameList.get(index).getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	public static ProjectGame getGameInstance(GameInterface g) {
		try {
			return (ProjectGame) g.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * Get the list of files at dir and call updateGameListFromFile for each jar
	 * file
	 * 
	 * @param file   - directory
	 * @param insert - true if should insert file at games folder
	 * @return true if everything went fine, false otherwise
	 */
	private static boolean updateGameListFromDir(File file, boolean insert) {
		File files[] = file.listFiles();

		boolean error = false;

		for (File f : files) {
			if (f.getAbsolutePath().endsWith(".jar")) {
				error = error || !updateGameListFromFile(f, insert);
			}
		}

		return error;
	}

	/**
	 * Add a game to games list
	 * 
	 * @param jf - JarFile with the game
	 * @return true if could add the game, false if Jar is not of a game
	 */
	private static boolean addGame(JarFile jf) {
		GameInterface result = getGameInterface(jf);
		if(gameList != null) {
			for(GameInterface g : gameList) {
				if(result.getGameName().equals(g.getGameName())) {
					return false;
				}
			}
		}
		if (result != null) {
			gameList.add(result);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the jarfile and add the game if it's a game
	 * 
	 * @param file   - file
	 * @param insert - true if should insert file at games folder
	 * @return
	 */
	private static boolean updateGameListFromFile(File file, boolean insert) {
		JarFile jar = null;
		try {
			jar = jarFromFile(file);
		} catch (IOException e) {
			return false;
		}

		if (jar == null) {
			return false;
		}

		boolean gameAdded = addGame(jar);
		if(!gameAdded) {
			return false;
		}
		
		if (gameAdded && insert) {
			insertFileAtGamesFolder(file);
		}

		return true;
	}

	/**
	 * Move a file to games folder using file.renameTo
	 * @param file
	 */
	private static void insertFileAtGamesFolder(File file) {
		String fileName = file.getName();

		try {
			fileName = new URI(fileName).getPath();
		} catch (URISyntaxException e) {
			System.out.println("URI syntax error: " + e.getMessage());
			return;
		}

		String path = gameFolderPath + File.separator + fileName;

		if (!file.renameTo(new File(path))) {
			System.out.println("Failed to move file");
		}
	}

	/**
	 * Must be called to initialize everything before using using them
	 * 
	 * @throws URISyntaxException - when trying to convert gameFolderPath to URI
	 */
	public static void initAll() throws URISyntaxException, IOException {

		initGameFolderPath();
		initGameList();
	}

	/**
	 * Get the folder where the games should be stored at
	 * 
	 * @return String - games folder path
	 */
	private static void initGameFolderPath() throws URISyntaxException {
		String base_path = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		int dif = base_path.length() - base_path.lastIndexOf(File.separator);
		base_path = base_path.substring(0, base_path.length() - dif);
		
		String path = base_path + File.separator + "games";

		File gamesDir = new File(path);

		boolean created = gamesDir.mkdir();
		if (!created && (!gamesDir.exists() || !gamesDir.isDirectory())) {
			JOptionPane.showMessageDialog(null,
					"Failed to find/create games dir. Check if you have permission to create directory in the emulation folder");
		}

		gameFolderPath = path;
	}

	/**
	 * Get a JarFile from a File
	 * 
	 * @param f - File to convert
	 * @return JarFile after converting or null if failed
	 * @throws IOException
	 */
	private static JarFile jarFromFile(File f) throws IOException {
		String aux = f.getAbsolutePath();
		if (aux.endsWith(".jar")) {
			return new JarFile(f);
		}
		return null;
	}

	/**
	 * Get all jar files from the game folder path
	 * 
	 * @return ArrayList\ltJarFile\gt - Every jars found in the game folder path
	 */
	private static ArrayList<JarFile> getJars(String path) throws IOException {
		File fs[] = new File(path).listFiles();
		ArrayList<JarFile> jars = new ArrayList<JarFile>();

		if (fs.length == 0) {
			return jars;
		}

		for (File f : fs) {
			JarFile tmp = jarFromFile(f);
			if (tmp != null) {
				jars.add(jarFromFile(f));
			}
		}

		return jars;
	}

	/**
	 * Get every game interface from the jars of the designed path
	 * 
	 * @return ArrayList\ltGameInterface\gt - contains every game interface found
	 */
	private static void initGameList() throws IOException {
		ArrayList<JarFile> jars = getJars(gameFolderPath);
		gameList = new ArrayList<GameInterface>();

		for (JarFile jf : jars) {
			addGame(jf);
		}
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
