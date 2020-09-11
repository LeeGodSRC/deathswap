package org.imanity.deathswap.timers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.services.SwapService;
import org.imanity.framework.ImanityCommon;
import org.imanity.framework.bukkit.timer.impl.AbstractTimer;
import org.imanity.framework.plugin.service.Autowired;

import java.util.Collection;

public class SwapTeleportTimer extends AbstractTimer {

    public static final long SWAP_TIME = 5 * 60 * 1000L;

    @Autowired
    private Deathswap plugin;

    @Autowired
    private SwapService swapService;

    public SwapTeleportTimer() {
        super(SWAP_TIME);

        ImanityCommon.SERVICE_HANDLER.registerAutowired(this);

        this.announcing(true);
    }

    @Override
    public Collection<? extends Player> getReceivers() {
        return this.plugin.getServer().getOnlinePlayers();
    }

    @Override
    public String announceMessage(Player player, int seconds) {
        return ChatColor.RED + "Swapping in " + seconds + " seconds!";
    }

    @Override
    public void sendMessage(Player player, String message, int seconds) {
        super.sendMessage(player, message, seconds);
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
    }

    @Override
    public void elapsed() {
        this.swapService.swap();
    }
}
