package com.pro100kryto.server.extensions.cmdinterpreter;

public interface ICommandCallback {
    Iterable<ACommand> getAllCommands();
}
