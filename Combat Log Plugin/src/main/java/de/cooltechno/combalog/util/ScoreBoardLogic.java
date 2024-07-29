package de.cooltechno.combalog.util;

import de.cooltechno.combalog.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScoreBoardLogic {

    static final int time = Main.getPlugin().conf.getInt("combat.timer");
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final java.util.Map<Player, ScheduledFuture<?>> playerTimers = new java.util.HashMap<>();

    public static void showCombatTimer(Player player) {
        // Cancel existing timer if present
        if (playerTimers.containsKey(player)) {
            ScheduledFuture<?> existingFuture = playerTimers.get(player);
            existingFuture.cancel(false);
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard TimerScoreBoard = manager.getNewScoreboard();

        Objective objective = TimerScoreBoard.registerNewObjective("TimerScoreBoard", "dummy", Main.getPlugin().conf.get("combat.scoreboard.title").toString());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team Timer = TimerScoreBoard.registerNewTeam("Timer");
        Timer.addEntry(ChatColor.BLUE.toString());
        Timer.setPrefix("15");

        Score Top = objective.getScore("§4DO NOT LOG OUT!");
        Top.setScore(2);

        Score Bottom = objective.getScore("§4DO NOT LOG OUT!!");
        Bottom.setScore(0);

        player.setScoreboard(TimerScoreBoard);

        // Define the runnable task
        Runnable displayTask = new Runnable() {
            int remainingTime = time; // Set to initial time

            @Override
            public void run() {
                if (remainingTime > -1) {
                    if (playerTimers.containsKey(player)) {
                        UpdateScoreBoard(player, remainingTime);
                        remainingTime--;
                    }
                } else {
                    playerTimers.remove(player); // Remove the timer entry from the map
                    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    remainingTime = time;
                }
            }
        };

        // Schedule the display task to run every second
        ScheduledFuture<?> displayHandle = scheduler.scheduleAtFixedRate(displayTask, 0, 1, TimeUnit.SECONDS);

        // Store the future in the map
        playerTimers.put(player, displayHandle);

        objective.getScore(ChatColor.BLUE.toString()).setScore(1);
    }

    public static void UpdateScoreBoard(Player player, int time) {
        Scoreboard scoreboard = player.getScoreboard();
        Team Timer = scoreboard.getTeam("Timer");

        if (Timer != null) {
            Timer.setPrefix("You are in Combat for " + time + "s");
        }
    }
}
