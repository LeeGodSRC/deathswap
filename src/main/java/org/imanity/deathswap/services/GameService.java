package org.imanity.deathswap.services;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.imanity.deathswap.Deathswap;
import org.imanity.deathswap.enums.GameStatus;
import org.imanity.framework.bukkit.Imanity;
import org.imanity.framework.bukkit.metadata.Metadata;
import org.imanity.framework.bukkit.metadata.MetadataKey;
import org.imanity.framework.bukkit.util.Utility;
import org.imanity.framework.plugin.service.Autowired;
import org.imanity.framework.plugin.service.IService;
import org.imanity.framework.plugin.service.Service;

import java.io.File;
import java.util.Arrays;

@Service(name = "game")
@Getter
public class GameService implements IService {

    private static final MetadataKey<Boolean> PLAYING_TAG = MetadataKey.createBooleanKey("Playing");

    @Autowired
    private Deathswap plugin;
    @Autowired
    private SwapService swapService;

    private int maxCount;
    @Setter
    private World world;
    @Setter
    private GameStatus status;

    public int getAliveCount() {
        return (int) Imanity.getPlayers()
                .stream()
                .filter(this::isPlaying)
                .count();
    }

    public Player getLastPlayerStanding() {
        return Imanity.getPlayers()
                .stream()
                .filter(this::isPlaying)
                .findFirst()
                .orElse(null);
    }

    public void init() {
        this.status = GameStatus.LOBBY;

        File file = new File(this.plugin.getServer().getWorldContainer(), "arena");
        file.delete();
    }

    public void createWorld() {
        this.world = new WorldCreator("arena")
                .cache(true)
                .environment(World.Environment.NORMAL)
                .createWorld();

        this.world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
    }

    public void start() {

        this.status = GameStatus.GAMING;

        int count = 0;
        Location location = this.world.getSpawnLocation();
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {

            Utility.clear(player);
            player.setFlying(false);
            player.setAllowFlight(false);

            count++;
            player.teleport(location);
            player.sendMessage(ChatColor.AQUA + "Start!");
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);

            Arrays.asList(
                    new PotionEffect(PotionEffectType.SPEED, 30 * 20, 2),
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 255),
                    new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 1)
            ).forEach(player::addPotionEffect);

            Metadata
                    .provideForPlayer(player)
                    .put(PLAYING_TAG, true);

        }

        this.maxCount = count;

        this.swapService.timer();

    }

    public void check() {

        if (this.getAliveCount() > 1) {
            return;
        }

        this.status = GameStatus.ENDING;

        Player winner = this.getLastPlayerStanding();

        for (Player player : Imanity.getPlayers()) {
            player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
            player.sendMessage(ChatColor.GOLD + winner.getName() + " won!");
        }

    }

    public void setSpectator(Player player) {

        this.removeFromPlaying(player);

        Utility.clear(player);

        player.setAllowFlight(true);
        player.setFlying(true);

        player.setCanPickupItems(false);
        player.setCanPickupExperience(false); // It's ImanitySpigot stuff

    }

    public void removeFromPlaying(Player player) {
        Metadata
                .provideForPlayer(player)
                .remove(PLAYING_TAG);
    }

    public boolean isPlaying(Player player) {
        return Metadata
                .provideForPlayer(player)
                .has(PLAYING_TAG);
    }
}
