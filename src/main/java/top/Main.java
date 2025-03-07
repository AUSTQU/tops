package top;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.commands.TopkiCommand;
import top.data.DataManager;
import top.statistics.handlers.BlockBreakTopHandler;
import top.statistics.handlers.BlockPlaceTopHandler;
import top.statistics.handlers.FoodConsumeHandler;
import top.statistics.handlers.TimeTopHandler;
import top.statistics.handlers.GuiProtectionHandler;
import top.statistics.handlers.DeathsTopHandler;
import top.statistics.handlers.FishingTopHandler;
import top.statistics.handlers.AnimalKillsHandler;
import top.statistics.handlers.MobKillsHandler;
import top.statistics.handlers.DistanceTravelHandler; // Import nowego handlera

public final class Main extends JavaPlugin {

    private DataManager dataManager;
    private TimeTopHandler timeTopHandler;
    private BlockBreakTopHandler blockBreakTopHandler;
    private BlockPlaceTopHandler blockPlaceTopHandler;
    private FoodConsumeHandler foodConsumeHandler;
    private DeathsTopHandler deathsTopHandler;
    private FishingTopHandler fishingTopHandler;
    private AnimalKillsHandler animalKillsHandler;
    private MobKillsHandler mobKillsHandler;
    private DistanceTravelHandler distanceTravelHandler; // Pole dla handlera dystansu

    @Override
    public void onEnable() {
        getLogger().info("Plugin Topki został włączony!");

        // Upewniamy się, że folder danych istnieje
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Inicjalizacja DataManager
        dataManager = new DataManager(this.getDataFolder());

        // Inicjalizacja handlerów
        timeTopHandler = new TimeTopHandler(dataManager);
        blockBreakTopHandler = new BlockBreakTopHandler(dataManager);
        blockPlaceTopHandler = new BlockPlaceTopHandler(dataManager);
        foodConsumeHandler = new FoodConsumeHandler(dataManager);
        deathsTopHandler = new DeathsTopHandler(dataManager);
        fishingTopHandler = new FishingTopHandler(dataManager);
        animalKillsHandler = new AnimalKillsHandler(dataManager);
        mobKillsHandler = new MobKillsHandler(dataManager);
        distanceTravelHandler = new DistanceTravelHandler(dataManager); // Inicjalizacja DistanceTravelHandler

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(timeTopHandler, this);
        getServer().getPluginManager().registerEvents(blockBreakTopHandler, this);
        getServer().getPluginManager().registerEvents(blockPlaceTopHandler, this);
        getServer().getPluginManager().registerEvents(foodConsumeHandler, this);
        getServer().getPluginManager().registerEvents(deathsTopHandler, this);
        getServer().getPluginManager().registerEvents(fishingTopHandler, this);
        getServer().getPluginManager().registerEvents(animalKillsHandler, this);
        getServer().getPluginManager().registerEvents(mobKillsHandler, this);
        getServer().getPluginManager().registerEvents(distanceTravelHandler, this); // Rejestracja DistanceTravelHandler
        getServer().getPluginManager().registerEvents(new GuiProtectionHandler(), this);

        // Rejestracja komendy /topki
        getCommand("topki").setExecutor(new TopkiCommand(dataManager));

        // Harmonogram odświeżania statystyk
        scheduleStatisticUpdates();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Topki został wyłączony!");
        // Zapisujemy konfigurację do pliku przy wyłączeniu pluginu
        dataManager.saveConfig();
    }

    /**
     * Harmonogram, który odświeża statystyki graczy i zapisuje zmiany w konfiguracji.
     */
    private void scheduleStatisticUpdates() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                // Aktualizacja dystansu przebiegniętego przez gracza w harmonogramie
                distanceTravelHandler.updateStatistic(player);

                // Aktualizacja innych topów (np. czas online)
                timeTopHandler.updateStatistic(player);
            });
            dataManager.saveConfig(); // Zapis zmian w configu
        }, 0L, 1200L); // Co 60 sekund (1200 ticków)
    }
}