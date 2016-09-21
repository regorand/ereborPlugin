package Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import resources.Utilities;

import java.util.*;


public class PlayerListener implements Listener {

    private int teleDelay = 100;
    private long lastTeleport = System.currentTimeMillis();
    private Player lastPlayerToTeleport;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            ItemStack inHand = player.getItemInHand();
            if(inHand.getType().equals(Material.STICK) && inHand.getItemMeta().getDisplayName().equals("tele")){
                Set<Material> airSet = Utilities.airSet;
                List<Block> blocksInSight = player.getLineOfSight(airSet, 255);

                if(System.currentTimeMillis() - lastTeleport > teleDelay || !lastPlayerToTeleport.equals(player)){
                    Location newLoc = blocksInSight.get(blocksInSight.size() - 2).getLocation().add(0.5, -1, 0.5);
                    newLoc.setYaw(player.getLocation().getYaw());
                    newLoc.setPitch(player.getLocation().getPitch());
                    if(!airSet.contains(newLoc.add(0, 1, 0).getBlock().getType())){
                        newLoc = newLoc.add(0, -1, 0);
                    }
                    player.teleport(newLoc);
                    lastPlayerToTeleport = player;
                    lastTeleport = System.currentTimeMillis();
                }
            }
        }
    }
}
