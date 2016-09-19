package resources;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jan-Luca on 11.12.2015.
 */
public class InventoryBar {


    private ItemStack[] inventory;
    private Player player;
    private String name;

    public InventoryBar(ItemStack[] invToAdd, Player player, String name){
        this.inventory = invToAdd;
        this.player = player;
        this.name = name;
    }

    public ItemStack[] getInventory(){
        return inventory;
    }

    public String getName(){
        return name;
    }
}
