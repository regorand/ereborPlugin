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

        new WorldCommandExecuter(this);
        new PlayerCommandExecuter(this);
        new ReactionCommandExecuter(this);

        Utilities.init();
    }
}