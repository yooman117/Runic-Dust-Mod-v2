package com.dustcore.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.dustcore.DustModTab;
import com.dustcore.api.DustItemManager;
import com.dustcore.config.DustContent;
import com.dustcore.util.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInk extends DustModItem {
	
	public static final int maxAmount = 32;
	
	private Icon bottle;
	private Icon[] main;
	private Icon[] sub;
	
	public ItemInk(int i)
    {
        super(i);
        setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(DustModTab.dustTab);
    }
	

    

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 5; i < 1000; ++i)
        {
        	if(DustItemManager.getColors()[i] != null){
                par3List.add(getInk(i));
        	}
        }
    }
    

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
    	int dustID = getDustID(itemstack);
    	String id = DustItemManager.getIDS()[dustID];
    	if(id != null) return "tile.ink." + DustItemManager.idsRemote[dustID];
        return "tile.ink";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
    	int meta = stack.getItemDamage();
    	int id = getDustID(meta);
    	if(pass == 0) return 16777215;
    	return pass == 1 ? DustItemManager.getPrimaryColor(id) : DustItemManager.getSecondaryColor(id);
    }


    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int meta, int rend)
    {
    	if(rend == 0) return bottle;
    	
    	int off = (maxAmount-1)-meta%maxAmount;
    	
    	off /= (maxAmount/8);
    	
    	if(rend == 1){
    		return main[off];
    	}else
    		return sub[off];
    }
    
    public static ItemStack getInk(int dustID){
    	return new ItemStack(DustContent.ink.itemID, 1, dustID*maxAmount + maxAmount-1);
    }
    
    public static int getDustID(ItemStack item){
    	return getDustID(item.getItemDamage());
    }
    public static int getDustID(int meta){
    	return (meta - (meta%maxAmount)) / maxAmount; 
    }
    
    public static boolean reduce(EntityPlayer p, ItemStack item, int amt){
    	if(p.capabilities.isCreativeMode) return true;
    	int fill = item.getItemDamage()%maxAmount;
    	int level = item.getItemDamage() - fill;
    	if(fill < amt) return false;
    	fill -= amt;
    	if(fill == 0) {
    		item.itemID = Item.glassBottle.itemID;
    		item.setItemDamage(0);
    	}else
    		item.setItemDamage(level + fill);
    	return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) 
    {
    	this.bottle = iconRegister.registerIcon(References.spritePath + "inkBottle");
    	main = new Icon[8];
    	sub = new Icon[8];
    	for(int i = 0; i < main.length; i++){
    		main[i] = iconRegister.registerIcon(References.spritePath + "ink_main_" + i);
    		sub[i] = iconRegister.registerIcon(References.spritePath + "ink_sub_" + i); 
    	}
    }

}
