package de.cooltechno.combalog.listener;

import de.cooltechno.combalog.Main;
import de.cooltechno.combalog.util.CombatTimerLogic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class listener_AsyncCommand implements Listener {


    @EventHandler
    public void onCommandPreProccess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        List<String> ForbiddenCommands = (List<String>) Main.getPlugin().conf.getList("combat.forbidden.commands");

        if (CombatTimerLogic.IsInCombat(player)) {
            for (int i = 0; i < ForbiddenCommands.size(); i++) {

                String forbCommand = ForbiddenCommands.get(i);

                if (command.startsWith(forbCommand)) {
                    player.sendMessage(Main.getPlugin().getPrefix() + Main.getPlugin().conf.get("Strings.used.ForbiddenCommand"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
