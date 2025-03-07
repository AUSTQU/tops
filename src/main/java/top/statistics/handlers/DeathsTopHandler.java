package top.statistics.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import top.data.DataManager;

public class DeathsTopHandler implements Listener {

    private final DataManager dataManager;

    public DeathsTopHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName(); // Pobranie nazwy gracza
        int currentDeaths = dataManager.getConfig().getInt(playerName + ".deaths", 0); // Pobranie liczby śmierci
        dataManager.getConfig().set(playerName + ".deaths", currentDeaths + 1); // Zwiększenie licznika śmierci o 1
        dataManager.saveConfig(); // Zapisanie zmian w pliku
    }
}