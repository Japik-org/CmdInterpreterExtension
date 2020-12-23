package com.pro100kryto.server.extensions.cmdinterpreter;

import com.pro100kryto.server.IServerControl;
import com.pro100kryto.server.logger.ILogger;

public abstract class ACommand{
    protected final ICommandCallback callback;
    protected final IServerControl serverControl;
    protected final ILogger logger;
    protected final String key;

    protected ACommand(ICommandCallback callback, IServerControl serverControl, ILogger logger, String key) {
        this.callback = callback;
        this.serverControl = serverControl;
        this.logger = logger;
        this.key = key;
    }

    public void execute(String command) throws Throwable {
        String[] cmdArr = command.split(" ");
        if (!cmdArr[0].equals(key)) throw new IllegalArgumentException();
        if (!execute(cmdArr)){
            writeCmdResponse(logger,"Wrong command");
        }
    }

    public final String getKey() {
        return key;
    }

    protected abstract boolean execute(String[] cmdArr) throws Throwable;

    protected void writeCmdResponse(ILogger logger, String msg){
        logger.write("> "+msg);
    }

    public abstract String getInfo();
}
