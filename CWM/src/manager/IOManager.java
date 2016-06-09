package manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOManager{
	private static OutputStream DefOut   = System.out;
	private static OutputStream DefErr   = System.err;
	private static InputStream  DefIn    = System.in;
	
	private static PrintStream MPrinter  = new PrintStream(DefOut);
	private static PrintStream MEPrinter = new PrintStream(DefErr);
	private static Scanner     MScanner  = new Scanner(DefIn);
	
	private static PrintStream FileLogger;
	
	public static void setOutputStream(OutputStream OS){
		DefOut=OS;
		MPrinter=new PrintStream(DefOut);
	}
	
	public static OutputStream getOutputStream(){
		return DefOut;
	}
	
	public static void setErrorStream(OutputStream OS){
		DefErr=OS;
		MEPrinter=new PrintStream(DefErr);
	}
	
	public static OutputStream getErrorStream(){
		return DefErr;
	}
	
	public static void setInputStream(InputStream IS){
		DefIn=IS;
		MScanner=new Scanner(DefIn);
	}

	public static InputStream getInputStream(){
		return DefIn;
	}

    public static void print(String str){
      if(PropertyManager.getProperties().containsKey("LOG")){
        if(PropertyManager.getProperty("LOG").equals("true")){
        	try {
    			FileLogger = new PrintStream(FileManager.getLogFile());
    			FileLogger.println("[SYS/"+Thread.currentThread()+"] "+str);
    		} catch (Exception e) {
    			//TODO
    		}
        }}
    	MPrinter.print(str);
    }

    public static void errprint(String str){
    	if(PropertyManager.getProperties().containsKey("LOG")){
        if(PropertyManager.getProperty("LOG").equals("true")){
        	try {
    			FileLogger = new PrintStream(FileManager.getLogFile());
    			FileLogger.println("[SYSERR/"+Thread.currentThread()+"] "+str);
    		} catch (Exception e) {
    			//TODO
    		}
        }}
    	MEPrinter.print(str);
    }
    
    public static boolean hasNextLine(){
    	return MScanner.hasNextLine();
    }
    
    public static String nextLine(){
    	
    	String str=MScanner.nextLine();
    	
    	if(PropertyManager.getProperties().containsKey("LOG")){
        if(PropertyManager.getProperty("LOG").equals("true")){
        	try {
    			FileLogger = new PrintStream(FileManager.getLogFile());
    			FileLogger.println("[SYS/"+Thread.currentThread()+"] "+str);
    		} catch (Exception e) {
    			//TODO
    		}
        }}
        
    	return str;
    }
}
