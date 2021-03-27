package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand {

    private MainCommand mainCommand;
    private Leastereggs main;

    public TeleportCommand (MainCommand mainCommand, CommandSender sender, String[] args) {
        this.mainCommand = mainCommand;
        this.main = mainCommand.getMain();

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            if (args.length>1) {
                try {
                    int id = Integer.parseInt(args[1]);

                    if (main.storageUtils.getEggs().get(id)!=null) {
                        Egg egg = main.storageUtils.getEggs().get(id);

                        p.teleport(egg.getLoc().clone().add(0.5,0.5,0.5));
                        main.utils.sendMsg(p,main.getCfg().getString("messages.teleported","&eYou have been teleported to the Egg!"));
                    } else {
                        main.utils.sendMsg(p,main.getCfg().getString("messages.invalidID","&cThere is no Easter Egg with this ID!"));
                    }
                } catch (NumberFormatException ex) {
                    main.utils.sendMsg(p,main.getCfg().getString("messages.mustBeNumber","&cYou must type a number!"));
                }

            } else {
                main.utils.sendMsg(p, main.getCfg().getString("messages.usage.syntaxe", "&cInvalid Command Usage! &7Usage: &f/ee %arguments%")
                        .replace("%arguments%", "tp "
                                + main.getCfg().getString("messages.usage.argument", "<%arg%>").replace("%arg%", "ID")));
            }
        } else
            main.utils.sendConsoleMsg(main.getCfg().getString("messages.onlyPlayer", "&cThis command can be sent only ingame!"));
    }
}
