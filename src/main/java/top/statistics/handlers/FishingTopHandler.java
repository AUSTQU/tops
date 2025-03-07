package top.statistics.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import top.data.DataManager;

public class FishingTopHandler implements Listener {

    private final DataManager dataManager;

    public FishingTopHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Sprawdzamy, czy gracz wyłowił coś przy użyciu wędki
        if (event.getState() == State.CAUGHT_FISH || event.getState() == State.CAUGHT_ENTITY) {
            String playerName = event.getPlayer().getName(); // Pobranie nazwy gracza
            int currentFishCount = dataManager.getConfig().getInt(playerName + ".fishCaught", 0); // Pobranie aktualnej liczby
            dataManager.getConfig().set(playerName + ".fishCaught", currentFishCount + 1); // Zwiększamy licznik o 1
            dataManager.saveConfig(); // Zapisujemy konfigurację
        }
    }
}