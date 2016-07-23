--example of supported operations in a future lua update

Hoster = require("Hoster")
Register = require("Register")

--Hoster.shutdownServer(ServerNameOrID)
--Hoster.startServer(ServerNameOrID)
--Hoster.saveServer(ServerNameOrID)
--Hoster.deleteServer(ServerNameOrID)
--Hoster.getServerByScript(ScriptName)
--Hoster.ban(ServerNameOrID,InetAddressName)
--Hoster.disban(ServerNameOrID,InetAddressName)
--Hoster.setBannedMSG(ServerNameOrID,String bannedmsg)

--CommandRegister.register(CommandOrName,Reaction)
--CommandRegister.deregister(CommandOrName)
--CommandRegister.resolve(CommandName)
--CommandRegister.isRegistred(Command)

function startServerHook(ServerName)
end

function shutdownServerHook(ServerName)
end

function processServerMSG(ServerName,msg)
end

function processClientMessage(ScriptName,msg)
end

function startProgHook()
end

function shutdownProgHook()
end

function connectToServerHook(ServerName,ServerIP,table requirements)
end

function disconnectFromServerHook(ServerName,ServerIP,table requirements)
end