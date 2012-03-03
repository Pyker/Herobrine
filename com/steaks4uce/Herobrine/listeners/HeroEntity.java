package com.steaks4uce.Herobrine.listeners;
import org.bukkit.Material;
import org.bukkit.World;
<<<<<<< HEAD
import org.bukkit.entity.CreatureType;
=======
>>>>>>> Cleaned up old, useless code, updated to 1.1-R6
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.steaks4uce.Herobrine.Herobrine;
import com.steaks4uce.Herobrine.text.CustomLogger;
import com.steaks4uce.Herobrine.text.TextGenerator;
<<<<<<< HEAD
=======
import org.bukkit.entity.EntityType;
>>>>>>> Cleaned up old, useless code, updated to 1.1-R6

public class HeroEntity implements Listener {
    public static Herobrine plugin;
    CustomLogger log = new CustomLogger();
    
    public HeroEntity(Herobrine instance) {
        plugin = instance;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e.equals(plugin.hbEntity)) {
            if (!(event.getCause()==DamageCause.ENTITY_ATTACK)) {
                event.setCancelled(true);
                e.setFireTicks(0);
            } else {
                event.setDamage(1); 
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (event.getEntityType().equals(EntityType.ZOMBIE) && Herobrine.trackingEntity && plugin.isDead()) {
            plugin.hbEntity = e;
            Herobrine.trackingEntity = Boolean.valueOf(false);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        World w = event.getEntity().getWorld();
        if (e.equals(plugin.hbEntity)) {
            w.dropItemNaturally(e.getLocation(), new ItemStack(Material.GOLDEN_APPLE, 1));
            w.createExplosion(e.getLocation(), -1.0F);
            Herobrine.isAttacking = false;
            event.setDroppedExp(50);
            event.getDrops().clear();
            if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e.getLastDamageCause();
                if(ev.getDamager() instanceof Player) {
                    Player p = (Player)ev.getDamager();
                    if (Herobrine.sendMessages) {
                        TextGenerator tg = new TextGenerator();
                        p.sendMessage(tg.getMessage());
                    } 
                    log.event(8, p.getName());
                }
            }
        }
    }
}