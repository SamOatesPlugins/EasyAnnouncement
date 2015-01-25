package com.samoatesgames.easyannouncement.command;

import com.samoatesgames.easyannouncement.Announcement;
import com.samoatesgames.easyannouncement.EasyAnnouncement;
import com.samoatesgames.samoatesplugincore.commands.BasicCommandHandler;
import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Sam
 */
public class AnnouncementCommand extends BasicCommandHandler {

    private final EasyAnnouncement m_plugin;
    
    /**
     * 
     * @param plugin
     */
    public AnnouncementCommand(EasyAnnouncement plugin) {
        super("easy.announcement.command");
        m_plugin = plugin;
    }

    /**
     * 
     * @param manager
     * @param sender
     * @param arguments
     * @return 
     */
    @Override
    public boolean execute(PluginCommandManager manager, CommandSender sender, String[] arguments) {
        
        if (arguments.length == 0) {
            showHelp(manager, sender);
            return true;
        }
        
        if (arguments[0].equalsIgnoreCase("list")) {
            listAnnouncements(manager, sender);
            return true;
        }
        
        if (arguments[0].equalsIgnoreCase("show")) {
            if (arguments.length != 2) {
                showHelp(manager, sender);
                return true;
            }
            showAnnouncement(manager, sender, arguments[1]);
            return true;
        }
        
        return true;
    }
    
    public void showAnnouncement(final PluginCommandManager manager, final CommandSender sender, final String rawArgument) {
        
        int announcementIndex;
        try {
            announcementIndex = Integer.parseInt(rawArgument);
        } catch(Exception ex) {
            manager.sendMessage(sender, "Invalid announcement ID: " + rawArgument);
            return;
        }
        
        Map<Integer, Announcement> announcements = m_plugin.getAnnouncements();
        if (!announcements.containsKey(announcementIndex)) {
            manager.sendMessage(sender, "Announcement with ID " + rawArgument + " does not exist.");
            return;
        }
        
        Announcement announcement = announcements.get(announcementIndex);        
        for (Player player : m_plugin.getServer().getOnlinePlayers()) {
            announcement.sendToPlayer(player, m_plugin);
        }        
    }
    
    /**
     * 
     * @param manager
     * @param sender 
     */
    public void listAnnouncements(final PluginCommandManager manager, final CommandSender sender) {
        
        Map<Integer, Announcement> announcements = m_plugin.getAnnouncements();
        for (Entry<Integer, Announcement> entry : announcements.entrySet()) {
            
            manager.sendMessage(sender, "ID: " + entry.getKey());
            manager.sendMessage(sender, "Lines:");
            
            for (String line : entry.getValue().getLines()) {
                manager.sendMessage(sender, " - " + line);
            }
        }
        
    }
    
    /**
     * 
     * @param manager
     * @param sender 
     */
    public void showHelp(final PluginCommandManager manager, final CommandSender sender) {
        
        manager.sendMessage(sender, "-- Easy Announcements Help --");
        manager.sendMessage(sender, " - /announcement list");
        manager.sendMessage(sender, " - /announcement show <id>");
        
    }
    
}
