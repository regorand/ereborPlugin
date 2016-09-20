package commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import resources.Utilities;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by jan-luca on 25.11.15.
 */
public class WorldCommandExecuter implements CommandExecutor {

    public Block b1 = null;
    public Block b2 = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        Player player = (Player) sender;
        World world = player.getWorld();

        switch (label) {
            case "test":
                int counter = 0;
                for(Entity e: world.getEntities()){

                    if(!Utilities.notRemovable.contains(e.getType())){
                        counter++;
                        EntityType eType = e.getType();
                        Bukkit.broadcastMessage("rekt get " + eType.toString());
                        e.remove();
                    }

                }

                Bukkit.broadcastMessage(counter + " Scrubs got rekt");
                Bukkit.broadcastMessage("lel");
                return true;
            case "calc":
                if(args.length != 0) {

                    try {
                        int blockNr = Integer.valueOf(args[0]);

                        Set<Material> airSet = Utilities.airSet;
                        if (blockNr == 1) {
                            Utilities.setCalcBlock1(player.getTargetBlock(airSet, 200));
                            Bukkit.broadcastMessage("Block 1 gesetzt");
                        } else if (blockNr == 2) {
                            Utilities.setCalcBlock2(player.getTargetBlock(airSet, 200));
                            Bukkit.broadcastMessage("Block 2 gesetzt");
                        }
                    } catch (NumberFormatException e) {
                        Bukkit.broadcastMessage("bitte eine Zahl eingeben");
                    }
                }else {
                    Bukkit.broadcastMessage(Utilities.createMessage());
                    try {
                        Bukkit.broadcastMessage("Blöcke im bereich: " + Utilities.countBlocks(world));
                    } catch (RuntimeException e2) {
                        Bukkit.broadcastMessage("error mate, mach beide blöcke undso diesdas");
                    }

                    Utilities.setCalcBlock1(null);
                    Utilities.setCalcBlock2(null);
                    return true;
                }

            case "wand":

                if(args.length == 0){
                    return true;
                }

                if(args[0].equals("set")){
                    try{
                        int index = Integer.valueOf(args[1]);
                        if(index < 0 || index > 3){
                            Bukkit.broadcastMessage("bitte eine Zahl zwischen 0 und 3");
                            return true;
                        }
                        Set<Material> airSet = Utilities.airSet;
                        Utilities.setWallBlock(player.getTargetBlock(airSet, 200), index);

                    }catch (NumberFormatException e){
                        Bukkit.broadcastMessage("bitte eine Zahl als zweites argument bei set eingeben");
                        return true;
                    }
                }else if(args[0].equals("bau")){
                    Material material = null;

                    Bukkit.broadcastMessage("finds material");
                    if(args.length > 1){
                        Material[] materials = Material.values();
                        for(Material m: materials){
                            if(m.toString().equals(args[1].toUpperCase())){
                                material = m;
                                break;
                            }
                        }
                    }
                    if(material == null){
                        material = Material.STONE;
                    }

                    Bukkit.broadcastMessage("found material");

                    Utilities.bauWall(material);
            }

                return true;



            case "testline":
                if(args.length == 0){
                    return true;
                }

                if(args[0].equals("set")){
                    try{
                        int index = Integer.valueOf(args[1]);
                        if(index < 1 || index > 2){
                            Bukkit.broadcastMessage("bitte eine Zahl zwischen 1 und 2");
                            return true;
                        }
                        Set<Material> airSet = Utilities.airSet;
                        if(index == 1){
                            b1 = player.getTargetBlock(airSet, 200);
                            Bukkit.broadcastMessage("set Block 1");
                        }else if(index == 2){
                            b2 = player.getTargetBlock(airSet, 200);
                            Bukkit.broadcastMessage("set Block 2");
                        }

                    }catch (NumberFormatException e){
                        Bukkit.broadcastMessage("bitte eine Zahl als zweites argument bei set eingeben");
                        return true;
                    }
                }else if(args[0].equals("bau")){
                    List<Block> l = Utilities.getLineBetweenBlocks(b1, b2);

                    Bukkit.broadcastMessage(String.valueOf(l.size()));
                    for(Block b: l){
                        b.setType(Material.GOLD_BLOCK);
                    }
                }


            default:
                return false;
        }
    }
}
