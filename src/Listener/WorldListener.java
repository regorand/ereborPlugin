package Listener;


import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import javax.rmi.CORBA.Util;

public class WorldListener implements Listener {

    @EventHandler
    public void onRain(WeatherChangeEvent event){
        if(event.toWeatherState()){
            event.setCancelled(true);
            Bukkit.broadcastMessage("rekt get Rain!");
        }
    }
}
