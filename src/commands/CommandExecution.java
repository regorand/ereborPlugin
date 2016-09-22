package commands;

import org.bukkit.command.CommandSender;

public abstract class CommandExecution {
    public abstract void execute(CommandSender sender, String label, String[] args);
}
