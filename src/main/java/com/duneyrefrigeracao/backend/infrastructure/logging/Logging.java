package com.duneyrefrigeracao.backend.infrastructure.logging;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging implements ILogging {

    private Logger _logger;

    public Logging(Class<?> classObj){
        this._logger = LogManager.getLogger(classObj);
    }


    @Override
    public void LogMessage(LogLevel level, String message) {
        if(this._logger == null) {
            this._logger = LogManager.getLogger(Logging.class);
        }

        switch (level){
            case INFO -> this._logger.info(message);
            case DEBUG -> this._logger.debug(message);
            case WARN -> this._logger.warn(message);
            case ERROR -> this._logger.error(message);

            default -> this._logger.warn(String.format("Tentativa de entrada de Log sem parametrização, corrigir para logs mais precisos -> %s",message));
        }


    }

}
