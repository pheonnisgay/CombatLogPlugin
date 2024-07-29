package de.cooltechno.combalog.util;

import de.cooltechno.combalog.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CombatTimerLogic {

    public static ArrayList<UUID> Combat = new ArrayList<>();
    private final static HashMap<UUID, BukkitTask> combatTasks = new HashMap<>();

    public static boolean IsInCombat(Player player){
        if (Combat.contains(player.getUniqueId())){
            return true;
        }
        return false;
    }

    public static void AddToCombat(Player player){

        if (Combat.contains(player.getUniqueId())) {
            BukkitTask existingTask = combatTasks.get(player.getUniqueId());
            if (existingTask != null) {
                existingTask.cancel();
            }
        }else {
            Combat.add(player.getUniqueId());
        }


        // Schedule new task to remove player after specified time
        BukkitTask newTask = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            Combat.remove(player.getUniqueId());
            combatTasks.remove(player.getUniqueId());
        }, Main.getPlugin().getCombatTime() * 20L);

        combatTasks.put(player.getUniqueId(), newTask);
    }

    public static void RemoveFromCombat(Player player){
        // Cancel existing task(for manual Combat removal)
        BukkitTask existingTask = combatTasks.get(player.getUniqueId());
        if (existingTask != null) {
            existingTask.cancel();
            combatTasks.remove(player.getUniqueId());
        }
        Combat.remove(player.getUniqueId());
    }

}
