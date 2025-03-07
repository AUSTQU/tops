package top.statistics.handlers;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiProtectionHandler implements Listener {

    private static final String GUI_TITLE = ChatColor.DARK_GRAY + "Topki graczy"; // Tytuł GUI, musi być zgodny z Twoim GUI

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Sprawdzamy, czy GUI ma odpowiedni tytuł
        if (event.getView().getTitle().equals(GUI_TITLE)) {
            // Anulujemy event, jeśli gracz próbował coś kliknąć w GUI
            event.setCancelled(true);
        }
    }
}