package cwm.main;

import java.io.File;
import java.io.PrintStream;
import java.util.Locale;

import cwm.manager.CommandManager;
import cwm.manager.CommunikationManager;
import cwm.manager.FileManager;
import cwm.manager.GUIManager;
import cwm.manager.IOManager;
import cwm.manager.PropertyManager;
import cwm.manager.ThreadManager;

public class Main {
	
	private static final double Version = 0.3;
	
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
	
	public static void prepare(){
    try{
 		  FileManager.init();
		  PropertyManager.loadDefaults();
		  CommandManager.registerDefaults();
		  if(FileManager.isInstalled()){
		    PropertyManager.load(FileManager.getFileByName("Prog.conf"));
		    CommunikationManager.hostAutostartServer();
		  }
		  ThreadManager.initMainExecutor(
		    Integer.parseInt(PropertyManager.getProperty("MAIN_EXECUTOR_TYPE"))
		  );
		  Thread.setDefaultUncaughtExceptionHandler((t,e) -> handleError(e,t,"CRITICAL ERROR:"));
	  	if(PropertyManager.getProperty("GUI").equals("true")){
				GUIManager.start();
		}
	  }catch(Throwable e){
		  handleError(e,Thread.currentThread(),"ERROR:");
	  }
	}

  public static void handleError(Throwable e,Thread t,String msg){
    try{
    	IOManager.errprint(msg+" in Thread \""+t.getName()+"\"\n Of type \n"+e+"\n");
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
    		if(FileManager.getFileByName("Cwm.log").exists()==false){
    			IOManager.errprint("Program files not installed, generating log file");
    			File f = new File("CWMErr.log");
    			f.createNewFile();
    			FileManager.setFileByName("Cwm.log", f);
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
    		if(changed){
					PropertyManager.setProperty("LOG","false");
				}
    	}
		  Main.run();
      }catch(Exception ex){
        PropertyManager.setProperty("LOG", "false");
    	  PropertyManager.setProperty("ANSI", "false");
    	  IOManager.errprint("\nAn error occured in exception handler!\n"+ex+"\n"+
		                       "Please message me about this isuue, so i can fix it.\n nickkoro02@gmail.com\n"+
    			                 "Hint: LOG and ANSI were set to false, as they can cause the issue.");
		    ex.printStackTrace(new PrintStream(IOManager.getErrorStream()));
      }
   }
}
