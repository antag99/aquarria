package com.github.antag99.aquarria.xnb;

import com.badlogic.gdx.files.FileHandle;

public final class Steam {
	private Steam() {
	}

	public static FileHandle findTerrariaDirectory() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			// Check the windows registry for steam installation path
			try {
				String steamPath = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
						"Software\\Valve\\Steam", "SteamPath");
				FileHandle steamDirectory = new FileHandle(steamPath);
				if (isTerrariaDirectory(steamDirectory)) {
					return steamDirectory;
				}
			} catch (Throwable ignored) {
			}
		}

		// If nothing other works, then prompt the user
		return null;
	}

	public static boolean isTerrariaDirectory(FileHandle terrariaDirectory) {
		if (!terrariaDirectory.isDirectory()) {
			return false;
		}

		FileHandle terrariaExe = terrariaDirectory.child("Terraria.exe");
		FileHandle contentDirectory = terrariaDirectory.child("Content");

		return terrariaExe.exists() && contentDirectory.exists();
	}
}
