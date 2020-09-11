package org.imanity.deathswap.listeners;

import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.enums.GameStatus;
import org.imanity.deathswap.services.GameService;
import org.imanity.framework.bukkit.events.player.PlayerDamageEvent;
import org.imanity.framework.bukkit.listener.FunctionEventChecker;
import org.imanity.framework.bukkit.listener.FunctionEventHandler;
import org.imanity.framework.bukkit.listener.FunctionListener;
import org.imanity.framework.events.annotation.AutoWiredListener;
import org.imanity.framework.plugin.service.Autowired;

@AutoWiredListener
public class EndingListener extends FunctionListener<Deathswap> {

    @Autowired
    private GameService gameService;

    public EndingListener() {
        this.initial(Deathswap.INSTANCE, new FunctionEventChecker()
                .nonPlayerOnly(() -> gameService.getStatus() == GameStatus.ENDING));
    }

    @FunctionEventHandler
    public void onPlayerDamage(PlayerDamageEvent event) {
        event.setCancelled(true);
    }

}
