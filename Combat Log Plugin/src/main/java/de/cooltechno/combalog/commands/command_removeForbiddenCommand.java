package de.cooltechno.combalog.commands;

import de.cooltechno.combalog.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class command_removeForbiddenCommand implements CommandExecutor {

    List<String> ForbiddenCommands = (List<String>) Main.getPlugin().conf.getList("combat.forbidden.commands");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if (!(sender instanceof Player)){
            sender.sendMessage(Main.getPlugin().getPrefix() + "You Need To Be A Player in Order To Execute This command");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1){
            player.sendMessage(Main.getPlugin().getPrefix() + "Wrong Syntax: /unforbid </command>");
            return true;
        }

        String com = args[0];

        if (!com.startsWith("/")){
            player.sendMessage(Main.getPlugin().getPrefix() + "Wrong Syntax: /unforbid </command>");
            return true;
        }

        if (!ForbiddenCommands.contains(com)){
            player.sendMessage(Main.getPlugin().getPrefix() + "The command" + com + "is not forbidden.");
            return true;
        }
        Main.getPlugin().RemoveForbidCommand(com);
        player.sendMessage(Main.getPlugin().getPrefix() + "You succesfully made " + com + " usable in combat again!");
        return true;
    }

}
