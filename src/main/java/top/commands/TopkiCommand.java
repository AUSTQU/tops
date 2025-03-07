package top.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.data.DataManager;

import java.util.*;
import java.util.stream.Collectors;

public class TopkiCommand implements CommandExecutor {

    private final DataManager dataManager;

    public TopkiCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Komenda tylko dla graczy.");
            return true;
        }

        Player player = (Player) sender;

        // Tworzymy GUI
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Topki graczy");

        // Topka czasu (slot 0)
        inventory.setItem(0, generateTopItem(Material.CLOCK, ChatColor.GRAY + "Topka czasu online", getTopTime()));

        // Topka wykopanych bloków (slot 1)
        inventory.setItem(1, generateTopItem(Material.STONE_PICKAXE, ChatColor.GRAY + "Topka wykopanych bloków", getTopBlockBreakers()));

        // Topka postawionych bloków (slot 2)
        inventory.setItem(2, generateTopItem(Material.GRASS_BLOCK, ChatColor.GRAY + "Topka postawionych bloków", getTopPlacedBlocks()));

        // Topka zjedzonych przedmiotów (slot 3)
        inventory.setItem(3, generateTopItem(Material.BREAD, ChatColor.GRAY + "Topka zjedzonych przedmiotów", getTopEatenItems()));

        // Nowa Topka śmierci (slot 5)
        inventory.setItem(4, generateTopItem(Material.SPIDER_EYE, ChatColor.GRAY + "Topka śmierci", getTopDeaths()));

        inventory.setItem(5, generateTopItem(Material.COD, ChatColor.GRAY + "Topka wyłowionych ryb", getTopFishCaught()));

        inventory.setItem(6, generateTopItem(Material.GOLDEN_SWORD, ChatColor.GRAY + "Topka zabitych zwierząt", getTopAnimalKillers()));
        player.openInventory(inventory);

        inventory.setItem(7, generateTopItem(Material.ROTTEN_FLESH, ChatColor.GRAY + "Topka zabitych mobów", getTopMobKillers()));

        inventory.setItem(8, generateTopItem(Material.GOLDEN_BOOTS, ChatColor.GRAY + "Topka dystansu", getTopDistanceTravelers()));

        return true;
    }

    private ItemStack generateTopItem(Material material, String title, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);

        if (lore.isEmpty()) {
            lore = Collections.singletonList(ChatColor.RED + "Brak danych");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private List<String> getTopTime() {
        Map<String, Integer> timeStats = new HashMap<>();

        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int minutes = dataManager.getConfig().getInt(playerName + ".timeOnline", 0);
            timeStats.put(playerName, minutes);
        });

        return generateTopList(timeStats, " minut");
    }

    private List<String> getTopBlockBreakers() {
        Map<String, Integer> blockStats = new HashMap<>();

        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int blocksBroken = dataManager.getConfig().getInt(playerName + ".blocksBroken", 0);
            blockStats.put(playerName, blocksBroken);
        });

        return generateTopList(blockStats, " bloków");
    }

    private List<String> getTopPlacedBlocks() {
        Map<String, Integer> placedBlockStats = new HashMap<>();

        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int blocksPlaced = dataManager.getConfig().getInt(playerName + ".blocksPlaced", 0);
            placedBlockStats.put(playerName, blocksPlaced);
        });

        return generateTopList(placedBlockStats, " bloków");
    }

    private List<String> getTopEatenItems() {
        Map<String, Integer> eatenStats = new HashMap<>();

        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int itemsEaten = dataManager.getConfig().getInt(playerName + ".itemsEaten", 0);
            eatenStats.put(playerName, itemsEaten);
        });

        return generateTopList(eatenStats, " zjedzonych rzeczy");
    }

    private List<String> getTopDeaths() {
        Map<String, Integer> deathStats = new HashMap<>();

        // Pobieranie danych o liczbie śmierci z konfiguracji
        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int deaths = dataManager.getConfig().getInt(playerName + ".deaths", 0); // Klucz YAML: ".deaths"
            deathStats.put(playerName, deaths);
        });

        // Sortowanie i generowanie formatowanej listy wyników
        return generateTopList(deathStats, " razy zginął");
    }

    private List<String> getTopFishCaught() {
        Map<String, Integer> fishStats = new HashMap<>();

        // Pobieranie danych z konfiguracji
        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int fishCaught = dataManager.getConfig().getInt(playerName + ".fishCaught", 0); // Pobranie liczby wyłowionych ryb
            fishStats.put(playerName, fishCaught);
        });

        // Generowanie posortowanej listy Top 10
        return generateTopList(fishStats, " wyłowionych ryb");
    }
    private List<String> getTopAnimalKillers() {
        Map<String, Integer> animalKillStats = new HashMap<>();

        // Pobieranie danych z konfiguracji
        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int animalKills = dataManager.getConfig().getInt(playerName + ".animalKills", 0); // Pobranie liczby zabójstw
            animalKillStats.put(playerName, animalKills);
        });

        // Generowanie posortowanej listy Top 10
        return generateTopList(animalKillStats, " zabitych zwierząt");
    }
    private List<String> getTopMobKillers() {
        Map<String, Integer> mobKillStats = new HashMap<>();

        // Pobieranie danych z konfiguracji
        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            int mobKills = dataManager.getConfig().getInt(playerName + ".mobKills", 0); // Pobieranie wartości mobKills
            mobKillStats.put(playerName, mobKills);
        });

        // Generowanie posortowanej listy Top 10
        return generateTopList(mobKillStats, " zabitych mobów");
    }

    private List<String> getTopDistanceTravelers() {
        Map<String, Double> distanceStats = new HashMap<>();

        // Pobieramy dane z konfiguracji
        dataManager.getConfig().getKeys(false).forEach(playerName -> {
            double distance = dataManager.getConfig().getDouble(playerName + ".distance", 0.0);
            distanceStats.put(playerName, distance);
        });

        // Konwersja Map<String, Double> do Map<String, Integer>
        Map<String, Integer> roundedDistanceStats = new HashMap<>();
        distanceStats.forEach((player, distance) ->
                roundedDistanceStats.put(player, (int) Math.round(distance))
        );

        // Generujemy posortowaną listę Top 10 graczy
        return generateTopList(roundedDistanceStats, " kratek przebiegniętych");
    }

    private List<String> generateTopList(Map<String, Integer> stats, String suffix) {
        if (stats.isEmpty()) {
            return new ArrayList<>();
        }

        return stats.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(entry -> ChatColor.GRAY + "" + (new ArrayList<>(stats.keySet()).indexOf(entry.getKey()) + 1) + ". " +
                        ChatColor.RED + entry.getKey() + ChatColor.DARK_GRAY + " | " +
                        ChatColor.GRAY + entry.getValue() + suffix)
                .collect(Collectors.toList());
    }
}