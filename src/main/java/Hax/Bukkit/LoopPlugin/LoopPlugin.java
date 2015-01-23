package Hax.Bukkit.LoopPlugin;
 
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
 
public final class LoopPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("LoopPlugin has been enabled!");
    }
 
    @Override
    public void onDisable() {
        getLogger().info("LoopPlugin has been disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("loop") && args.length > 2) {
    		try {
				int delay = Integer.parseInt(args[args.length - 1]);
				int repeat = Integer.parseInt(args[args.length - 2]);
				String stringCommand = getStringCommand(args);
				return executeCommand(sender, stringCommand, repeat, delay);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return false;
			}
    	}
    	
    	return false; 
    }
    
    private boolean executeCommand(final CommandSender sender, final String cmd, final int repeat, int delay) {
    	sender.sendMessage(ChatColor.RED + "Loop Progress: 1/" + repeat);
		if (!Bukkit.dispatchCommand(sender, parseStringCommand(cmd, 0))) {
			return false;
		}
		
    	for (int i = 1; i < repeat; i++) {
    		final int j = i + 1;
			getServer().getScheduler().scheduleSyncDelayedTask(this,
					new Runnable() {
						@Override
						public void run() {
							sender.sendMessage(ChatColor.RED + "Loop Progress: " + j + "/" + repeat);
							Bukkit.dispatchCommand(sender, parseStringCommand(cmd, j));
						}
					}, delay*i);
		}
		return true;
    }
    
    private String getStringCommand(String[] args) {
    	String stringCommand = "";
    	for (int i = 0; i < args.length - 2; i++) {
    		stringCommand +=  " " + args[i];
    	}
    	
    	return stringCommand.substring(1);
    }
    
    private String parseStringCommand(String cmd, int index) {
    	String[] parts = cmd.split("<|>");
    	cmd = "";

    	
    	for (int i = 1; i < parts.length; i += 2) {
    		parts[i] = parse(parts[i], index);
    	}
    	
    	for (String part : parts) {
    		cmd += part;
    	}
    	
    	return cmd;
    }
    
    private String parse(String part, int index) {
    	part = part.replaceAll("random", Double.toString(Math.random()));
    	//part.replaceAll("$i", Integer.toString(index));
    	
    	Expression e = new ExpressionBuilder(part)
    		.variables("i")
    		.build();
    	
    	e.setVariable("i", index);
    	
    	return Double.toString(e.evaluate());
    }
}