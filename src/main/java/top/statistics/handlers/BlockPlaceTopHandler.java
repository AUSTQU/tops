package top.statistics.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import top.data.DataManager;

public class BlockPlaceTopHandler implements Listener {

    private final DataManager dataManager;

    public BlockPlaceTopHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();

        // Pobieramy aktualną liczbę bloków postawionych przez gracza
        int blocksPlaced = dataManager.getConfig().getInt(playerName + ".blocksPlaced", 0);

        // Zwiększamy liczbę bloków o 1
        dataManager.getConfig().set(playerName + ".blocksPlaced", blocksPlaced + 1);

        // Zapisujemy nowe dane
        dataManager.saveConfig();
    }
}