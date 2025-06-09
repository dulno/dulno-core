package com.dulno.core.log;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

@Accessors(fluent = true)
public final class Log extends Logger {
  public static Log create(String name) throws Exception {
    var consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(LogFormat.create(LogFormat.FormatType.CONSOLE));
    consoleHandler.setLevel(Level.ALL);
    var log = new Log(name, null, consoleHandler);
    log.setLevel(Level.ALL);
    log.addHandler(consoleHandler);
    Runtime.getRuntime().addShutdownHook(new Thread(log::close));
    return log;
  }

  @Getter
  private final String name;
  private final Log parentLog;
  private final ConsoleHandler consoleHandler;
  @Getter
  private int currentLogLine = 0;

  private Log(
    String name, Log parentLog, ConsoleHandler consoleHandler
  ) {
    super(name, null);
    this.name = name;
    this.parentLog = parentLog;
    this.consoleHandler = consoleHandler;
  }

  @Override
  public void log(LogRecord record) {
    super.log(formatRecord(record));
    if (parentLog != null) {
      parentLog.increaseCurrentLogLine();
    } else {
      increaseCurrentLogLine();
    }
  }

  public void consoleLog(Level level, String message) {
    restrictedLog(consoleHandler, new LogRecord(level, message));
    if (parentLog != null) {
      parentLog.increaseCurrentLogLine();
    } else {
      increaseCurrentLogLine();
    }
  }

  private void restrictedLog(Handler handler, LogRecord record) {
    handler.publish(formatRecord(record));
  }

  private LogRecord formatRecord(LogRecord record) {
    record.setMessage("[" + getName().toUpperCase() + "] " + record.getMessage());
    return record;
  }

  public void close() {
    for (var handler : getHandlers()) {
      handler.close();
    }
  }

  public void increaseCurrentLogLine() {
    currentLogLine += 1;
  }

  public void resetCurrentLogLine() {
    currentLogLine = 0;
  }

  public Log subLog(String name) {
    var log = new Log(name, this, consoleHandler);
    log.setLevel(Level.ALL);
    log.addHandler(consoleHandler);
    Runtime.getRuntime().addShutdownHook(new Thread(log::close));
    return log;
  }
}
