package cwm.main;

import java.io.File;
import java.util.Locale;

import cwm.cmd.CommandRegistry;
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
    	   CommandRegistry.resolve(IOManager.nextLine());
       }
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
		CommandRegistry.registerDefaults();
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
    	boolean hasAnswer=false;
    	while(!hasAnswer){
    		IOManager.errprint("Log error to file? (y/n)\n>");
    		if((line = IOManager.nextLine()).toLowerCase(Locale.ROOT).equals("y")){logToFile=true;hasAnswer=true;break;}
    		if(line.toLowerCase(Locale.ROOT).equals("n")){hasAnswer=true;logToFile=false;break;}
    	}
    	
    	if(logToFile){
    		boolean changed = PropertyManager.getProperty("LOG").equals("false");
    		
    		PropertyManager.setProperty("LOG","true");
    		try{
    		if(FileManager.isInstalled()==false){File f = new File("CWMErr.log");f.createNewFile();FileManager.setLogFile(f);}
    		}catch(Exception ex){}
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
		Main.run();
    }
}
