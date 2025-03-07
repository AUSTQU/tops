package top.statistics.handlers;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import top.data.DataManager;

import java.util.HashMap;
import java.util.Map;

public class DistanceTravelledHandler implements Listener {

    private final DataManager dataManager;

    // Przechowywanie ostatnich pozycji graczy
    private final Map<String, Location> lastPositions = new HashMap<>();

    public DistanceTravelledHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Pobierz nazwę gracza i jego aktualną pozycję
        String playerName = event.getPlayer().getName();
        Location currentLocation = event.getTo();

        // DEBUG: Wyświetlamy obecną pozycję
        System.out.println("DEBUG: Aktualna pozycja gracza " + playerName + ": " + currentLocation);

        // Jeśli `currentLocation` to null, zakończ
        if (currentLocation == null) {
            return;
        }

        // Pobierz ostatnią pozycję gracza
        Location lastLocation = lastPositions.get(playerName);

        // Jeśli brak ostatniej pozycji, ustaw bieżącą jako startową i zakończ
        if (lastLocation == null) {
            lastPositions.put(playerName, currentLocation);
            return;
        }

        // Oblicz dystans ruchu w osi X-Z
        double deltaX = currentLocation.getX() - lastLocation.getX();
        double deltaZ = currentLocation.getZ() - lastLocation.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // Jeśli minimalny dystans jest za mały, zakończ
        if (distance < 0.1) {
            return;
        }

        // Pobierz aktualny dystans gracza z konfiguracji
        int currentDistance = dataManager.getConfig().getInt(playerName + ".distanceTravelled", 0);

        // Dodaj przeliczony dystans (zaokrąglony w dół) do istniejącego
        int newDistance = currentDistance + (int) Math.floor(distance);

        // DEBUG: Wyświetl dane z obliczonego dystansu
        System.out.println("DEBUG: Dystans gracza " + playerName + " zwiększony o: " + Math.floor(distance));
        System.out.println("DEBUG: Łączny dystans gracza " + playerName + ": " + newDistance);

        // Zapisz nowy dystans gracza w konfiguracji
        dataManager.getConfig().set(playerName + ".distanceTravelled", newDistance);

        // Zapisz bieżącą pozycję jako ostatnią znaną
        lastPositions.put(playerName, currentLocation);

        // DEBUG: Spróbuj zapisać konfigurację gracza
        try {
            dataManager.saveConfig();
            System.out.println("DEBUG: Konfiguracja dla gracza " + playerName + " zapisana pomyślnie.");
        } catch (Exception e) {
            System.err.println("ERROR: Błąd przy zapisie konfiguracji dla gracza " + playerName + ": " + e.getMessage());
        }
    }
}