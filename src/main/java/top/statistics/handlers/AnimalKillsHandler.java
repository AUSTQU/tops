package top.statistics.handlers;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import top.data.DataManager;

public class AnimalKillsHandler implements Listener {

    private final DataManager dataManager;

    public AnimalKillsHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Sprawdzamy, czy zabity byt jest żywą istotą oraz czy ma zabójcę
        if (entity instanceof LivingEntity livingEntity) {
            Player killer = livingEntity.getKiller(); // Pobieramy zabójcę tylko, gdy istnieje

            // Sprawdzamy, czy zabójcą jest gracz i czy zabita istota jest dobrym zwierzęciem
            if (killer != null && entity instanceof Animals) {
                String playerName = killer.getName();

                // Pobieramy aktualny licznik z YAML
                int currentAnimalKills = dataManager.getConfig().getInt(playerName + ".animalKills", 0);

                // Zwiększamy licznik
                dataManager.getConfig().set(playerName + ".animalKills", currentAnimalKills + 1);

                // Zapisujemy zmiany w YAML
                dataManager.saveConfig();
            }
        }
    }
}