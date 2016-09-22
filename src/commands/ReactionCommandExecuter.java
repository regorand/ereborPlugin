package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;

/**
 * Created by Jan-Luca on 21.09.2016.
 */
public class ReactionCommandExecuter implements CommandExecutor{

    Plugin plugin;

    public ReactionCommandExecuter(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String name;
        if(!(commandSender instanceof Player)){
            return true;
        }


        Player player = (Player) commandSender;

        switch (label.toLowerCase()){
            case "wtf":
                name = args.length == 0 ? "Thomas" : args[0];
                Random r = new Random();
                int ra = r.nextInt();
                if(ra % 20 == 0){
                    Bukkit.broadcastMessage(name + ", don't get Bushy");
                }else if (ra % 10 == 0){
                    Bukkit.broadcastMessage(name + ", don't get pushy");
                }
                Bukkit.broadcastMessage("wtf " + name);
                return true;
            case "nice":
                name = args.length == 0 ? "" : args[0];
                Bukkit.broadcastMessage("nice one " + name);
                return true;
            case "?":
                name = args.length == 0 ? "" : args[0];
                Player player2 = findPlayerByName(name);
                if(player2 != null){
                    player.sendMessage("@" + name + " ?");
                    player2.teleport(player);
                }else {
                    Bukkit.broadcastMessage("@" + name + " ?");
                }
                return true;
        }
        return false;
    }

    private Player findPlayerByName(String name){
        for(Player player: plugin.getServer().getOnlinePlayers()){
            if(player.getDisplayName().equalsIgnoreCase(name)){
                return player;
            }
        }
        return null;
    }
}
