package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.ListGUIHolder;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.Arrays;

public class MainCommand implements CommandExecutor {

    private final Leastereggs main;
    private final Utils utils;
    private final DataUtils data;
    private CommandSender sender;

    public MainCommand (Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
        this.data = main.getData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        if (args.length>0) {
            if (sender instanceof Player && !main.storageUtils.getTyping().containsKey(sender) || sender instanceof ConsoleCommandSender) {

                // HELP COMMAND
                if (args[0].equalsIgnoreCase("help"))
                    sendHelpMsg();

                // GIVE COMMAND
                else if (args[0].equalsIgnoreCase("give"))
                    new GiveCommand(this, sender);

                // RELOAD COMMAND
                else if (args[0].equalsIgnoreCase("reload"))
                    new ReloadCommand(this, sender);

                // LIST COMMAND
                else if (args[0].equalsIgnoreCase("list"))
                    new ListCommand(this, sender,args);

                // MENU COMMAND
                else if (args[0].equalsIgnoreCase("menu"))
                    if (isPlayerSender()) {
                        Player p = (Player) sender;
                        p.openInventory(new ListGUIHolder(main, p, 0).getInventory());
                    } else
                        utils.sendConsoleMsg(main.getCfg().getString("messages.onlyPlayer", "&cThis command can be sent only ingame!"));
                // CREATE EGG COMMAND
                else if (args[0].equalsIgnoreCase("create")) {
                    new CreateEggCommand(this,sender);
                }
                // SET EGG COMMAND
                else if (args[0].equalsIgnoreCase("set")) {
                    new SetEggCommand(this,sender);
                }

                // TELEPORT COMMAND
                else if (args[0].equalsIgnoreCase("tp")) {
                    new TeleportCommand(this,sender,args);
                }

                // EDIT EGG COMMAND
                else if (args[0].equalsIgnoreCase("edit")) {
                    new EditCommand(this,sender,args);

                } else
                    sendHelpMsg();
            }
        } else {
            sendHelpMsg();
        }

        return false;
    }

    private void sendHelpMsg(){
        if (isPlayerSender())
            for (String str : main.getCfg().getStringList("messages.help", Arrays.asList("&cMessage is missing in the config!"))){
                utils.sendMsg((Player) sender,str);
            }
        else
            for (String str : main.getCfg().getStringList("messages.help", Arrays.asList("&cMessage is missing in the config!"))) {
                utils.sendConsoleMsg(str);
            }
    }

    public boolean isPlayerSender() {
        return (sender instanceof Player);
    }

    public Leastereggs getMain() {
        return main;
    }

}
