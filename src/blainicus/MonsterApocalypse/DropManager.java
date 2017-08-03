package blainicus.MonsterApocalypse;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class DropManager {
	List<ItemStack> drops;
	List<String> droplist;
	MonsterApocalypse plugin;
	String itemstring;
	int countindex, chanceindex, dataindex;
	double chance;
	int count;
	byte data;
	Material mat;
	public DropManager(MonsterApocalypse instance){
		plugin=instance;
		drops=new ArrayList<ItemStack>();
		droplist=new ArrayList<String>();
	}
	@SuppressWarnings("deprecation")
	public void getDrops(Entity ent){
		drops.clear();
		droplist=plugin.getMobDrops(ent);
		for(int n=0; n<droplist.size(); n++){
			itemstring=droplist.get(n);
			if(itemstring.startsWith("item"))continue;
			countindex=itemstring.indexOf(":")+1;
			chanceindex=itemstring.indexOf(":", countindex+1)+1;
			try{
			mat=Material.getMaterial(Integer.parseInt(itemstring.substring(0, countindex-1)));
			}catch(NumberFormatException epz){
				mat=Material.getMaterial(itemstring.substring(0, countindex-1));
			}
			count=Integer.parseInt(itemstring.substring(countindex, chanceindex-1));
			chance=Double.parseDouble(itemstring.substring(chanceindex));
			if(plugin.spawnrand.nextDouble()<=chance/100d){
			drops.add(new ItemStack(mat, count));
			}
		}
	}
	public void addDrops(Entity ent, List<ItemStack> list){
		try{
		getDrops(ent);
		}
		catch(NullPointerException e){
			System.out.println("Monster Apocalypse: Invalid drop setting "+itemstring+" for entity "+ent.getClass().getSimpleName()+". Please use a valid CraftBukkit enum.");
			return;
		}
		for(int n=0; n<drops.size(); n++){
			try{
			list.add(drops.get(n));}
			catch(UnsupportedOperationException e){}
		}
	}
	public void checkdrops(){
		for (int n=0; n<plugin.moblist.size(); n++){
			if(plugin.moblist.get(n).name=="TamedWolf"){continue;}
		try{
			getDrops(plugin.moblist.get(n).name);
			}
			catch(NullPointerException e){
				System.out.println("Monster Apocalypse: Invalid drop setting "+itemstring+" for entity "+plugin.moblist.get(n).name+ ". Please use a valid CraftBukkit enum.");
				return;
			}
			
		}
	}
	@SuppressWarnings("deprecation")
	public void getDrops(String name){
		drops.clear();
		droplist=plugin.getMobDrops(name);
		for(int n=0; n<droplist.size(); n++){
			try{
			itemstring=droplist.get(n);
			if(itemstring.startsWith("item"))continue;
			countindex=itemstring.indexOf(":")+1;
			chanceindex=itemstring.indexOf(":", countindex+1)+1;
			dataindex=itemstring.indexOf(":", chanceindex+1)+1;
			if(dataindex==0){
			mat=Material.getMaterial(itemstring.substring(0, countindex-1));
			count=Integer.parseInt(itemstring.substring(countindex, chanceindex-1));
			chance=Double.parseDouble(itemstring.substring(chanceindex));
			if(plugin.spawnrand.nextDouble()<=chance/100d){
			drops.add(new ItemStack(mat, count));
			}}
			else{
				mat=Material.getMaterial(itemstring.substring(0, countindex-1));
				count=Integer.parseInt(itemstring.substring(countindex, chanceindex-1));
				chance=Double.parseDouble(itemstring.substring(chanceindex, dataindex-1));
				//				System.out.println(chanceindex+" "+dataindex);
				data=Byte.parseByte(itemstring.substring(dataindex));
				if(plugin.spawnrand.nextDouble()<=chance/100d){
				drops.add(new ItemStack(mat, count, (short)0, data));
				}
			}}
			catch(Exception e){System.out.println("Monster Apocalypse: Error adding drop to drop list of "+name+", check your drops for errors.");}
		}
	}
}