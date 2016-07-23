package cwm.cmd;

import java.util.HashMap;

public class CommandRegistry {
	
	private static HashMap<String,Command> Commands = new HashMap<>(); 
	
  public static void register(String args,Reaction r){
	  Command Cmd = Command.create(args, r);
	  Commands.put(args.split(" ")[0], Cmd);
  }
  
  public static void deregister(String key){
	  Commands.remove(key);
  }

  public static void deregister(Command cmd){
	  Commands.remove(cmd.getKey());
  }

  public static boolean isRegistred(Command cmd){
	  return Commands.containsValue(cmd);
  }
  
  public static boolean isRegistred(String key){
	  return Commands.containsKey(key);
  }
  
  public static void resolve(String args){
	  String KEY = args.split(" ")[0];
	  if(Commands.containsKey(KEY)){
		  Commands.get(KEY).invoke(args);
	  }
  }

  public static void registerDefaults(){
	  
	  final Reaction EXIT = (args) -> {CmdMethods.SYS_EXIT(args);};
	  final Reaction SYS_EXIT = (args) -> {CmdMethods.SYS_EXIT(args);};
	  final Reaction SYS_CALL = (args) -> {CmdMethods.SYS_CALL(args);};
	  final Reaction SYS_ANSI_TEST = (args) -> {CmdMethods.SYS_ANSI_TEST(args);};
	  final Reaction SYS_RUNTIME_ERROR_TEST = (args) -> {CmdMethods.SYS_RUNTIME_ERROR_TEST(args);};
	  final Reaction SYS_CHECKED_ERROR_TEST = (args) -> {CmdMethods.SYS_CHECKED_ERROR_TEST(args);};
	  final Reaction SYS_GETUPDATE = (args) -> {CmdMethods.SYS_GETUPDATE(args);};
	  final Reaction SYS_INSTALL_UPDATE = (args) -> {CmdMethods.SYS_INSTALL_UPDATE(args);};
	  final Reaction PM_SET = (args) -> {CmdMethods.PM_SET(args);};
	  final Reaction PM_GET = (args) -> {CmdMethods.PM_GET(args);};
	  final Reaction PM_STORE = (args) -> {CmdMethods.PM_STORE(args);};
	  final Reaction PM_LOAD = (args) -> {CmdMethods.PM_LOAD(args);};
	  final Reaction FM_INSTALL = (args) -> {CmdMethods.FM_INSTALL(args);};
	  final Reaction FM_INSTALLED = (args) -> {CmdMethods.FM_INSTALLED(args);};
	  final Reaction FM_DELETE_ALL = (args) -> {CmdMethods.FM_DELETE_ALL(args);};
	  final Reaction GM_START = (args) -> {CmdMethods.GM_START(args);};
	  final Reaction CM_HOST = (args) -> {CmdMethods.CM_HOST(args);};
	  final Reaction CM_CONNECT = (args) -> {CmdMethods.CM_CONNECT(args);};
	  final Reaction CM_SHUTDOWN = (args) -> {CmdMethods.CM_SHUTDOWN(args);};
	  final Reaction CM_BROADCAST = (args) -> {CmdMethods.CM_BROADCAST(args);};
	  final Reaction CM_BAN = (args) -> {CmdMethods.CM_BAN(args);};
	  final Reaction CM_DISBAN = (args) -> {CmdMethods.CM_DISBAN(args);};
	  final Reaction CM_INFO = (args) -> {CmdMethods.CM_INFO(args);};
	  
	  
	  register("exit #INT",EXIT);
	  register("sys-exit #INT",SYS_EXIT);
	  register("sys_call STR #STR",SYS_CALL);
	  register("sys-ansi-test",SYS_ANSI_TEST);
	  register("sys-runtime-error-test #STR",SYS_RUNTIME_ERROR_TEST);
	  register("sys-checked-error-test #STR",SYS_CHECKED_ERROR_TEST);
	  register("sys-getupdate STR",SYS_GETUPDATE);
	  register("sys-install-update STR",SYS_INSTALL_UPDATE);
	  register("pm-set STR STR",PM_SET);
	  register("pm-get",PM_GET);
	  register("pm-store #STR",PM_STORE);
	  register("pm-load #STR",PM_LOAD);
	  register("fm-install",FM_INSTALL);
	  register("fm-installed",FM_INSTALLED);
	  register("fm-delete-all",FM_DELETE_ALL);
	  register("gm-start",GM_START);
	  register("cm-host STR #INT",CM_HOST);
	  register("cm-connect STR",CM_CONNECT);
	  register("cm-shutdown STR",CM_SHUTDOWN);
	  register("cm-broadcast STR STR",CM_BROADCAST);
	  register("cm-ban STR STR",CM_BAN);
	  register("cm-disban STR STR",CM_DISBAN);
	  register("cm-info",CM_INFO);
	  
  }
}
