package main;

import java.util.Locale;
import manager.FileManager;
import manager.GUIManager;
import manager.IOManager;
import manager.PropertyManager;
import manager.ThreadManager;

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
     try{
		PropertyManager.loadDefaults();
		ThreadManager.initMainExecutor(
				Integer.parseInt(PropertyManager.getProperty("MAIN_EXECUTOR_TYPE"))
		);
		
		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler(){

					@Override
					public void uncaughtException(Thread t, Throwable e) {
						handleError(e,t,"CRITICAL ERROR:");
					}
			      
		});
		if(FileManager.isInstalled()){
			Main.installAndLoad();
		}
	 }catch(Throwable e){
		 handleError(e,Thread.currentThread(),"ERROR:");
	 }
	}

    public static void handleError(Throwable e,Thread t,String msg){
    	IOManager.errprint(msg+" in Thread "+t.getName()+"\nOf type "+e+"\n\n");
    	boolean logToFile;
    	String line;
    	while(true){
    		IOManager.errprint("Log error to file? (y/n)\n");
    		if((line = IOManager.nextLine()).toLowerCase(Locale.ROOT).equals("y")){logToFile=true;break;}
    		if(line.toLowerCase(Locale.ROOT).equals("n")){logToFile=false;break;}
    	}
    	
    	if(logToFile){
    		boolean changed = PropertyManager.getProperty("LOG").equals("false");
    		
    		PropertyManager.setProperty("LOG","true");
    		IOManager.log("ERRSYS/"+Thread.currentThread().getName(),"=============================================================================================");
    		IOManager.log("ERRSYS/"+Thread.currentThread().getName(),"THREAD: "+t.getName()+" EXCEPTION: "+e);
    		IOManager.log("", "");
    		
    		 for(int i=0;i<e.getStackTrace().length;i++){
    			 IOManager.log("STACKTRACE",e.getStackTrace()[i].toString());
    		 }
    		 IOManager.log("", "");
             IOManager.log("PROPERTIES", PropertyManager.getProperties().toString());
    	     
             IOManager.log("OS",System.getProperty("os.name")+" "+System.getProperty("os.arch"));
             IOManager.log("JVM", System.getProperty("java.runtime.name")+" "+System.getProperty("java.version")+" "+System.getProperty("java.vm.name"));
             IOManager.log("USERDIR", System.getProperty("user.dir"));
             IOManager.log("PROCESSORCOUNT", ""+Runtime.getRuntime().availableProcessors());
             IOManager.log("HEAP/TOTAL", ""+Runtime.getRuntime().totalMemory());
             IOManager.log("HEAP/FREE", ""+Runtime.getRuntime().freeMemory());
             IOManager.log("HEAP/MAX", ""+Runtime.getRuntime().maxMemory());
             
    		 IOManager.log("ERRSYS/"+Thread.currentThread().getName(), "=============================================================================================");
    		 
    		if(changed){PropertyManager.setProperty("LOG","false");}
    	}
		TerminalDialog.run();
    }
}
