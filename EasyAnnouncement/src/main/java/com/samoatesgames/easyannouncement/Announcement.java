/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samoatesgames.easyannouncement;

import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Sam
 */
public class Announcement {

    private final List<String> m_lines;
    
    /**
     * 
     * @param lines 
     */
    Announcement(List<String> lines) {
        m_lines = lines;
    }
    
    /**
     * 
     * @param player 
     * @param plugin 
     */
    public void sendToPlayer(Player player, EasyAnnouncement plugin) {
        for (String line : m_lines) {
            plugin.sendMessage(player, line);
        }        
    }
    
}
