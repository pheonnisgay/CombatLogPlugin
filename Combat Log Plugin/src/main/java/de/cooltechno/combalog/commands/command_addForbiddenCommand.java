package de.cooltechno.combalog.commands;

import de.cooltechno.combalog.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class command_addForbiddenCommand implements CommandExecutor {

    List<String> ForbiddenCommands = (List<String>) Main.getPlugin().conf.getList("combat.forbidden.commands");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if (!(sender instanceof Player)){
            sender.sendMessage(Main.getPlugin().getPrefix() + "You Need To Be A Player in Order To Execute This command");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1){
            player.sendMessage(Main.getPlugin().getPrefix() + "Wrong Syntax: /forbid </command>");
            return true;
        }
        String ForbiddenC = args[0];


        if (!args[0].startsWith("/")){
            player.sendMessage(Main.getPlugin().getPrefix() + "The command does not start with /. This is an example on how to forbid a command: /forbid /spawn");
            return true;
        }

        if (ForbiddenCommands.contains(ForbiddenC)){
            player.sendMessage(Main.getPlugin().getPrefix() + "This command is already forbidden.");
            return true;
        }
        Main.getPlugin().AddForbidCommand(ForbiddenC);
        player.sendMessage(Main.getPlugin().getPrefix() + ForbiddenC + " has been added to the Forbidden Commands in combat");
        return true;
    }

}
