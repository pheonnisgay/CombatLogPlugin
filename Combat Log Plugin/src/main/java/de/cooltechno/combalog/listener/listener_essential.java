package de.cooltechno.combalog.listener;

import de.cooltechno.combalog.Main;
import de.cooltechno.combalog.util.CombatTimerLogic;
import de.cooltechno.combalog.util.ScoreBoardLogic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class listener_essential implements Listener {



    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = (Player) event.getEntity();
            CombatTimerLogic.AddToCombat(player);
            CombatTimerLogic.AddToCombat((Player) event.getDamager());
            ScoreBoardLogic.showCombatTimer(player);
            ScoreBoardLogic.showCombatTimer(((Player) event.getDamager()).getPlayer());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        if (CombatTimerLogic.IsInCombat(player)){
            CombatTimerLogic.RemoveFromCombat(player);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

            if (Main.getPlugin().conf.getBoolean("combat.setting.KillerGetsOutOfCombat") && event.getEntity().getKiller() != null){
                CombatTimerLogic.RemoveFromCombat(event.getEntity().getKiller());
                event.getEntity().getKiller().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if (CombatTimerLogic.IsInCombat(player)){
            CombatTimerLogic.RemoveFromCombat(player);
            player.setHealth(0.0);
            event.setQuitMessage(Main.getPlugin().getPrefix() + player.getName() + " has logged out while being in combat at X: " + player.getLocation().getBlockX() + " Y: " + player.getLocation().getBlockY() + " Z: " + player.getLocation().getBlockZ() + "!");
        }
    }
}
