package org.imanity.deathswap.listeners;

import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.enums.GameStatus;
import org.imanity.deathswap.services.GameService;
import org.imanity.framework.bukkit.listener.FunctionEventChecker;
import org.imanity.framework.bukkit.listener.FunctionEventHandler;
import org.imanity.framework.bukkit.listener.FunctionListener;
import org.imanity.framework.events.annotation.AutoWiredListener;
import org.imanity.framework.plugin.service.Autowired;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@AutoWiredListener
public class LobbyListener extends FunctionListener<Deathswap> {

    @Autowired
    private GameService gameService;

    public LobbyListener() {
        this.initial(Deathswap.INSTANCE, new FunctionEventChecker()
                .nonPlayerOnly(() -> gameService.getStatus() == GameStatus.LOBBY));
    }

    @FunctionEventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {

        World world = this.gameService.getWorld();
        event.setSpawnLocation(world.getSpawnLocation());

    }

    @FunctionEventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
