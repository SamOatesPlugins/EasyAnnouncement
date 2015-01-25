package com.samoatesgames.easyannouncement;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The main plugin class
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public final class EasyAnnouncement extends SamOatesPlugin {
    
    private final Map<Integer, Announcement> m_announcements;
    
    private int m_announcementIndex = 0;
    
    private int m_announcementTime = 1;
    
    private int m_scheduleID = -1;
    
    /**
     * Class constructor
     */
    public EasyAnnouncement() {
        super("EasyAnnouncement", "Announcement", ChatColor.RED);
        m_announcements = new HashMap<Integer, Announcement>();
    }

    /**
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        super.onEnable();
        loadAnnouncements();
        
        final EasyAnnouncement plugin = this;
        Long announcementTime = 20L * 60L * m_announcementTime;
        m_scheduleID = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            public void run() {
                Announcement announcement = plugin.getActiveAnnouncement();
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    announcement.sendToPlayer(player, plugin);                    
                }                
            }
            
        }, announcementTime, announcementTime);
        
        this.logInfo("Succesfully enabled.");
    }

    /**
     * Called when the plugin is disabled
     */
    @Override
    public void onDisable() {
        
        if (m_scheduleID != -1) {
            this.getServer().getScheduler().cancelTask(m_scheduleID);
            m_scheduleID = -1;
        }
        
        this.logInfo("Succesfully disabled.");
    }
    
    /**
     * Register all configuration settings
     */
    public void setupConfigurationSettings() {
    
        this.registerSetting(Setting.TimeBetweenAnnouncements, 10);
        this.registerSetting(Setting.NumberOfAnnouncements, 1);
        
        this.registerSetting("announcement.0.lines", new String[] { "This is an example announcement", "Brought to you by the Easy Announcements plugin." });
    }
    
    /**
     * Load all announcements from the configuration file
     */
    private void loadAnnouncements() {
        
        m_announcementTime = this.getSetting(Setting.TimeBetweenAnnouncements, 10);
        final int noofAnnouncements = this.getSetting(Setting.NumberOfAnnouncements, 1);
        
        this.logInfo("Announcement time delay set to " + m_announcementTime + " minutes.");
        this.logInfo("Loading " + noofAnnouncements + " announcements.");
        
        for (int announcementIndex = 0; announcementIndex < noofAnnouncements; ++announcementIndex) {
            
            String announcementKey = "announcement." + announcementIndex + ".";
            
            List<String> lines = this.getSetting(announcementKey + "lines", null);
            if (lines == null) {
                this.logError("Failed to load the announcement with the ID '" + announcementIndex + "'.");
                continue;
            }
            
            Announcement announcement = new Announcement(lines);
            m_announcements.put(announcementIndex, announcement);
        }
        
    }
    
    /**
     * 
     * @return 
     */
    public Announcement getActiveAnnouncement() {
        
        Announcement announcement = m_announcements.get(m_announcementIndex);
        m_announcementIndex++;
        if (m_announcementIndex >= m_announcements.size()) {
            m_announcementIndex = 0;
        }
        
        return announcement;
    }
}
