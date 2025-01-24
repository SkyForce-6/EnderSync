package org.skyforce.endersync.Inventory.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.skyforce.endersync.Data.ItemData;
import org.skyforce.endersync.Database.DatabaseManager;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private final DatabaseManager databaseManager;
    private final Gson gson;
    private final String tableName;

    public InventoryManager(DatabaseManager databaseManager, String tableName) {
        this.databaseManager = databaseManager;
        this.gson = new GsonBuilder().create();
        this.tableName = tableName;
    }

    public void saveInventory(Player player) throws SQLException {
        ItemStack[] items = player.getInventory().getContents();
        String encodedItems = encodeItems(items);

        databaseManager.executeUpdate("REPLACE INTO " + tableName + " (uuid, items) VALUES (?, ?)",
                player.getUniqueId().toString(), encodedItems);
    }

    public void loadInventory(Player player) throws SQLException {
        ResultSet rs = databaseManager.executeQuery("SELECT items FROM " + tableName + " WHERE uuid = ?",
                player.getUniqueId().toString());

        if (rs.next()) {
            String encodedItems = rs.getString("items");
            ItemStack[] items = decodeItems(encodedItems);
            player.getInventory().setContents(items);
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
        ItemStack[] items = new ItemStack[36];
        for (ItemData itemData : itemDataList) {
            items[itemData.getSlot()] = itemData.getItemStack();
        }
        return items;
    }
}