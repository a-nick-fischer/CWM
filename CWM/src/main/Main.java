package main;

import manager.FileManager;
import manager.GUIManager;
import manager.IOManager;
import manager.PropertyManager;

public class Main {
	
	private static final double Version = 0.2;

	public static void main(String[] args) {
		Main.prepare();
		IOManager.print("Class Wide Messenger v"+0.2);
		TerminalDialog.run();
	}

	public static double getVersion(){
		return Version;
	}

	public static void installAndLoad(){
		FileManager.install();
		PropertyManager.setup(FileManager.getPropertyFile());
		PropertyManager.load(FileManager.getPropertyFile());
		if(Boolean.parseBoolean(PropertyManager.getProperty("GUI"))){GUIManager.start();}
	}
	
	public static void prepare(){
		if(FileManager.isInstalled()){
			Main.installAndLoad();
		}
	}
}
