package top.statistics.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import top.data.DataManager;

public class DistanceTravelHandler implements Listener {

    private final DataManager dataManager;

    public DistanceTravelHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Pobieramy pozycje gracza przed i po ruchu
        var from = event.getFrom();
        var to = event.getTo();

        // Sprawdzamy, czy gracz faktycznie się przemieścił (X, Y, Z)
        if (to != null && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
            String playerName = event.getPlayer().getName();

            // Obliczenie dystansu przebiegniętego między z "from" a "to"
            double distance = from.distance(to);

            // Aktualizacja dystansu w konfiguracji
            double currentDistance = dataManager.getConfig().getDouble(playerName + ".distance", 0.0);
            dataManager.getConfig().set(playerName + ".distance", currentDistance + distance);

            // Opcjonalny zapis do pliku
            dataManager.saveConfig();
        }
    }

    /**
     * Metoda aktualizująca dystans gracza, wywoływana w harmonogramie.
     * Tutaj można dodać jakiekolwiek dodatkowe procesy walidacji, jeśli są potrzebne.
     *
     * @param player Gracz, którego dane są aktualizowane
     */
    public void updateStatistic(Player player) {
        String playerName = player.getName();

        // Jeśli gracz nie istnieje w YAML, ustaw jego dystans na 0
        if (!dataManager.getConfig().contains(playerName + ".distance")) {
            dataManager.getConfig().set(playerName + ".distance", 0.0);
        }

        // Na tym etapie można dodać inne procesy związane z synchronizacją danych dystansu
        dataManager.saveConfig();
    }
}