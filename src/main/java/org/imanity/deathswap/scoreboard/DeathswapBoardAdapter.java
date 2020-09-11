package org.imanity.deathswap.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.imanity.deathswap.timers.SwapTeleportTimer;
import org.imanity.framework.ImanityCommon;
import org.imanity.framework.bukkit.scoreboard.ImanityBoardAdapter;
import org.imanity.framework.bukkit.timer.TimerHandler;
import org.imanity.framework.bukkit.util.FormatUtil;
import org.imanity.framework.plugin.service.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DeathswapBoardAdapter implements ImanityBoardAdapter {

    @Autowired
    private TimerHandler timerHandler;

    public DeathswapBoardAdapter() {
        ImanityCommon.SERVICE_HANDLER.registerAutowired(this);
    }

    @Override
    public String getTitle(Player player) {
        return "&bDeath Swap";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        SwapTeleportTimer teleportTimer = this.timerHandler.getTimer(SwapTeleportTimer.class);

        if (teleportTimer == null) {
            return null;
        }

        lines.add(ChatColor.WHITE + "Swapping: " + ChatColor.AQUA + FormatUtil.formatToSecondsAndMinutes(teleportTimer.secondsRemaining()));
        return lines;
    }
}
