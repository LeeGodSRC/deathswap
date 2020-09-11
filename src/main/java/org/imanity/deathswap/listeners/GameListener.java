package org.imanity.deathswap.listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.enums.GameStatus;
import org.imanity.deathswap.services.GameService;
import org.imanity.deathswap.services.SwapService;
import org.imanity.framework.bukkit.listener.FunctionEventChecker;
import org.imanity.framework.bukkit.listener.FunctionEventHandler;
import org.imanity.framework.bukkit.listener.FunctionListener;
import org.imanity.framework.events.annotation.AutoWiredListener;
import org.imanity.framework.plugin.service.Autowired;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@AutoWiredListener
public class GameListener extends FunctionListener<Deathswap> {

    @Autowired
    private GameService gameService;
    @Autowired
    private SwapService swapService;

    public GameListener() {
        this.initial(Deathswap.INSTANCE, new FunctionEventChecker()
            .playerOnly(player -> this.gameService.getStatus() == GameStatus.GAMING && this.gameService.isPlaying(player))
            .nonPlayerOnly(() -> this.gameService.getStatus() == GameStatus.GAMING));
    }

    @FunctionEventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {

        World world = this.gameService.getWorld();
        event.setSpawnLocation(world.getSpawnLocation());

    }

    @FunctionEventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.gameService.removeFromPlaying(player);
        this.swapService.allocateSwapPlayers();

        this.gameService.check();

        event.setQuitMessage(ChatColor.RED + player.getName() + " has quit the game.");
    }

    @FunctionEventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You Lost!");

        this.gameService.setSpectator(player);
        this.gameService.check();

        event.setDeathMessage(ChatColor.RED + player.getName() + " Died! " + ChatColor.RED + "(" + this.gameService.getAliveCount() + "/" + this.gameService.getMaxCount() + ")");
    }

}
