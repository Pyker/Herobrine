package com.steaks4uce.Herobrine.listeners;

import org.bukkit.Material;
import org.bukkit.World;
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
import org.bukkit.entity.EntityType;

public class HeroEntity implements Listener {
    public Herobrine plugin;
    CustomLogger log = new CustomLogger(plugin);
    
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
        if (event.getEntityType().equals(EntityType.ZOMBIE) && plugin.trackingEntity && plugin.isDead()) {
            plugin.hbEntity = e;
            plugin.trackingEntity = false;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        World w = event.getEntity().getWorld();
        if (e.equals(plugin.hbEntity)) {
            w.dropItemNaturally(e.getLocation(), new ItemStack(Material.GOLDEN_APPLE, 1));
            w.createExplosion(e.getLocation(), -1.0F);
            plugin.isAttacking = false;
            event.setDroppedExp(50);
            event.getDrops().clear();
            if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e.getLastDamageCause();
                if(ev.getDamager() instanceof Player) {
                    Player p = (Player)ev.getDamager();
                    if (plugin.sendMessages) {
                        TextGenerator tg = new TextGenerator();
                        p.sendMessage(tg.getMessage());
                    } 
                    log.event(8, p.getName());
                }
            }
        }
    }
}