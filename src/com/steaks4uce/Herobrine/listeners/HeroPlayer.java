package com.steaks4uce.Herobrine.listeners;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.steaks4uce.Herobrine.Herobrine;
import com.steaks4uce.Herobrine.PossibleActions;

public class HeroPlayer implements Listener {
    public Herobrine plugin;
    Random r = new Random();
    PossibleActions actions = new PossibleActions(plugin);

    public HeroPlayer(Herobrine instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        int eventChoice = r.nextInt(plugin.innerChance + 1);
        if (eventChoice == 1) {
            if (plugin.modifyWorld) {
                actions.createTorch(p);
            }
        } else if (eventChoice == 2) {
            if (plugin.modifyWorld) {
                actions.createSign(p);
            }
        } else if (eventChoice == 3) {
            actions.playSound(p);
        } else if (eventChoice == 4) {
            if (plugin.modifyWorld) {
                actions.randomFire(p);
            }
        } else if (eventChoice == 5) {
            actions.attackPlayer(p);
        } else if (eventChoice == 6) {
            actions.appearNear(p);
        } else if (eventChoice == 7) {
            if (plugin.modifyWorld) {
                actions.buryPlayer(p);
            }
        } else if (eventChoice == 8) {
            if (plugin.modifyWorld) {
                actions.placeChest(p);
            }
        }
    }
}