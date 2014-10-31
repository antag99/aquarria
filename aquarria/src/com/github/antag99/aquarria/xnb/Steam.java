package com.github.antag99.aquarria.xnb;

import com.badlogic.gdx.files.FileHandle;

public final class Steam {
	private Steam() {
	}

	public static FileHandle findTerrariaDirectory() {
		// Check the windows registry for steam installation path
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
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

		// Else prompt the user
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
