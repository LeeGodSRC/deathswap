package org.imanity.deathswap.commands;

import org.bukkit.entity.Player;
import org.imanity.deathswap.services.GameService;
import org.imanity.deathswap.services.SwapService;
import org.imanity.framework.bukkit.command.Command;
import org.imanity.framework.plugin.service.Autowired;

public class AdminCommand {

    @Autowired
    private static GameService gameService;
    @Autowired
    private static SwapService swapService;

    @Command(names = { "start" }, permissionNode = "imanity.admin")
    public static void start(Player player) {
        gameService.start();
    }

    @Command(names = {"swap"}, permissionNode = "imanity.admin")
    public static void swap(Player player) {
        swapService.swap();
    }
}
