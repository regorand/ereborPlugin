package resources;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Utilities {

    public static Block block1 = null;
    public static Block block2 = null;

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

    public static void bauWall(Material material) {


    }

    public static List<Block> getLineBetweenBlocks(Block b1, Block b2) {
        World world = b1.getWorld();

        ArrayList<Block> returnList = new ArrayList<>();
        returnList.add(b1);
        if (b1.getLocation() == b2.getLocation()) {
            return returnList;
        }
        returnList.add(b2);


        Location l1 = b1.getLocation(),
                l2 = b2.getLocation();

        int diff_X = Math.abs(l2.getBlockX() - l2.getBlockX()),
                diff_Y = Math.abs(l1.getBlockY() - l2.getBlockY()),
                diff_Z = Math.abs(l1.getBlockZ() - l2.getBlockZ());



        //get stepsize (greatest diff of all 3 coords)
        //stepsize is =< 1 -> stepsize = 1 wenn x groeﬂte stepsize
        //ausnahme fall wenn diff_X == 0





        //create mx+b params for both y and z


        //might change from here

        //special case if x diff is zero


        //calculate blocks

        return null;
    }


    @Deprecated
    public static List<Block> getLineBetweenBlocks_old(Block b1, Block b2, int version) {
        World world = b1.getWorld();

        ArrayList<Block> returnList = new ArrayList<>();
        returnList.add(b1);
        if (b1.getLocation() == b2.getLocation()) {
            return returnList;
        }
        returnList.add(b2);

        Location l1 = null, l2 = null;

        switch (version) {
            case 1:
                l1 = b1.getLocation();
                l2 = b2.getLocation();
                break;
            case 2:
                l1 = b1.getLocation().add(0.5, 0.5, 0.5);
                l2 = b2.getLocation().add(0.5, 0.5, 0.5);
                break;
            case 3:
                l1 = b1.getLocation().add(-0.5, -0.5, -0.5);
                l2 = b2.getLocation().add(-0.5, -0.5, -0.5);
        }

        //define m and b for linear function form: f(x) = mx + b
        //for both y and z
        double m_y = (l2.getY() - l1.getY()) / (l2.getX() - l1.getX()),
                m_z = (l2.getZ() - l1.getZ()) / (l2.getX() - l1.getX());

        double b_y = l1.getY() - (l1.getBlockX() * m_y),
                b_z = l1.getZ() - (l1.getBlockX() * m_z);

        int smallerX = (int) Math.min(l1.getX(), l2.getX()),
                largerX = (int) Math.max(l1.getX(), l2.getX()),
                smallerY = (int) Math.min(l1.getY(), l2.getY()),
                largerY = (int) Math.max(l1.getY(), l2.getY()),
                smallerZ = (int) Math.min(l1.getZ(), l2.getZ()),
                largerZ = (int) Math.max(l1.getZ(), l2.getZ());


        //adding one because if the difference is 0
        // we have a width of one x


        double diff_x = largerX - smallerX,
                diff_y = largerY - smallerY,
                diff_z = largerZ - smallerZ,
                diffPerX;
        Block b;

        //special case for line with 2 diffs set to 0 -> straight line of blocks
        if (diff_x == 0 && (diff_y == 0 || diff_z == 0)) {
            Bukkit.broadcastMessage("two are 0");
            //goes in here if difference of x and one more coordinate are 0
            if (diff_y == 0) {
                //in the case the y diff is zero
                for (int z = smallerZ; z < largerZ; z++) {
                    b = world.getBlockAt(l1.getBlockX(), l1.getBlockY(), z);
                    if (!returnList.contains(b)) {
                        returnList.add(b);
                    }
                }
            } else {
                //the only case should be diff_z == 0
                for (int y = smallerY; y < largerY; y++) {
                    b = world.getBlockAt(l1.getBlockX(), y, l1.getBlockZ());
                    if (!returnList.contains(b)) {
                        returnList.add(b);
                    }
                }
            }
            return returnList;
        }

        if (diff_x >= Math.max(diff_y, diff_z)) {
            diffPerX = 1;
        } else {
            diffPerX = Math.max(diff_y, diff_z) / (diff_x + 1);
        }


        for (double x = smallerX; x < largerX + 1; x++) {
            for (double diff = 0; diff < diffPerX; diff++) {
                double ratio = diff / diffPerX;
                b = world.getBlockAt(
                        (int) x,
                        (int) Math.round(m_y * (x + ratio) + b_y),
                        (int) Math.round(m_z * (x + ratio) + b_z));
                if (!returnList.contains(b)) {
                    returnList.add(b);
                }
            }

        }

        return returnList;

        //test if Location retains comma values as coord. (probably yes)

        //test if Location has method to get List<Block> between two Locations (probably not, implement yourself)
        //  -> calc line by function for: x over y AND x over z, then create Block from those and add to List
    }


    public static boolean canFill() {
        return block1 != null && block2 != null;
    }

    public static Block getBlock1() {
        return block1;
    }

    public static Block getBlock2() {
        return block2;
    }

    public static void setBlock1(Block block) {
        block1 = block;
    }

    public static void setBlock2(Block block) {
        block2 = block;
    }

    public static ItemStack[] getInvBySaveName(String name) {
        for (int i = 0; i < Invs.size(); i++) {
            if (Invs.get(i).getName().equals(name)) {
                return Invs.get(i).getInventory();
            }
        }
        return null;
    }

    public static int countBlocks(World world) throws RuntimeException {
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
                    if (!world.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
                        blocks++;
                    }
                }
            }
        }
        return blocks;
    }

    public static void fillArea(Material material, World world) {
        Location loc1 = block1.getLocation();
        Location loc2 = block2.getLocation();

        double smallerX = Math.min(loc1.getX(), loc2.getX());
        double smallerY = Math.min(loc1.getY(), loc2.getY());
        double smallerZ = Math.min(loc1.getZ(), loc2.getZ());
        double biggerX = Math.max(loc1.getX(), loc2.getX());
        double biggerY = Math.max(loc1.getY(), loc2.getY());
        double biggerZ = Math.max(loc1.getZ(), loc2.getZ());

        Block block;

        for (int x = (int) smallerX; x <= biggerX; x++) {
            for (int y = (int) smallerY; y <= biggerY; y++) {
                for (int z = (int) smallerZ; z <= biggerZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
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
