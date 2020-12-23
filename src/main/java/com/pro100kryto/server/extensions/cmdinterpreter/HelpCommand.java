package com.pro100kryto.server.extensions.cmdinterpreter;

import com.pro100kryto.server.IServerControl;
import com.pro100kryto.server.logger.ILogger;

public class HelpCommand extends ACommand {

    public HelpCommand(ICommandCallback callback, IServerControl serverControl, ILogger logger) {
        super(callback, serverControl, logger, "help");
    }

    @Override
    protected boolean execute(String[] cmdArr) throws Throwable {
        StringBuilder msg = new StringBuilder();
        Iterable<ACommand> commands = callback.getAllCommands();
        for (ACommand command : commands) {
            msg.append(System.lineSeparator())
                    .append("- ").append(command.getKey())
                    .append(" :: ").append(command.getInfo());
        }
        writeCmdResponse(logger, msg.toString());
        return true;
    }

    @Override
    public String getInfo() {
        return "commands list";
    }
}
