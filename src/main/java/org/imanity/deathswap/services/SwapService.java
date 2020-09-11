package org.imanity.deathswap.services;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.timers.SwapTeleportTimer;
import org.imanity.framework.bukkit.Imanity;
import org.imanity.framework.bukkit.metadata.Metadata;
import org.imanity.framework.bukkit.metadata.MetadataKey;
import org.imanity.framework.bukkit.timer.TimerHandler;
import org.imanity.framework.plugin.service.Autowired;
import org.imanity.framework.plugin.service.IService;
import org.imanity.framework.plugin.service.Service;
import org.imanity.framework.util.entry.EntryArrayList;

import java.util.*;
import java.util.stream.Collectors;

@Service(name = "deathSwap")
public class SwapService implements IService {

    private MetadataKey<UUID> swapTargetKey;

    @Autowired
    private Deathswap plugin;

    @Autowired
    private GameService gameService;

    @Autowired
    private TimerHandler timerHandler;

    @Override
    public void init() {
        this.swapTargetKey = MetadataKey.createUuidKey("SwapTarget");
    }

    public void timer() {
        this.timerHandler.clear(SwapTeleportTimer.class);

        this.timerHandler.add(new SwapTeleportTimer());
        this.allocateSwapPlayers();

    }

    public void allocateSwapPlayers() {
        EntryArrayList<Player, Player> targetMap = new EntryArrayList<>();

        loop: while (true) {
            List<? extends Player> players = Imanity.getPlayers()
                    .stream()
                    .filter(Player::isOnline)
                    .collect(Collectors.toList());
            List<Player> alreadyTargeted = new ArrayList<>();

            for (Player player : players) {

                Player finalTarget = null;

                for (Player target : players) {
                    if (player == target) {
                        continue;
                    }

                    if (alreadyTargeted.contains(target)) {
                        continue;
                    }

                    finalTarget = target;

                    alreadyTargeted.add(target);
                    targetMap.add(player, target);
                }

                if (finalTarget == null) {
                    targetMap.clear();
                    continue loop;
                }

            }

            break;
        }

        targetMap.forEach((player, target) -> {
            Metadata
                    .provideForPlayer(player.getUniqueId())
                    .put(this.swapTargetKey, target.getUniqueId());
            player.sendMessage(ChatColor.GOLD + "Your target is " + target.getName() + ".");
        });
    }

    public void swap() {

        Map<UUID, Location> cachedLocations = new HashMap<>();
        List<? extends Player> players = Imanity.getPlayers();

        for (Player player : players) {
            if (gameService.isPlaying(player)) {
                cachedLocations.put(player.getUniqueId(), player.getLocation());
            }
        }

        for (Player player : players) {
            UUID uuid = Metadata
                    .provideForPlayer(player.getUniqueId())
                    .get(this.swapTargetKey)
                    .orElse(null);

            if (cachedLocations.containsKey(uuid)) {

                player.teleport(cachedLocations.get(uuid));

            } else {

                player.sendMessage(ChatColor.RED + "Something wrong in system!");

            }
        }

        this.timer();

    }
}
