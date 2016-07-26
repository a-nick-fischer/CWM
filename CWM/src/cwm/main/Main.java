package cwm.main;

import java.io.File;
import java.io.PrintStream;
import java.util.Locale;

import cwm.manager.CommandManager;
import cwm.manager.FileManager;
import cwm.manager.GUIManager;
import cwm.manager.IOManager;
import cwm.manager.PropertyManager;
import cwm.manager.ThreadManager;

public class Main {
	
	private static final double Version = 0.2;
	
    public static void main(String[] args) {
		Main.prepare();
		IOManager.print("Class Wide Messenger v"+Version);
		Main.run();
	}
    
    public static void run(){
       while(true){
    	   IOManager.print("\n>");
    	   CommandManager.resolve(IOManager.nextLine());
       }
    }
    
	public static double getVersion(){
		return Version;
	}

	public static void installAndLoad(){
		FileManager.init();
		FileManager.install();
		PropertyManager.setup(FileManager.getFileByName("Prog.conf"));
		PropertyManager.load(FileManager.getFileByName("Prog.conf"));
		if(PropertyManager.getProperty("GUI").equals("true")){GUIManager.start();}
	}
	
	public static void prepare(){
     try{
		PropertyManager.loadDefaults();
		CommandManager.registerDefaults();
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
    	IOManager.errprint(msg+" in Thread \""+t.getName()+"\"\n Of type "+e+"\n");
    	
    	boolean logToFile=false;
    	String line;
    	
    	while(true){
    		IOManager.errprint("Log error to file? (y/n)\n>");
    		if((line = IOManager.nextLine()).toLowerCase(Locale.ROOT).equals("y")){logToFile=true;break;}
    		if(line.toLowerCase(Locale.ROOT).equals("n")){logToFile=false;break;}
    	}
    	
    	if(logToFile){
    		boolean changed = PropertyManager.getProperty("LOG").equals("false");
    		PropertyManager.setProperty("LOG","true");
    		
    		try{
    			
    		   if(FileManager.getFileByName("Cwm.log").exists()==false){
    			   IOManager.errprint("Program files not installed, generating log file");
    			   File f = new File("CWMErr.log");
    			   f.createNewFile();
    			   FileManager.setFileByName("Cwm.log", f);}
    		
    		}catch(Exception ex){
    		    IOManager.errprint("\nAn special error occured in exception handler!\n"+ex+"\n");
    		    IOManager.errprint("Please message me about this isuue, so i can fix it.\n nickkoro02@gmail.com\n\n");
    		    ex.printStackTrace(new PrintStream(IOManager.getErrorStream()));
    		    Main.run();
    		}
    		
    		IOManager.log("ERRSYS/"+Thread.currentThread().getName(),"=============================================================================================");
    		IOManager.log("ERRSYS/"+Thread.currentThread().getName(),"THREAD: "+t.getName()+" EXCEPTION: "+e);
    		IOManager.log("------", "");
    		
    		 for(int i=0;i<e.getStackTrace().length;i++){
    			 IOManager.log("STACKTRACE",e.getStackTrace()[i].toString());
    		 }
    		 IOManager.log("------", "");
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
		Main.run();
    }
}
