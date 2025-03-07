package top.statistics.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import top.data.DataManager;

public class BlockBreakTopHandler implements Listener {

    private final DataManager dataManager;

    public BlockBreakTopHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // Pobieramy aktualną ilość wykopanych bloków
        int currentBlocks = dataManager.getConfig().getInt(playerName + ".blocksBroken", 0);

        // Zwiększamy licznik bloków i zapisujemy w konfiguracji
        dataManager.getConfig().set(playerName + ".blocksBroken", currentBlocks + 1);
        dataManager.saveConfig();
    }
}