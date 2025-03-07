package top.statistics.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import top.data.DataManager;

public class FoodConsumeHandler implements Listener {

    private final DataManager dataManager;

    public FoodConsumeHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        String playerName = event.getPlayer().getName();

        // Pobieranie aktualnej liczby zjedzonych przedmiotów
        int itemsEaten = dataManager.getConfig().getInt(playerName + ".itemsEaten", 0);

        // Zwiększamy licznik o 1
        dataManager.getConfig().set(playerName + ".itemsEaten", itemsEaten + 1);

        // Zapisujemy dane do pliku
        dataManager.saveConfig();
    }
}