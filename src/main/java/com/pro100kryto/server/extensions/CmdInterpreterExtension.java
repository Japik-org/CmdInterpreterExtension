package com.pro100kryto.server.extensions;

import com.pro100kryto.server.IServerControl;
import com.pro100kryto.server.StartStopStatus;
import com.pro100kryto.server.extension.IExtension;
import com.pro100kryto.server.extensions.cmdinterpreter.ACommand;
import com.pro100kryto.server.extensions.cmdinterpreter.HelpCommand;
import com.pro100kryto.server.extensions.cmdinterpreter.ICommandCallback;
import com.pro100kryto.server.extensions.cmdinterpreter.ServerCommand;
import com.pro100kryto.server.logger.ILogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdInterpreterExtension implements IExtension, ICommandCallback {
    private final IServerControl serverControl;
    private final ILogger logger;
    private final Map<String, ACommand> commands;
    private StartStopStatus status = StartStopStatus.STOPPED;

    public CmdInterpreterExtension(IServerControl serverControl) {
        this.serverControl = serverControl;
        logger = serverControl.getLoggerManager().createLogger("ext:CmdInterpreter");
        commands = new HashMap<>();
    }

    @Override
    public void start() throws Throwable {
        if (status!=StartStopStatus.STOPPED) throw new IllegalStateException("Is not stopped");
        status = StartStopStatus.STARTING;

        List<ACommand> commandList = new ArrayList<>();
        loadCommandList(commandList);
        for (ACommand c : commandList) {
            commands.put(c.getKey(), c);
        }

        status = StartStopStatus.STARTED;
    }

    @Override
    public void stop(boolean force) throws Throwable {
        if (status==StartStopStatus.STOPPED) throw new IllegalStateException("Already stopped");
        status = StartStopStatus.STOPPING;

        // ...

        status = StartStopStatus.STOPPED;
    }

    @Override
    public StartStopStatus getStatus() {
        return status;
    }

    @Override
    public String getType() {
        return "CmdInterpreter";
    }

    @Override
    public void sendCommand(String command) throws Throwable {
        logger.write("< "+command);

        // exec command
        try {
            String key = command.split(" ")[0];
            if (commands.containsKey(key))
                commands.get(key).execute(command);
            else
                logger.write("> Command not found");

        } catch (Throwable throwable){
            logger.writeException(throwable);
            logger.write("> Failed execute command");
        }
    }

    private void loadCommandList(List<ACommand> commands){
        commands.add(new HelpCommand(this, serverControl, logger));
        commands.add(new ServerCommand(this, serverControl, logger));
    }

    @Override
    public Iterable<ACommand> getAllCommands() {
        return commands.values();
    }
}
