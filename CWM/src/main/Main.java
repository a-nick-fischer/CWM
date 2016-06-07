package main;

import manager.FileManager;
import manager.GUIManager;
import manager.PropertyManager;

public class Main {
	
	private static final double Version = 0.2;
	private static boolean isInst = false;

	public static void main(String[] args) {
	 System.out.println("ok");
	}

	public static double getVersion(){
		return Version;
	}

	public static void setup(){
		FileManager.setup();
		PropertyManager.setup(FileManager.getPropertyFile());
		PropertyManager.load(FileManager.getPropertyFile());
		if(Boolean.parseBoolean(PropertyManager.getProperty("Gui"))){GUIManager.start();}
	}
	
	public static boolean isInstalled(){
		return isInst;
		
	}
	
	public static void setInstalled(boolean isInst){
		Main.isInst=isInst;
	}
}
