package org.skyforce.endersync.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.skyforce.endersync.Data.ItemData;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderChestManager {
    private final DatabaseManager databaseManager;
    private final Gson gson;

    public EnderChestManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.gson = new GsonBuilder()
                .create();
    }

    public void saveEnderChest(Player player) throws SQLException {
        Inventory enderChest = player.getEnderChest();
        String encodedItems = encodeItems(enderChest.getContents());

        databaseManager.executeUpdate("REPLACE INTO enderchests (uuid, items) VALUES (?, ?)",
                player.getUniqueId().toString(), encodedItems);
    }

    public void loadEnderChest(Player player) throws SQLException {
        ResultSet rs = databaseManager.executeQuery("SELECT items FROM enderchests WHERE uuid = ?",
                player.getUniqueId().toString());

        if (rs.next()) {
            String encodedItems = rs.getString("items");
            ItemStack[] items = decodeItems(encodedItems);
            player.getEnderChest().setContents(items);
        }
    }

    private String encodeItems(ItemStack[] items) {
        List<ItemData> itemDataList = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null) {
                itemDataList.add(new ItemData(item, i));
            }
        }
        return gson.toJson(itemDataList);
    }

    private ItemStack[] decodeItems(String data) {
        Type listType = new TypeToken<List<ItemData>>() {}.getType();
        List<ItemData> itemDataList = gson.fromJson(data, listType);
        ItemStack[] items = new ItemStack[27];
        for (ItemData itemData : itemDataList) {
            items[itemData.getSlot()] = itemData.getItemStack();
        }
        return items;
    }
}