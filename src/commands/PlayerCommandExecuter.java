package commands;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import resources.InventoryBar;
import resources.Utilities;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class PlayerCommandExecuter implements CommandExecutor {

    HashMap<String, CommandExecution> COMMANDS = new HashMap<>();

    public PlayerCommandExecuter(JavaPlugin plugin) {
        loadCommands();
        for (String key : COMMANDS.keySet()) {
            plugin.getCommand(key).setExecutor(this);
        }
    }

    private void loadCommands(){
        COMMANDS.put("flyingspeed", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                flyingSpeedCommand(args, (Player) sender);
            }
        });

        COMMANDS.put("walkingspeed", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                walkingSpeedCommand(args, (Player) sender);
            }
        });

        COMMANDS.put("inventory", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                inventoryCommand(args, (Player) sender);
            }
        });

        COMMANDS.put("tele", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                teleCommand((Player) sender);
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("must be a Player to use this command");
            return true;
        }
        Bukkit.broadcastMessage("new");
        if(COMMANDS.containsKey(label)){
            COMMANDS.get(label).execute(sender, label, args);
            return true;
        }

        return false;
    }

    private void flyingSpeedCommand(String[] args, Player player){
        if (args.length > 0) {
            player.setFlySpeed(Float.valueOf(args[0]) * (float) 0.2);
        } else {
            player.setFlySpeed(1 * (float) 0.2);
        }
        player.sendMessage("success");
    }

    private void walkingSpeedCommand(String[] args, Player player){
        if (args.length > 0) {
            player.setWalkSpeed(Float.valueOf(args[0]) * (float) 0.2);
        } else {
            player.setWalkSpeed(1 * (float) 0.2);
        }
        player.sendMessage("success");
    }

    private void inventoryCommand(String[] args, Player player){
        if (!(args.length > 1)) {
            Bukkit.broadcastMessage("/Inventory <mode> <name>");
            return;
        }
        if (args[0].equals("save")) {
            Utilities.addToInvs(new InventoryBar(player.getInventory().getContents(), player, args[1]));
            return;
        } else if (args[0].equals("load")) {
            ItemStack[] inv = Utilities.getInvBySaveName(args[1]);
            if (inv != null) {
                player.getInventory().setContents(inv);
                player.updateInventory();
            } else {
                Bukkit.broadcastMessage("Dieses Inventory wurde nicht gefunden");
            }

            return;
        }
        Bukkit.broadcastMessage("save or load as first Parameter <mode>");
    }

    private void teleCommand(Player player){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta im = stick.hasItemMeta() ? stick.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.STICK);
        im.setDisplayName("tele");
        stick.setItemMeta(im);
        player.getInventory().setItemInHand(stick);
    }
}
