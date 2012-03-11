package com.steaks4uce.Herobrine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.steaks4uce.Herobrine.formats.SmokeArea;
import com.steaks4uce.Herobrine.listeners.HeroBlock;
import com.steaks4uce.Herobrine.listeners.HeroEntity;
import com.steaks4uce.Herobrine.listeners.HeroPlayer;
import com.steaks4uce.Herobrine.text.CustomLogger;

public class Herobrine extends JavaPlugin {
    public Logger log;
    public Boolean trackingEntity = false;
    public Entity hbEntity;
    public int innerChance = 100000;
    public ArrayList<SmokeArea> smokes = new ArrayList<SmokeArea>();
    private PossibleActions actions = new PossibleActions(this);
    public Boolean removeMossyCobblestone = true;
    public Boolean changeEnvironment = true;
    public Boolean useFire = true;
    public Boolean fireTrails = true;
    public Boolean sendMessages = true;
    public Boolean isAttacking = false;
    public Boolean canAttack = true;
    public Boolean modifyWorld = true;
    public Boolean useStatistics = true;

    @Override
    public void onDisable() {
    	log.info("Version " + this.getDescription().getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
    	log = this.getLogger();
    	File mainDirectory = this.getDataFolder();
    	File configFile = new File(mainDirectory, "Settings.properties");
    	Properties settingsFile = new Properties();
        mainDirectory.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                FileOutputStream out = new FileOutputStream(configFile);
                settingsFile.put("modify-world", Boolean.toString(modifyWorld));
                settingsFile.put("send-messages", Boolean.toString(sendMessages));
                settingsFile.put("change-environment", Boolean.toString(changeEnvironment));
                settingsFile.put("remove-mossystone", Boolean.toString(removeMossyCobblestone));
                settingsFile.put("action-chance", Integer.toString(innerChance));
                settingsFile.put("allow-fire", Boolean.toString(useFire));
                settingsFile.put("fire-trails", Boolean.toString(fireTrails));
                settingsFile.put("can-attack", Boolean.toString(canAttack));
                settingsFile.put("use-statistics", Boolean.toString(useStatistics));
                settingsFile.store(out, "Configuration file for Herobrine 1.4");
            } catch (IOException ex) {
                log.info("Failed to create the configuration file!");
            }
        } else {
            try {
                FileInputStream in = new FileInputStream(configFile);
                try {
                    settingsFile.load(in);
                    modifyWorld = Boolean.valueOf(settingsFile.getProperty("modify-world"));
                    sendMessages = Boolean.valueOf(settingsFile.getProperty("send-messages"));
                    changeEnvironment = Boolean.valueOf(settingsFile.getProperty("change-environment"));
                    removeMossyCobblestone = Boolean.valueOf(settingsFile.getProperty("remove-mossystone"));
                    innerChance = Integer.parseInt(settingsFile.getProperty("action-chance"));
                    useFire = Boolean.valueOf(settingsFile.getProperty("allow-fire"));
                    fireTrails = Boolean.valueOf(settingsFile.getProperty("fire-trails"));
                    canAttack = Boolean.valueOf(settingsFile.getProperty("can-attack"));
                    useStatistics = Boolean.valueOf(settingsFile.getProperty("use-statistics"));
                } catch (IOException ex) {
                    log.info("Failed to load the configuration file!");
                    getServer().getPluginManager().disablePlugin(this);
                }
            } catch (FileNotFoundException ex) {
                log.info("Failed to load the configuration file!");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        
        log.info("Version " + this.getDescription().getVersion() + " is enabled!");
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new HeroBlock(this), this);
        pm.registerEvents(new HeroEntity(this), this);
        pm.registerEvents(new HeroPlayer(this), this);
        
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (!isDead()) {
                    hbEntity.setVelocity(hbEntity.getLocation().getDirection().multiply(0.6));
                    Random rand = new Random();
                    int i = rand.nextInt(4);
                    if (i == 1) {
                        hbEntity.setVelocity(new Vector(hbEntity.getVelocity().getBlockX(), 1, hbEntity.getVelocity().getZ()));
                    }
                    if (fireTrails && isAttacking) {
                        Block b = hbEntity.getLocation().getBlock();
                        Block g = b.getLocation().subtract(0, 1, 0).getBlock();
                        if (b.getType().equals(Material.AIR) && !(g.getType().equals(Material.AIR))) {
                            b.setType(Material.FIRE);
                        }  
                    }
                }
                
                for (SmokeArea smoke : smokes) {
                    World w = smoke.loc.getWorld();
                    Location l = smoke.loc;
                    w.playEffect(l, Effect.SMOKE, 0);
                }
            }
        }, 0L, 20L);
        
        try {
            if (useStatistics) {
                URL server = new URL("http://www.nkrecklow.com/herobrine/enable.php?write=true");
                URLConnection connection = server.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                @SuppressWarnings("unused")
                String result = br.readLine();
            }
        } catch (Exception ex) {}
    }

    public boolean isDead() {
        if (hbEntity == null || hbEntity.isDead()) { 
            return true; 
        } else { 
            return false;
        }
    }
    
    public boolean canSpawn(World w) {
        return w.getAllowMonsters();
    }
    
    public void addSmoke(Location l) {
        smokes.add(new SmokeArea(l));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        CustomLogger cm = new CustomLogger(this);
        if (cmd.getName().equals("hb")) {
            try {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("appear")) {
                        Player p = (Player)sender;
                        Player target = getServer().getPlayer(args[1]);
                        if (p.isOp()) {
                            if (canSpawn(target.getWorld())) {
                                actions.appearNear(target);
                                p.sendMessage(ChatColor.GREEN + "Herobrine has appeared near " + target.getName() + "!");
                                cm.command(p.getName(), "/hb appear");
                            } else {
                                p.sendMessage(ChatColor.RED + "Herobrine is not allowed to spawn in " + target.getName() + "'s world!");
                                p.sendMessage(ChatColor.RED + "Please enable monsters in that world to continue!");
                                cm.failed(p.getName(), "/hb appear");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permission for this!");
                            cm.failed(p.getName(), "/hb appear");
                        }
                    } else if (args[0].equalsIgnoreCase("bury")) {
                        Player p = (Player)sender;
                        Player target = getServer().getPlayer(args[1]);
                        if (p.isOp()) {
                            if (target.isOnline()) {
                                actions.buryPlayer(target);
                                p.sendMessage(ChatColor.GREEN + "Herobrine has buried " + target.getName() + "!");
                                cm.command(p.getName(), "/hb appear");
                            } else {
                                p.sendMessage(ChatColor.RED + "Player not found!");
                                cm.failed(p.getName(), "/hb bury");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        Player p = (Player)sender;
                        if (p.isOp()) {
                            hbEntity.remove();
                            p.sendMessage(ChatColor.GREEN + "Herobrine has been removed!");
                            cm.command(p.getName(), "/hb remove");
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permission for this!");
                            cm.failed(p.getName(), "/hb remove");
                        }
                    } else if (args[0].equalsIgnoreCase("attack")) {
                        Player p = (Player)sender;
                        Player target = getServer().getPlayer(args[1]);
                        if (p.isOp()) {
                            if (canSpawn(target.getWorld())) {
                                if (canAttack) {
                                    actions.attackPlayer(target);
                                    p.sendMessage(ChatColor.GREEN + "Herobrine is now attacking " + target.getName() + "!");
                                    cm.command(p.getName(), "/hb attack");
                                } else {
                                    p.sendMessage(ChatColor.RED + "Herobrine is not allowed to attack players!");
                                    cm.failed(p.getName(), "/hb attack");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Herobrine is not allowed to spawn in " + target.getName() + "'s world!");
                                p.sendMessage(ChatColor.RED + "Please enable monsters in that world to continue!");
                                cm.failed(p.getName(), "/hb attack");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permission for this!");
                            cm.failed(p.getName(), "/hb attack");
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        Player p = (Player)sender;
                        ChatColor t = ChatColor.RED;
                        ChatColor w = ChatColor.WHITE;
                        p.sendMessage(t + "attack"  + w + " - Attack a certain player.");
                        p.sendMessage(t + "appear"  + w + " - Appear near a certain player.");
                        p.sendMessage(t + "bury"  + w + " - Bury a certain player alive.");
                        p.sendMessage(t + "remove"  + w + " - Remove him in case of error.");
                    } else {
                        Player p = (Player)sender;
                        p.sendMessage(ChatColor.RED + "Not a valid command!");
                        p.sendMessage(ChatColor.RED + "Type '/hb help' for help");
                    }
                } else { 
                    log.info("You must be a player to use this command!");
                }
            } catch (Exception ex) {
                if (sender instanceof Player) {
                    Player p = (Player)sender;
                    p.sendMessage(ChatColor.RED + "Type '/hb help' for help");
                } else {
                    log.info("You must be a player to use this command!");
                }
            }
        }
        return true;
    }
}