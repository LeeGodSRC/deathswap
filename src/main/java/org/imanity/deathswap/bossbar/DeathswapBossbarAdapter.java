package org.imanity.deathswap.bossbar;

import org.bukkit.ChatColor;
import org.imanity.deathswap.timers.SwapTeleportTimer;
import org.imanity.framework.ImanityCommon;
import org.imanity.framework.bukkit.bossbar.BossBar;
import org.imanity.framework.bukkit.bossbar.BossBarAdapter;
import org.imanity.framework.bukkit.bossbar.BossBarData;
import org.imanity.framework.bukkit.timer.TimerHandler;
import org.imanity.framework.bukkit.util.FormatUtil;
import org.imanity.framework.plugin.service.Autowired;

public class DeathswapBossbarAdapter implements BossBarAdapter {

    @Autowired
    private TimerHandler timerHandler;

    public DeathswapBossbarAdapter() {
        ImanityCommon.SERVICE_HANDLER.registerAutowired(this);
    }

    @Override
    public BossBarData tick(BossBar bossBar) {
        SwapTeleportTimer teleportTimer = this.timerHandler.getTimer(SwapTeleportTimer.class);

        if (teleportTimer == null) {
            return null;
        }

        long time = teleportTimer.timeRemaining();
        return new BossBarData(ChatColor.GOLD + "Will Swap after " + FormatUtil.formatToSecondsAndMinutes(teleportTimer.secondsRemaining()), time > 0 ? (time / (float) SwapTeleportTimer.SWAP_TIME) * 100.0F : 0);
    }
}
