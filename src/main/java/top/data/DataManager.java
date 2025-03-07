package top.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataManager {
    private final File file;
    private FileConfiguration config;
    private final Logger logger = Logger.getLogger("DataManager");

    public DataManager(File dataFolder) {
        file = new File(dataFolder, "playerstop.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    logger.info("Plik playerstop.yml został utworzony.");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Nie udało się utworzyć playerstop.yml", e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Nie udało się zapisać playerstop.yml", e);
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}