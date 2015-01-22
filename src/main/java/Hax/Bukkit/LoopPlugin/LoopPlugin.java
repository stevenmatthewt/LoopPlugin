package Hax.Bukkit.LoopPlugin;
 
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
 
public final class LoopPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");
    }
 
    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
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
    
    private boolean executeCommand(final CommandSender sender, final String cmd, int repeat, int delay) {
    	for (int i = 0; i < repeat; i++) {
			getServer().getScheduler().scheduleSyncDelayedTask(this,
					new Runnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(sender, cmd);
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
}