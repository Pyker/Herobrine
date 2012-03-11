package com.steaks4uce.Herobrine.text;

import com.steaks4uce.Herobrine.Herobrine;

public class CustomLogger {
	public Herobrine plugin;
	
	public CustomLogger(Herobrine instance) {
		plugin = instance;
	}
    
    public void event(int event, String p) {
        if (event == 1) {
            plugin.log.info("Herobrine was summoned by " + p + "!");
        } else if (event == 2) {
            plugin.log.info("Herobrine placed a torch near " + p + "!");
        } else if (event == 3) {
            plugin.log.info("Herobrine placed a sign near " + p + "!");
        } else if (event == 4) {
            plugin.log.info("Herobrine played a sound for " + p + "!"); 
        } else if (event == 5) {
            plugin.log.info("Herobrine is attacking " + p + "!");
        } else if (event == 6) {
            plugin.log.info("Herobrine appeared near " + p + "!");
        } else if (event == 7) {
            plugin.log.info("Herobrine placed fire near " + p + "!");
        } else if (event == 8) {
            plugin.log.info("Herobrine was defeated by " + p + "!");
        } else if (event == 10) {
            plugin.log.info("Herobrine placed a chest near " + p + "!");
        } else if (event == 11) {
            plugin.log.info("Herobrine exploded a chest for " + p + "!");
        } else if (event == 12) {
            plugin.log.info("Herobrine buried " + p + "!");
        } else {
        	plugin.log.info("This function has been removed!");
        }
    } 
    
    public void command(String n, String x) {
        String s = "The command " + x + " was issued by " + n + "!";
        plugin.log.info(s);
    }
    
    public void failed(String n, String x) {
        String s = "The command " + x + " was failed by " + n + "!";
        plugin.log.info(s);
    }
}
