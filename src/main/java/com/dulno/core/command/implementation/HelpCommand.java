package com.dulno.core.command.implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.dulno.core.command.Command;
import com.dulno.core.log.Log;

@Singleton
public final class HelpCommand extends Command {
  @Inject
  private HelpCommand(Log log) {
    super(log, "help", new String[] {"info", "commands"}, new String[0]);
  }

  @Override
  public boolean execute(String[] arguments) {
    log().info("Commands: ");
    log().info("- user");
    log().info("- bundle");
    log().info("- group");
    log().info("- team");
    log().info("- permission");
    log().info("- exit");
    return true;
  }
}
