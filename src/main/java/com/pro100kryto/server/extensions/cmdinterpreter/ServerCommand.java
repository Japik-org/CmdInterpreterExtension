package com.pro100kryto.server.extensions.cmdinterpreter;

import com.pro100kryto.server.IServerControl;
import com.pro100kryto.server.logger.ILogger;

public class ServerCommand extends ACommand {

    public ServerCommand(ICommandCallback callback, IServerControl serverControl, ILogger logger) {
        super(callback, serverControl, logger, "server");
    }

    @Override
    protected boolean execute(String[] cmdArr) {
        if (cmdArr.length==1){
            writeCmdResponse(logger, getInfo());
            return true;
        }

        if (cmdArr[1].equals("start")){
            boolean all = cmdArr.length==3 && cmdArr[2].equals("all");
            try {
                 serverControl.start();
                //writeCmdResponse(logger, "Server was "+(res?"successfully":"not")+" started");
            } catch (Throwable e) {
                logger.writeException(e);
            }
            writeCmdResponse(logger, "Server status = " + serverControl.getStatus());

        } else if (cmdArr[1].equals("stop")){
            boolean force = cmdArr.length==3 && cmdArr[2].equals("force");
            try {
                serverControl.stop(force);
            } catch (Throwable throwable){
                logger.writeException(throwable);
            }
            writeCmdResponse(logger, "Server status = " + serverControl.getStatus());

        } else if (cmdArr[1].equals("check")){
            writeCmdResponse(logger, "Server status = " + serverControl.getStatus());
            writeCmdResponse(logger, "Services: " + serverControl.getServiceManager().getServiceNames().toString());
            //writeCmdResponse(logger, "Executing "+cmdInterpreter.getCmdCountInProcess()+" commands");

        } else {
            return false;
        }

        return true;
    }

    @Override
    public String getInfo() {
        StringBuilder str = new StringBuilder();
        String initial = System.lineSeparator()+"-- "+getKey()+" ";

        str.append(initial).append("start").append(" :: ").append("start server");
        //str.append(initial).append("start all").append(" :: ").append("start server, all saved services and modules");
        str.append(initial).append("stop").append(" :: ").append("stop server and all processes");
        str.append(initial).append("stop force").append(" :: ").append("stop server and all processes FORCE");
        str.append(initial).append("check").append(" :: ").append("show current status");

        return str.toString();
    }
}
