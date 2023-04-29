package com.duneyrefrigeracao.backend.infrastructure.logging;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;

public interface ILogging {

    void LogMessage(LogLevel level, String message);


}
