package main;

import Listener.BlockListener;
import Listener.PlayerListener;
import Listener.PlayerServerEvent;
import Listener.WorldListener;
import commands.PlayerCommandExecuter;
import commands.ReactionCommandExecuter;
import commands.WorldCommandExecuter;
import org.bukkit.plugin.java.JavaPlugin;
import resources.Utilities;


public class MyPlugin extends JavaPlugin {




    public void onEnable() {

        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerServerEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        WorldCommandExecuter worldCommandExecuter = new WorldCommandExecuter();
        PlayerCommandExecuter playerCommandExecuter = new PlayerCommandExecuter();
        ReactionCommandExecuter reactionCommandExecuter = new ReactionCommandExecuter(this);

        getCommand("wtf").setExecutor(reactionCommandExecuter);
        getCommand("nice").setExecutor(reactionCommandExecuter);

        getCommand("?").setExecutor(reactionCommandExecuter);

        getCommand("flyingspeed").setExecutor(playerCommandExecuter);
        getCommand("walkingspeed").setExecutor(playerCommandExecuter);
        getCommand("inventory").setExecutor(playerCommandExecuter);
        getCommand("bau").setExecutor(playerCommandExecuter);
        getCommand("test").setExecutor(worldCommandExecuter);
        getCommand("calc").setExecutor(worldCommandExecuter);
        getCommand("wand").setExecutor(worldCommandExecuter);
        getCommand("testline").setExecutor(worldCommandExecuter);
        Utilities.init();
    }
}