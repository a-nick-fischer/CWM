package manager;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import main.Colors;
import main.Main;

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
	
	public static String filterAnsi(String str){
		str=str.replaceAll("\\u001B\\[0m", "");
		str=str.replaceAll("\\u001B\\[4m", "");
		str=str.replaceAll("\\u001B\\[30m", "");
		str=str.replaceAll("\\u001B\\[31m", "");
		str=str.replaceAll("\\u001B\\[32m", "");
		str=str.replaceAll("\\u001B\\[33m", "");
		str=str.replaceAll("\\u001B\\[34m", "");
		str=str.replaceAll("\\u001B\\[35m", "");
		str=str.replaceAll("\\u001B\\[36m", "");
		str=str.replaceAll("\\u001B\\[37m", "");
		str=str.replaceAll("\\u001B\\[40m", "");
		str=str.replaceAll("\\u001B\\[41m", "");
		str=str.replaceAll("\\u001B\\[42m", "");
		str=str.replaceAll("\\u001B\\[43m", "");
		str=str.replaceAll("\\u001B\\[44m", "");
		str=str.replaceAll("\\u001B\\[45m", "");
		str=str.replaceAll("\\u001B\\[46m", "");
		str=str.replaceAll("\\u001B\\[47m", "");
		return str;
	}
	
    public static void print(String str){
    if(PropertyManager.getProperty("ANSI").equals("false")){
    	str = filterAnsi(str);
    }else{
    	str = str+Colors.RESET;
    }
    	MPrinter.print(str);
    	MPrinter.flush();
    	
    	if(str!="\n>"){
    	str = filterAnsi(str);
        log("SYS/"+Thread.currentThread().getName(),str);
        }
    }

    public static void errprint(String str){
    	log("ERRSYS/"+Thread.currentThread().getName(),str);
    	if(PropertyManager.getProperty("ANSI").equals("true")){
    	MEPrinter.print(Colors.RED+str+Colors.RESET);
    	}else{
        str = filterAnsi(str);
    	MEPrinter.print(str);
    	}
    	MEPrinter.flush();
    }
    
    public static String nextLine(){
    	String str=MScanner.nextLine();
    	log("USER",str);
    	return str;
    }
    
    public static void log(String name,String str){
    	        if(PropertyManager.getProperty("LOG").equals("true") && FileManager.isInstalled()){
    	        	try {
    	        		String Date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    	    			FileLogger = new PrintStream(new FileOutputStream(FileManager.getLogFile(),true));
    	    			FileLogger.println("["+Date+"]"+"("+name+") "+str);
    	        	} catch (Throwable e) {
    					Main.handleError(e,Thread.currentThread(),"ERROR:");
    				}
    }}
 }
