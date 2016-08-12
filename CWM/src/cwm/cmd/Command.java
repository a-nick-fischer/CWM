package cwm.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cwm.manager.IOManager;

public class Command {
  private String Key;
  private List<String> Arguments;
  private Reaction R;
  private String Syntax = "No syntax avaible";
  
  private Command(String Key, List<String> Arguments, Reaction R){
	  this.Key = Key;
	  this.Arguments = Arguments;
	  this.R = R;
  }
  
  private boolean isOptional(String args){
	  return args.charAt(0)=='?'? true : false;
  }
  
  private String getType(String args){
	  try{
		  Integer.parseInt(args);
		  return "INT";
	  }catch(Exception e){
		  return "STR";
	  }
  }
  
  private boolean checkSyntax(List<String> args){
	  if(args.get(0).equals(Key)==false){return false;}
	  
	  int gotSize = args.size();
	  int minSize = 0;
	  
	  for(String Pattern: Arguments){
		  if(isOptional(Pattern)){
			  break;
		  }else{minSize++;}
	  }
	 
	  if(gotSize<minSize){return false;}
	  
	  for(int i =1;i<Arguments.size();i++){
		  String Comparer = null;
		  if(isOptional(Arguments.get(i-1))){
			  Comparer = Arguments.get(i-1).replaceAll("\\?","");
		  }
		  else{
			  Comparer = Arguments.get(i-1);
		  }

		  if(getType(args.get(i)).equals(Comparer)==false){
			  if(isOptional(Arguments.get(i))){
				  continue;
			  }else return false;
		  }
	  }
	  return true;
  }
  
  public String getKey(){
	  return Key;
  }
  
  public void invoke(String args){
	  List<String> Arguments = new ArrayList<>(Arrays.asList(args.split(" ")));
	  if(checkSyntax(Arguments)){
		  R.react(Arguments);
	  }else{printSyntax();}
  }

  private void printSyntax() {
	IOManager.print(Syntax);
  }

  public static Command create(String args, Reaction r){
	  List<String> ARGUMENTS = new ArrayList<>(Arrays.asList(args.split(" ")));
	  String KEY = ARGUMENTS.remove(0);
	  return new Command(KEY,ARGUMENTS,r);
  }
}