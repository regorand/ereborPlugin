package commands;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import resources.InventoryBar;
import resources.Utilities;

import java.util.Random;
import java.util.Set;

/**
 * Created by jan-luca on 25.11.15.
 */
public class PlayerCommandExecuter implements CommandExecutor {
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;



        World world = player.getWorld();

        switch (label.toLowerCase()) {
            case "flyingspeed":

                if (args.length > 0) {
                    player.setFlySpeed(Float.valueOf(args[0]) * (float) 0.2);
                } else {
                    player.setFlySpeed(1 * (float) 0.2);
                }
                sender.sendMessage("success");

                return true;
            case "walkingspeed":

                if (args.length > 0) {
                    player.setWalkSpeed(Float.valueOf(args[0]) * (float) 0.2);
                } else {
                    player.setWalkSpeed(1 * (float) 0.2);
                }
                sender.sendMessage("success");

                return true;

            case "inventory":

                if(!(args.length > 1)){
                    Bukkit.broadcastMessage("/Inventory <mode> <name>");
                    return true;
                }
                if(args[0].equals("save")){
                    Utilities.addToInvs(new InventoryBar(player.getInventory().getContents(), player, args[1]));
                    return true;
                }else if(args[0].equals("load")){
                    ItemStack[] inv = Utilities.getInvBySaveName(args[1]);
                    if(inv != null){
                        player.getInventory().setContents(inv);
                        player.updateInventory();;
                    }else{
                        Bukkit.broadcastMessage("Dieses Inventory wurde nicht gefunden");
                    }

                    return true;
                }
                Bukkit.broadcastMessage("save or load as first Parameter <mode>");
                return true;

            case "bau":{
                if(args.length == 0){
                    return true;
                }
                try{
                    int blockNr = Integer.valueOf(args[0]);
                    Set<Material> airSet = Utilities.airSet;
                    if(blockNr == 1){
                        Utilities.setBlock1(player.getTargetBlock(airSet, 200));
                        Bukkit.broadcastMessage("Block 1 gesetzt");
                    }else if(blockNr == 2){
                        Utilities.setBlock2(player.getTargetBlock(airSet, 200));
                        Bukkit.broadcastMessage("Block 2 gesetzt");
                    }


                }catch (NumberFormatException e){
                    String[] actualMessage = {"keine zahl eingegeben", "KEINE ZAHL EINGEGEBEN", "Keine Zahl Eingegeben"};
                    String[] lelrekt = {":^)", ":v)", "lel", "", "", "", "", "kappa", "baka", "", "", "5/7"};
                    Random r = new Random();
                    String s = actualMessage[r.nextInt(actualMessage.length)] + " " +  lelrekt[r.nextInt(lelrekt.length)];
                    Bukkit.broadcastMessage(s );
                    Material[] Materials = Material.values();
                    for(int i = 0; i < Materials.length; i++){
                        if(Materials[i].toString().equals(args[0].toUpperCase())){
                            Material fillWith = Material.getMaterial(args[0].toUpperCase());
                            if(Utilities.canFill()){
                                Utilities.fillArea(fillWith, world);
                                Utilities.setBlock1(null);
                                Utilities.setBlock2(null);
                                return true;
                            }
                            Bukkit.broadcastMessage("Es sind nicht beide Bloecke gesetzt");
                            return true;
                        }

                    }

                }
            }

        }




        return false;
    }
}
