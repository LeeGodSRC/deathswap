package org.imanity.deathswap;

import org.imanity.deathswap.scoreboard.DeathswapBoardAdapter;
import org.imanity.deathswap.services.GameService;
import org.imanity.framework.bukkit.Imanity;
import org.imanity.framework.bukkit.command.CommandHandler;
import org.imanity.framework.bukkit.plugin.ImanityPlugin;
import org.imanity.framework.plugin.Plugin;
import org.imanity.framework.plugin.PluginLoadOrder;
import org.imanity.framework.plugin.PluginType;
import org.imanity.framework.plugin.service.Autowired;

import java.io.File;

@Plugin(
        name = "DeathSwap",
        version = "0.0.1",
        authors = {"LeeGod"},
        load = PluginLoadOrder.POSTWORLD,
        type = PluginType.BUKKIT)
public class Deathswap extends ImanityPlugin {

    @Autowired
    public static Deathswap INSTANCE;

    @Autowired
    private GameService gameService;

    @Override
    public void postEnable() {
        CommandHandler.loadCommandsFromPackage(this, "org.imanity.deathswap.commands");

//        Imanity.registerBossBarHandler(new DeathswapBossbarAdapter(), 20L);
        Imanity.registerBoardHandler(new DeathswapBoardAdapter());

        this.gameService.createWorld();
    }

    @Override
    public void postDisable() {

        this.getServer().unloadWorld(this.gameService.getWorld(), true);
        File file = new File(this.getServer().getWorldContainer(), "arena");
        file.delete();

        this.getLogger().info("Deleted arena world.");

    }
}
