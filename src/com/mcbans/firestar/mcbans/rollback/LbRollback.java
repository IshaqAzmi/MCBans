package com.mcbans.firestar.mcbans.rollback;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.BukkitInterface;

import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;

public class LbRollback extends BaseRollback{
    public LbRollback(BukkitInterface plugin) {
        super(plugin);
    }

    private LogBlock logblock;

    @Override
    public boolean rollback(CommandSender sender, String senderName, String target) {
        if (logblock == null) return false;

        plugin.broadcastPlayer(senderName, ChatColor.GREEN + "Starting rollback..");

        for (String world : worlds) {
            QueryParams params = null;
            try {
                params = new QueryParams(logblock);
                params.setPlayer(target);
                //params.since = (time * plugin.Settings.getInteger("backDaysAgo"));
                params.since = (1440 * plugin.settings.getInteger("backDaysAgo"));
                params.world = plugin.getServer().getWorld(world);
                params.silent = false;

                this.logblock.getCommandsHandler().new CommandRollback(sender, params, true);
            } catch (Exception e) {
                plugin.broadcastPlayer(senderName, ChatColor.RED + "Unable to rollback player!");
                if (plugin.settings.getBoolean("isDebug")) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean setPlugin(Plugin plugin){
        if (plugin == null) return false;

        if (plugin instanceof LogBlock){
            this.logblock = (LogBlock) plugin;
            return true;
        }

        return false;
    }
}