package to.joe.decapitation;

import net.minecraft.server.v1_4_5.ItemStack;
import net.minecraft.server.v1_4_5.NBTTagCompound;
import net.minecraft.server.v1_4_5.TileEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;

public class Head {

    private ItemStack s;

    public Head(CraftItemStack itemStack) {
        s = itemStack.getHandle();
    }

    public Head(CraftItemStack itemStack, Location l) {
        s = itemStack.getHandle();
        CraftWorld world = (CraftWorld) l.getWorld();
        NBTTagCompound compound = new NBTTagCompound();
        TileEntity e = world.getTileEntityAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        e.b(compound);
        setName(compound.getString("ExtraType"));
    }

    public boolean isNamed() {
        if (s.tag == null)
            return false;
        return s.tag.hasKey("SkullOwner");
    }

    public void setName(String name) {
        if (s.tag == null)
            s.tag = new NBTTagCompound();
        s.tag.setString("SkullOwner", name);
    }

    public String getName() {
        return s.tag.getString("SkullOwner");
    }

}
