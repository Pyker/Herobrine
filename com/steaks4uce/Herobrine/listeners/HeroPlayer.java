package com.steaks4uce.Herobrine.listeners;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
<<<<<<< HEAD
import org.bukkit.event.player.PlayerChatEvent;
=======
>>>>>>> Cleaned up old, useless code, updated to 1.1-R6
import org.bukkit.event.player.PlayerMoveEvent;

import com.steaks4uce.Herobrine.Herobrine;
import com.steaks4uce.Herobrine.PossibleActions;

public class HeroPlayer implements Listener {
    public static Herobrine plugin;
    Random r = new Random();
    PossibleActions actions = new PossibleActions(plugin);

    public HeroPlayer(Herobrine instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        int eventChoice = r.nextInt(Herobrine.innerChance + 1);
        if (eventChoice == 1) {
            if (Herobrine.modifyWorld) {
                actions.createTorch(p);
            }
        } else if (eventChoice == 2) {
            if (Herobrine.modifyWorld) {
                actions.createSign(p);
            }
        } else if (eventChoice == 3) {
            actions.playSound(p);
        } else if (eventChoice == 4) {
            if (Herobrine.modifyWorld) {
                actions.randomFire(p);
            }
        } else if (eventChoice == 5) {
            actions.attackPlayer(p);
        } else if (eventChoice == 6) {
            actions.appearNear(p);
        } else if (eventChoice == 7) {
            if (Herobrine.modifyWorld) {
                actions.buryPlayer(p);
            }
        } else if (eventChoice == 8) {
            if (Herobrine.modifyWorld) {
                actions.placeChest(p);
            }
<<<<<<< HEAD
        } else if (eventChoice == 9) {
            if (Herobrine.sendMessages) {
                actions.sendMessage(p);
            }
        } else if (eventChoice == 10) {
            if (Herobrine.modifyWorld) {
                actions.digTunnel(p);
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String m = event.getMessage();
        m = m.toLowerCase();
        if (m.contains("herobrine")) {
            int i = r.nextInt(11);
            if (i == 1) {
                Player p = event.getPlayer();
                actions.sendMessage(p);
            }
=======
>>>>>>> Cleaned up old, useless code, updated to 1.1-R6
        }
    }
}