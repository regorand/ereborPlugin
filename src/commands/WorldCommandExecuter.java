package commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import resources.Utilities;

import java.util.HashMap;

public class WorldCommandExecuter implements CommandExecutor {

    HashMap<String, CommandExecution> COMMANDS = new HashMap<>();


    public WorldCommandExecuter(JavaPlugin plugin){
        loadCommands();
        for(String key: COMMANDS.keySet()){
            plugin.getCommand(key).setExecutor(this);
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!isPlayer(sender)){
            sender.sendMessage("nur als Spieler anwendbar");
            return true;
        }

        if(COMMANDS.containsKey(label)){
            COMMANDS.get(label).execute(sender, label, args);
            return true;
        }
        return false;
    }

    private void loadCommands(){

        COMMANDS.put("killall", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                killall(((Player) sender).getWorld());
            }
        });

        COMMANDS.put("calc", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                if (args.length == 0) {
                    return;
                }
                Player player = (Player) sender;
                World world = player.getWorld();
                try {
                    int blockNr = Integer.valueOf(args[0]);

                    if (blockNr == 1) {
                        Utilities.setCalcBlock1(player.getTargetBlock(Utilities.airSet, 200));
                        player.sendMessage("Block 1 gesetzt");
                    } else if (blockNr == 2) {
                        Utilities.setCalcBlock2(player.getTargetBlock(Utilities.airSet, 200));
                        player.sendMessage("Block 2 gesetzt");
                    }
                } catch (NumberFormatException e) {
                    boolean countAir = args[0].equals("true");
                    calculateBlocks(world, countAir);
                }
            }
        });

        COMMANDS.put("wall", new CommandExecution() {
            @Override
            public void execute(CommandSender sender, String label, String[] args) {
                if (args.length == 0) {
                    return;
                }
                Player player = (Player) sender;
                if (args[0].equals("set")) {
                    if(args[1] != null){
                        setWallBlocks(args[1], player);
                    }
                } else if (args[0].equals("bau")) {
                    String type = args.length > 1 ? args[1] : "";



                    if(type.equals("smooth")){
                        String materialString = args.length > 2 ? args[2] : "";
                        Utilities.bauSmoothWall(materialString);
                    } else {
                        String direction = args.length > 2 ? args[2] : "";
                        String materialString = args.length > 3 ? args[3] : "";

                        buildRoughWall(direction, materialString);

                    }
                    Utilities.setWallBlocksNull();
                }
            }
        });

        COMMANDS.put("clearslope", new CommandExecution() {
                    @Override
                    public void execute(CommandSender sender, String label, String[] args) {
                        if (args.length == 0) {
                            return;
                        }
                        Player player = (Player) sender;
                        if (args[0].equals("set")) {
                            if(args[1] != null){
                                setWallBlocks(args[1], player);
                            }
                        } else if(args[0].equals("clear")){
                            Utilities.clearSlope();
                        }
                    }
                });

        COMMANDS.put("bau", new CommandExecution() {
                    @Override
                    public void execute(CommandSender sender, String label, String[] args) {
                        if (args.length == 0) {
                            return;
                        }
                        Player player = (Player) sender;
                        try {
                            int blockNr = Integer.valueOf(args[0]);
                            if (blockNr == 1) {
                                Utilities.setBlock1(player.getTargetBlock(Utilities.airSet, 200), player);
                                player.sendMessage("Block 1 gesetzt");
                            } else if (blockNr == 2) {
                                Utilities.setBlock2(player.getTargetBlock(Utilities.airSet, 200), player);
                                player.sendMessage("Block 2 gesetzt");
                            }


                        } catch (NumberFormatException e) {
                            Bukkit.broadcastMessage(Utilities.createMessage());
                            Material[] Materials = Material.values();
                            for (int i = 0; i < Materials.length; i++) {
                                if (Materials[i].toString().equals(args[0].toUpperCase())) {
                                    Material fillWith = Material.getMaterial(args[0].toUpperCase());
                                    if (Utilities.canFill(player)) {
                                        Utilities.fillArea(fillWith, player.getWorld(), player);
                                        Utilities.setBlock1(null, player);
                                        Utilities.setBlock2(null, player);
                                        return;
                                    }
                                    player.sendMessage("Es sind nicht beide Bloecke gesetzt");
                                }

                            }

                        }
                    }
                });
    }

    private boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }

    private void killall(World world){
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
    }



    private void calculateBlocks(World world, boolean countAir){
            Bukkit.broadcastMessage(Utilities.createMessage());
            try {
                Bukkit.broadcastMessage("Blöcke im bereich: " + Utilities.countBlocks(world, countAir));
            } catch (RuntimeException e2) {
                Bukkit.broadcastMessage("error mate, mach beide blöcke undso diesdas");
            }
            Utilities.setCalcBlock1(null);
            Utilities.setCalcBlock2(null);
    }

    private void buildRoughWall(String direction, String materialString){
        Material material = Utilities.getMaterialFromString(materialString);
        if(material == null) material = Material.STONE;
    }

    private void setWallBlocks(String blockIndex, Player player){
        try {
            int index = Integer.valueOf(blockIndex);
            if (index < 0 || index > 3) {
                player.sendMessage("bitte eine Zahl zwischen 0 und 3");
                return;
            }
            Utilities.setWallBlock(player.getTargetBlock(Utilities.airSet, 200), index);

        } catch (NumberFormatException e) {
            player.sendMessage("bitte eine Zahl als zweites argument bei set eingeben");
            return;
        }
    }
}
