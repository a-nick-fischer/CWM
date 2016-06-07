package manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class IOManager{
	private static OutputStream DefOut   = System.out;
	private static OutputStream DefErr   = System.err;
	private static InputStream  DefIn    = System.in;
	
	private static PrintWriter MPrinter  = new PrintWriter(DefOut); //TODO in Stream setter override Printer and Scanner
	private static PrintWriter MEPrinter = new PrintWriter(DefErr);
	private static Scanner     MScanner  = new Scanner(DefIn);
	
	
	public static void setOutputStream(OutputStream OS){
		DefOut=OS;
		MPrinter=new PrintWriter(DefOut);
	}
	
	public static OutputStream getOutputStream(){
		return DefOut;
	}
	
	public static void setErrorStream(OutputStream OS){
		DefErr=OS;
		MEPrinter=new PrintWriter(DefErr);
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

    public static void println(String str){
    	MPrinter.println(str);
    }

    public static void print(String str){
    	MPrinter.print(str);
    }
    
    public static void errprintln(String str){
    	MEPrinter.println(str);
    }

    public static void errprint(String str){
    	MEPrinter.print(str);
    }
    
    public static boolean hasNextLine(){
    	return MScanner.hasNextLine();
    }
    
    public static String nextLine(){
    	return MScanner.nextLine();
    }
}
