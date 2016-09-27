package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;

public class ReactionCommandExecuter implements CommandExecutor{

    Plugin plugin;

    HashMap<String, CommandExecution> COMMANDS = new HashMap<>();

    public ReactionCommandExecuter(JavaPlugin plugin){
        loadCommands();
        this.plugin = plugin;
        for(String key: COMMANDS.keySet()){
            plugin.getCommand(key).setExecutor(this);
        }
        plugin.getCommand("kill").setExecutor(this);
    }

    private void loadCommands(){
        COMMANDS.put("wtf", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                wtfReaction(args);
            }
        });

        COMMANDS.put("nice", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                niceReaction(args);
            }
        });

        COMMANDS.put("?", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                questionReaction(args, (Player) sender);
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if(label.equalsIgnoreCase("kill") && args.length != 0){
            Player player = findPlayerByName(args[0]);
            if(args[0].equalsIgnoreCase("thomas")){
                player = findPlayerByName("jarkux");
                wtfReaction(new String[]{""});
                Bukkit.broadcastMessage("scrub got rekt");
            }
            if(player != null){
                player.setHealth(0.0D);
            }
        }



        if(!(sender instanceof Player)){
            sender.sendMessage("must be a Player to use this command");
            return true;
        }

        if(COMMANDS.containsKey(label)){
            COMMANDS.get(label).execute(sender, label, args);
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

    private void wtfReaction(String[] args){
        String name = args.length == 0 ? "" : args[0];
        Random r = new Random();
        int ra = r.nextInt();
        if(ra % 20 == 0){
            Bukkit.broadcastMessage(name + ", don't get Bushy");
        }else if (ra % 10 == 0){
            Bukkit.broadcastMessage(name + ", don't get pushy");
        }
        Bukkit.broadcastMessage("wtf " + name);
    }

    private void niceReaction(String[] args){
        String name = args.length == 0 ? "" : args[0];
        Bukkit.broadcastMessage("nice one " + name);
    }

    private void questionReaction(String[] args, Player player){
        String name = args.length == 0 ? "" : args[0];
        Player player2 = findPlayerByName(name);
        if(player2 != null){
            player.sendMessage("@" + name + " ?");
            player2.teleport(player);
        }else {
            Bukkit.broadcastMessage("@" + name + " ?");
        }
    }
}
