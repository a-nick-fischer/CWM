package main;

import manager.FileManager;
import manager.GUIManager;
import manager.IOManager;
import manager.PropertyManager;

public class Main {
	
	private static final double Version = 0.2;

	public static void main(String[] args) {
		Main.prepare();
		IOManager.print("Class Wide Messenger v"+Version);
		TerminalDialog.run();
	}

	public static double getVersion(){
		return Version;
	}

	public static void installAndLoad(){
		FileManager.install();
		PropertyManager.setup(FileManager.getPropertyFile());
		PropertyManager.load(FileManager.getPropertyFile());
		if(PropertyManager.getProperty("GUI").equals("true")){GUIManager.start();}
	}
	
	public static void prepare(){
		PropertyManager.init();
		if(FileManager.isInstalled()){
			Main.installAndLoad();
		}
	}
}
