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

        databaseManager.executeUpdate("REPLACE INTO " + tableName + " (player_uuid, inventory) VALUES (?, ?)",
                player.getUniqueId().toString(), encodedItems);
    }

    public void loadInventory(Player player) throws SQLException {
        ResultSet rs = databaseManager.executeQuery("SELECT inventory FROM " + tableName + " WHERE player_uuid = ?",
                player.getUniqueId().toString());

        if (rs.next()) {
            String encodedItems = rs.getString("inventory");
            if (encodedItems == null) {
                System.out.println("Error: Encoded inventory is null");
                player.getInventory().clear(); // Clear inventory if null
            } else {
                ItemStack[] items = decodeItems(encodedItems);
                player.getInventory().setContents(items);
            }
        } else {
            System.out.println("Error: No inventory found for player " + player.getUniqueId().toString());
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
        if (data == null || data.isEmpty()) {
            return new ItemStack[36];
        }

        Type listType = new TypeToken<List<ItemData>>() {}.getType();
        List<ItemData> itemDataList = gson.fromJson(data, listType);
        if (itemDataList == null) {
            itemDataList = new ArrayList<>();
        }

        ItemStack[] items = new ItemStack[36];
        for (ItemData itemData : itemDataList) {
            int slot = itemData.getSlot();
            if (slot >= 0 && slot < items.length) {
                items[slot] = itemData.getItemStack();
            } else {
                System.out.println("Warning: Slot " + slot + " is out of bounds for inventory array.");
            }
        }
        return items;
    }
}