package resources;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class Utilities {

    public static HashMap<String, Block> bauBlocks = new HashMap<>();

    public static HashMap<String, Block> copyBlocks = new HashMap<>();

    public static void setCalcBlock1(Block calcBlock1) {
        Utilities.calcBlock1 = calcBlock1;
    }

    public static void setCalcBlock2(Block calcBlock2) {
        Utilities.calcBlock2 = calcBlock2;
    }

    public static Block calcBlock1 = null;
    public static Block calcBlock2 = null;


    public static Block[] wallPoints = new Block[4];

    public static ArrayList<EntityType> notRemovable = new ArrayList<>();

    public static Set<Material> airSet = new HashSet<>();


    private static ArrayList<InventoryBar> Invs = new ArrayList<>();


    public static void addToInvs(InventoryBar inventoryBar) {
        Invs.add(inventoryBar);
    }

    public static void init() {


        airSet.add(Material.AIR);
        airSet.add(Material.WATER);
        airSet.add(Material.STATIONARY_WATER);
        airSet.add(Material.TORCH);

        notRemovable.add(EntityType.ARMOR_STAND);
        notRemovable.add(EntityType.PLAYER);
        notRemovable.add(EntityType.COMPLEX_PART);
        notRemovable.add(EntityType.ITEM_FRAME);
        notRemovable.add(EntityType.PAINTING);
        notRemovable.add(EntityType.WITHER_SKULL);
    }

    public static void setWallBlock(Block block, int corner) {
        //corners:
        //0 - bottom left
        //1 - bottom right
        //2 - top right
        //3 - top left
        Bukkit.broadcastMessage("set block " + corner);
        wallPoints[corner] = block;
    }

    public static void setWallBlocksNull(){
        for(Block b: wallPoints){
            b = null;
        }
    }

    public static void setBauBlocks(int index, Block block, Player player){
        String key = player.getDisplayName() + "_" + index;
        bauBlocks.put(key, block);
    }

    public static void bauSmoothWall(String materialString) {
        Material material = Utilities.getMaterialFromString(materialString);
        if(material == null) material = Material.STONE;

        ArrayList<Block> leftColumn = getLineBetweenBlocks(wallPoints[0], wallPoints[3]),
                rightColumn = getLineBetweenBlocks(wallPoints[1], wallPoints[2]);

        boolean leftIsSmaller = leftColumn.size() < rightColumn.size();
        int largerSize = Math.max(leftColumn.size(), rightColumn.size()),
            smallerSize = Math.min(leftColumn.size(), rightColumn.size()) - 1,
                leftIndex, rightIndex;

        for(int i = 0; i < smallerSize; i++){
            for(Block b: getLineBetweenBlocks(leftColumn.get(i), rightColumn.get(i))){
                b.setType(material);            }
        }

        for(int i = smallerSize; i < largerSize; i++){
            for(Block b: getLineBetweenBlocks(leftColumn.get(leftIsSmaller ? smallerSize : i), rightColumn.get(leftIsSmaller ?  i : smallerSize))){
                b.setType(material);
            }
        }
    }

    public static void clearSlope(){
        ArrayList<Block> leftColumn = getLineBetweenBlocks(wallPoints[0], wallPoints[3]),
                rightColumn = getLineBetweenBlocks(wallPoints[1], wallPoints[2]);

        int maxY = Math.max(
                Math.max(
                        Math.max(
                                wallPoints[0].getLocation().getBlockY(),
                                wallPoints[1].getLocation().getBlockY()),
                        wallPoints[2].getLocation().getBlockY()),
                wallPoints[3].getLocation().getBlockY());

        boolean leftIsSmaller = leftColumn.size() < rightColumn.size();
        int largerSize = Math.max(leftColumn.size(), rightColumn.size()),
                smallerSize = Math.min(leftColumn.size(), rightColumn.size()) - 1,
                leftIndex, rightIndex;

        for(int i = 0; i < smallerSize; i++){
            for(Block b: getLineBetweenBlocks(leftColumn.get(i), rightColumn.get(i))){
                b.setType(Material.AIR);
                while(b.getY() <= maxY){
                    b = b.getLocation().add(0, 1, 0).getBlock();
                    b.setType(Material.AIR);
                }
            }
        }

        for(int i = smallerSize; i < largerSize; i++){
            for(Block b: getLineBetweenBlocks(leftColumn.get(leftIsSmaller ? smallerSize : i), rightColumn.get(leftIsSmaller ?  i : smallerSize))){
                b.setType(Material.AIR);
                while(b.getY() <= maxY){
                    b = b.getLocation().add(0, 1, 0).getBlock();
                    b.setType(Material.AIR);
                }
            }
        }
    }

    public static ArrayList<Block> getLineBetweenBlocks(Block b1, Block b2) {
        World world = b1.getWorld();

        ArrayList<Block> returnList = new ArrayList<>();
        returnList.add(b1);
        if (b1.getLocation() == b2.getLocation()) {
            return returnList;
        }
        returnList.add(b2);

        Location l1 = b1.getLocation(),
                l2 = b2.getLocation();

        double smallX = Math.min(l1.getX(), l2.getX()),
                largeX = Math.max(l1.getX(), l2.getX()),
                smallY = Math.min(l1.getY(), l2.getY()),
                largeY = Math.max(l1.getY(), l2.getY()),
                smallZ = Math.min(l1.getZ(), l2.getZ()),
                largeZ = Math.max(l1.getZ(), l2.getZ()),
                diff_X =  largeX - smallX,
                diff_Y = largeY - smallY,
                diff_Z = largeZ - smallZ;

        if(diff_X == 0){
            int x = l1.getBlockX();
            if(diff_Z == 0){
                int z = l1.getBlockZ();
                for(int y = Math.min(l1.getBlockY(), l2.getBlockY()); y < Math.max(l1.getY(), l2.getY()); y++){
                    Block block = world.getBlockAt(x, y, z);
                    if(!returnList.contains(block)){
                        returnList.add(block);
                    }
                }
            }else{
                double m = (l2.getY() - l1.getY()) / (l2.getZ() - l1.getZ()),
                        b = l1.getY() - (l1.getZ() * m);
                double stepsize = 1;
                if(diff_Y > diff_Z){
                    stepsize = diff_Z / diff_Y;
                }

                for(double z = smallZ; z < largeZ; z += stepsize){
                    Block block = world.getBlockAt(x, (int) Math.round(m*z + b), (int) Math.round(z));
                    if(!returnList.contains(block)){
                        returnList.add(block);
                    }
                }
            }
        }else{
            double m_y = (l2.getY() - l1.getY()) / (l2.getX() - l1.getX()),
                    m_z = (l2.getZ() - l1.getZ()) / (l2.getX() - l1.getX());

            double b_y = l1.getY() - (l1.getBlockX() * m_y),
                    b_z = l1.getZ() - (l1.getBlockX() * m_z);

            double stepsize = 1;
            if(diff_Z > diff_X || diff_Y > diff_X){
                if(diff_Y > diff_Z){
                    stepsize = diff_X / diff_Y;
                }else {
                    stepsize = diff_X / diff_Z;
                }
            }

            for(double x = smallX; x < largeX; x += stepsize){
                Block block = world.getBlockAt((int) Math.round(x),
                        (int) Math.round(m_y * x + b_y),
                        (int) Math.round(m_z * x + b_z));
                if(!returnList.contains(block)){
                    returnList.add(block);
                }
            }
        }


        return returnList;
    }


    public static boolean canFill(Player player) {
        return bauBlocks.get(player.getDisplayName() + "_1") != null &&
                bauBlocks.get(player.getDisplayName() + "_2") != null;
    }

    public static Block getBauBlock(int index, Player player) {
        return bauBlocks.get(player.getDisplayName() + "_" + index);
    }

    public static Block getCopyBlock(int index, Player player) {
        return copyBlocks.get(player.getDisplayName() + "_" + index);
    }


    public static void fillArea(Material material, World world, Player player) {
        Location loc1 = getBauBlock(1, player).getLocation();
        Location loc2 = getBauBlock(2, player).getLocation();

        double smallerX = Math.min(loc1.getX(), loc2.getX());
        double smallerY = Math.min(loc1.getY(), loc2.getY());
        double smallerZ = Math.min(loc1.getZ(), loc2.getZ());
        double biggerX = Math.max(loc1.getX(), loc2.getX());
        double biggerY = Math.max(loc1.getY(), loc2.getY());
        double biggerZ = Math.max(loc1.getZ(), loc2.getZ());

        for (int x = (int) smallerX; x <= biggerX; x++) {
            for (int y = (int) smallerY; y <= biggerY; y++) {
                for (int z = (int) smallerZ; z <= biggerZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    public static ItemStack[] getInvBySaveName(String name) {
        for (int i = 0; i < Invs.size(); i++) {
            if (Invs.get(i).getName().equals(name)) {
                return Invs.get(i).getInventory();
            }
        }
        return null;
    }

    public static String createMessage(){
        String[] actualMessage = {"keine zahl eingegeben", "KEINE ZAHL EINGEGEBEN", "Keine Zahl Eingegeben"};
        String[] lelrekt = {":^)", ":v)", "lel", ":3", "", "", "", "kappa", "baka", "", "", "5/7"};
        Random r = new Random();
        return actualMessage[r.nextInt(actualMessage.length)] + " " +  lelrekt[r.nextInt(lelrekt.length)];
    }

    public static int countBlocks(World world, boolean countAir) throws RuntimeException {
        if (calcBlock1 == null || calcBlock2 == null) {
            throw new RuntimeException();
        }
        Location loc1 = calcBlock1.getLocation();
        Location loc2 = calcBlock2.getLocation();


        double smallerX = Math.min(loc1.getX(), loc2.getX());
        double smallerY = Math.min(loc1.getY(), loc2.getY());
        double smallerZ = Math.min(loc1.getZ(), loc2.getZ());
        double biggerX = Math.max(loc1.getX(), loc2.getX());
        double biggerY = Math.max(loc1.getY(), loc2.getY());
        double biggerZ = Math.max(loc1.getZ(), loc2.getZ());

        Block block;
        int blocks = 0;

        for (int x = (int) smallerX; x <= biggerX; x++) {
            for (int y = (int) smallerY; y <= biggerY; y++) {
                for (int z = (int) smallerZ; z <= biggerZ; z++) {
                    if (countAir || !world.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
                        blocks++;
                    }
                }
            }
        }
        return blocks;
    }

    public static Material getMaterialFromString(String materialSting){
        for(Material m: Material.values()){
            if(m.toString().equals(materialSting.toUpperCase())){
                return m;
            }
        }
        return null;
    }


    public static Block relativeToPlayer(final int distance, final boolean behind, final float yaw, final Block block) {
        float rotation = Math.abs(yaw);

        Block returnBlock = block;

        if (behind) {
            if (rotation >= 292.5 || rotation < 67.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(0, 0, -distance));
            } else if (112.5 <= rotation && rotation < 247.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(0, 0, distance));
            }
            if (22.5 <= rotation && rotation < 157.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(-distance, 0, 0));
            } else if (202.5 <= rotation && rotation < 337.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(distance, 0, 0));
            }
        } else {
            if (rotation >= 292.5 || rotation < 67.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(0, 0, distance));
            } else if (112.5 <= rotation && rotation < 247.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(0, 0, -distance));
            }
            if (22.5 <= rotation && rotation < 157.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(distance, 0, 0));
            } else if (202.5 <= rotation && rotation < 337.5) {
                returnBlock = returnBlock.getWorld().getBlockAt(returnBlock.getLocation().add(-distance, 0, 0));
            }
        }
        return returnBlock;
    }
}
