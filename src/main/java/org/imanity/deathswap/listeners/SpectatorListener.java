package org.imanity.deathswap.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.enums.GameStatus;
import org.imanity.deathswap.services.GameService;
import org.imanity.framework.bukkit.events.player.PlayerDamageByPlayerEvent;
import org.imanity.framework.bukkit.events.player.PlayerDamageEvent;
import org.imanity.framework.bukkit.listener.FunctionEventChecker;
import org.imanity.framework.bukkit.listener.FunctionEventHandler;
import org.imanity.framework.bukkit.listener.FunctionListener;
import org.imanity.framework.bukkit.visual.event.PreHandleVisualEvent;
import org.imanity.framework.events.annotation.AutoWiredListener;
import org.imanity.framework.plugin.service.Autowired;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@AutoWiredListener
public class SpectatorListener extends FunctionListener<Deathswap> {

    @Autowired
    private GameService gameService;

    public SpectatorListener() {
        this.initial(Deathswap.INSTANCE, new FunctionEventChecker()
            .nonPlayerOnly(() -> this.gameService.getStatus() != GameStatus.LOBBY)
            .playerOnly(player -> this.gameService.getStatus() != GameStatus.LOBBY && !this.gameService.isPlaying(player)));
    }

    @FunctionEventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {

        World world = this.gameService.getWorld();
        event.setSpawnLocation(world.getSpawnLocation());

    }

    @FunctionEventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.gameService.setSpectator(player);
    }

    @FunctionEventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onPlayerDamage(PlayerDamageEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onPlayerDamageByPlayer(PlayerDamageByPlayerEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!this.getChecker().getPlayerChecker().apply((Player) event.getEntity())) {
            return;
        }
        event.setCancelled(true);
    }

    @FunctionEventHandler(ignoreCancelled = true)
    public void onRenderVisualBlock(PreHandleVisualEvent event) {
        event.setCancelled(true);
    }

    @FunctionEventHandler(ignoreFunctionCheck = true)
    public void onPlayerDamageBySpectator(PlayerDamageByPlayerEvent event) {
        if (!this.getChecker().getPlayerChecker().apply(event.getDamager())) {
            return;
        }

        event.setCancelled(true);
    }

}
