package regionPreserve;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;

public class RegionPreserveCommand {
	
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args, JavaPlugin p)
	{
		if (sender instanceof Player) {
		if(sender.hasPermission("rp.edit") || sender.isOp()) 
		{
			if(args.length > 0)
			{
				// find out what command was executed
				if(args[0].equalsIgnoreCase("create"))
				{
					// /rp create <regionname> command
					if(args.length == 2)
					{
						RegionEdit.CreateRegion((Player) sender, args[1], p);
					}
					else
					{
						// wrong amount of args
						sender.sendMessage(ChatColor.RED + "Too few/many parameters!");
					}
				}
				else if(args[0].equalsIgnoreCase("delete"))
				{
					// /rp delete <regionname> command
					if(args.length == 2)
					{
						RegionEdit.DeleteRegion((Player) sender, args[1]);
					}
					else
					{
						// wrong amount of args
						sender.sendMessage(ChatColor.RED + "Too few/many parameters!");
					}
				}
				else if(args[0].equalsIgnoreCase("max"))
				{
					RegionEdit.MaxOutY((Player) sender);
				}
				else if(args[0].equalsIgnoreCase("flag"))
				{
					if(args.length != 4)
					{
						sender.sendMessage(ChatColor.RED + "Too few/many parameters!");
					}
					else
					{
						// right amount of args
						String action = args[2];
						String regionName = args[1];
						ActiveRegion region = null;
						String flag = args[3];
						for (ActiveRegion r : RegionPreserve.regions)
						{
							if(r.name.equalsIgnoreCase(regionName))
							{
								region = r;
							}
						}				
						if(region == null)
						{
							sender.sendMessage(ChatColor.RED + "That region does not exist!");
							return;
						}
						if(action.equalsIgnoreCase("add"))
						{
							try
							{
								region.flags.add(Flags.flagFromString(flag));
								List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
								for(ActiveRegion r : RegionPreserve.regions)
								{
									if(r.name.equals(region.name))
									{
										regions.add(region);
									}
									else
									{
										regions.add(r);
									}
								}
								RegionLoading.SaveRegions(regions);
								RegionLoading.LoadRegions();
								sender.sendMessage(ChatColor.GREEN + "Region flags updated!");
							}
							catch(Exception ex)
							{
								sender.sendMessage(ChatColor.RED + "That is not one of the supported flags!");
								sender.sendMessage(ChatColor.RED + "Supported Flags: use, build, burn, fade, grow, leafdecay, explode, bucket, monsterspawning, animalspawning, commands, mobdespawn, pvp");
							}
						}
						else if(action.equalsIgnoreCase("remove"))
						{
							try
							{
								region.flags.remove(Flags.flagFromString(flag));
								List<ActiveRegion> regions = new ArrayList<ActiveRegion>();
								for(ActiveRegion r : RegionPreserve.regions)
								{
									if(r.name.equals(region.name))
									{
										regions.add(region);
									}
									else
									{
										regions.add(r);
									}
								}
								RegionLoading.SaveRegions(regions);
								RegionLoading.LoadRegions();
								sender.sendMessage(ChatColor.GREEN + "Region flags updated!");
							}
							catch(Exception ex)
							{
								sender.sendMessage(ChatColor.RED + "That is not one of the supported flags!");
								sender.sendMessage(ChatColor.RED + "Supported Flags: use, build, burn, fade, grow, leafdecay, explode, bucket, monsterspawning, animalspawning, commands, mobdespawn, pvp");
							}
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "Invalid usage! Choose add or remove.");
							return;
						}
					}
				}
				else if(args[0].equalsIgnoreCase("reload"))
				{
					p.reloadConfig();
					RegionPreserve.regions = RegionLoading.LoadRegions();
					System.out.print("[RegionPreserve] RegionPreserve reloaded!");
					sender.sendMessage(ChatColor.GREEN + "RegionPreserve has been reloaded!");
				}
				else if(args[0].equalsIgnoreCase("info"))
				{
					boolean inRegion = false;
					for (ActiveRegion r : RegionPreserve.regions)
					{
						if(r.isLocationInRegion(((Player) sender).getLocation()))
						{
							sender.sendMessage(ChatColor.GREEN + "You are currently in the region '" + r.name + "'.");
							sender.sendMessage(ChatColor.GREEN + "Region flags: '" + Joiner.on(", ").join(r.flags) + "'.");
							inRegion = true;
						}
					}
					if(!inRegion)
					{
						sender.sendMessage(ChatColor.RED + "You are not in any region!");
					}
				}
				else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
				{
					// display help
					showHelp(sender);
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "This is not a valid RegionPreserve command!");
				}
			}
			else
			{
				// display help
				showHelp(sender);
			}
		}
		else
		{
			// user does not have permission
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
		}
	}
	else
	{
	 sender.sendMessage(ChatColor.RED + "You must be a player to edit RegionPreserve regions!");		
	}
	}
	
	public static void showHelp(CommandSender sender)
	{
		  sender.sendMessage(ChatColor.BLUE + "---------- " + ChatColor.WHITE + "Region Preserve Help" + ChatColor.BLUE + " -------------");
		  sender.sendMessage(ChatColor.BLUE + "/rp create [name] " + ChatColor.WHITE + "- Creates a region from two selected points");
		  sender.sendMessage(ChatColor.BLUE + "/rp delete [name] " + ChatColor.WHITE + "- Deletes a region");
		  sender.sendMessage(ChatColor.BLUE + "/rp max " + ChatColor.WHITE + "- Maxes the current selection from sky to bedrock");
		  sender.sendMessage(ChatColor.BLUE + "/rp flag [region] [add/remove] [flag] " + ChatColor.WHITE + "- Add or remove flags.");
		  sender.sendMessage(ChatColor.BLUE + "/rp info " + ChatColor.WHITE + "- Displays region information");
		  sender.sendMessage(ChatColor.BLUE + "/rp reload " + ChatColor.WHITE + "- Reloads the RegionPreserve config file");
		  sender.sendMessage(ChatColor.BLUE + "/rp help " + ChatColor.WHITE + "- Displays this message");
		  sender.sendMessage(ChatColor.BLUE + "-------------------------------------------");
	}

}
