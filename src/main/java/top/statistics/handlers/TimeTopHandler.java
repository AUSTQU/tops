package top.statistics.handlers;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import top.data.DataManager;

public class TimeTopHandler implements Listener { // Dodanie implementacji Listener

    private final DataManager dataManager;

    public TimeTopHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // Możesz dodawać zdarzenia, np. rejestrację gracza lub aktualizację jego danych przy wejściu
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        // Jeśli gracza brakuje w konfiguracji, ustawiamy jego dane początkowe
        if (!dataManager.getConfig().contains(playerName)) {
            dataManager.getConfig().set(playerName + ".timeOnline", 0);
            dataManager.saveConfig();
        }
    }

    // Metoda do aktualizacji czasu online
    public void updateStatistic(org.bukkit.entity.Player player) {
        String playerName = player.getName();

        // Pobieramy aktualną liczbę minut i zwiększamy o 1
        int currentMinutes = dataManager.getConfig().getInt(playerName + ".timeOnline", 0);
        dataManager.getConfig().set(playerName + ".timeOnline", currentMinutes + 1);
    }
}