package top.statistics.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import top.data.DataManager;

public class MobKillsHandler implements Listener {

    private final DataManager dataManager;

    public MobKillsHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Sprawdzamy, czy zabity byt jest żywą istotą (LivingEntity) i czy istnieje zabójca
        if (entity instanceof LivingEntity livingEntity) {
            Player killer = livingEntity.getKiller();

            // Jeżeli zabójcą jest gracz
            if (killer != null) {
                String playerName = killer.getName();

                // Pobieramy aktualny licznik zabitych mobów
                int currentMobKills = dataManager.getConfig().getInt(playerName + ".mobKills", 0);

                // Zwiększamy licznik
                dataManager.getConfig().set(playerName + ".mobKills", currentMobKills + 1);

                // Zapisujemy zmiany w YAML
                dataManager.saveConfig();
            }
        }
    }
}