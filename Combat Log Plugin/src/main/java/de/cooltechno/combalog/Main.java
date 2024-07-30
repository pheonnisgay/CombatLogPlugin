package de.cooltechno.combalog;

import de.cooltechno.combalog.commands.command_addForbiddenCommand;
import de.cooltechno.combalog.commands.command_removeForbiddenCommand;
import de.cooltechno.combalog.listener.listener_AsyncCommand;
import de.cooltechno.combalog.listener.listener_essential;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public File settingsFile;
    public FileConfiguration conf;
    public ArrayList<String> forbcommands;
    private static Main plugin;
    public List<String> ForbiddenCommands = (List<String>) Main.getPlugin().conf.getList("combat.forbidden.commands");

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs(); // Ensure the data folder exists
        settingsFile = new File(getDataFolder(), "Settings.yml");
        conf = YamlConfiguration.loadConfiguration(settingsFile);
        forbcommands = new ArrayList<>();

        getLogger().info("Combat Log Plugin successfully loaded");

        checkForSettingsFile();

        getCommand("forbid").setExecutor(new command_addForbiddenCommand());
        getCommand("unforbid").setExecutor(new command_removeForbiddenCommand());

        Bukkit.getPluginManager().registerEvents(new listener_AsyncCommand(), this);
        Bukkit.getPluginManager().registerEvents(new listener_essential(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPrefix(){
        String prefix = conf.getString("Strings.prefix");
        return prefix;
    }

    public int getCombatTime(){
        int time = conf.getInt("combat.timer");
        return time;
    }


    public void checkForSettingsFile() {
        // Check for existence of settings file
        if (!settingsFile.exists()) {
            // Create settings file
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set default forbidden (only in combat) commands
            forbcommands.add("/spawn");
            forbcommands.add("/back");


            // Set default values
            conf.set("Strings.prefix", "Set Your Prefix here! example: \"[example]\"");
            conf.set("Strings.used.ForbiddenCommand", "You can't use that command in combat!");
            conf.set("combat.timer", 15);
            conf.set("combat.forbidden.commands", forbcommands);
            conf.set("combat.setting.KillerGetsOutOfCombat", false);
            conf.set("combat.scoreboard.title", "§aCombat Timer");

            try {
                conf.save(settingsFile); // Save the configuration to persist changes
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            getLogger().info("The settings file was created. Please set the settings in " + settingsFile.getAbsolutePath());
            getLogger().info("Use '§' In Order To Use Colors in the Config!!");
        }
    }

    public void AddForbidCommand(String Command) {
        ForbiddenCommands.add(Command);
        conf.set("combat.forbidden.commands",ForbiddenCommands);
        try {
            conf.save(settingsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void RemoveForbidCommand(String Command){
        ForbiddenCommands.remove(Command);
        conf.set("combat.forbidden.commands",ForbiddenCommands);
        try {
            conf.save(settingsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Main getPlugin() {
        return plugin;
    }
}
