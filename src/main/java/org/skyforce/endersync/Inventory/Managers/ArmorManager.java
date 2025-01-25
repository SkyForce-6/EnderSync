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

public class ArmorManager {
    private final DatabaseManager databaseManager;
    private final Gson gson;
    private final String tableName;

    public ArmorManager(DatabaseManager databaseManager, String tableName) {
        this.databaseManager = databaseManager;
        this.gson = new GsonBuilder().create();
        this.tableName = tableName;
    }

    public void saveArmor(Player player) throws SQLException {
        ItemStack[] armor = player.getInventory().getArmorContents();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        String encodedArmor = encodeItems(armor);
        String encodedOffhand = encodeItem(offhand);

        if (encodedArmor == null) {
            encodedArmor = "";
        }
        if (encodedOffhand == null) {
            encodedOffhand = "";
        }

        // Retrieve the existing inventory value
        String existingInventory = "[]"; // Default empty inventory
        ResultSet rs = databaseManager.executeQuery("SELECT inventory FROM " + tableName + " WHERE uuid = ?",
                player.getUniqueId().toString());
        if (rs.next()) {
            existingInventory = rs.getString("inventory");
        }

        // Perform the REPLACE INTO operation
        databaseManager.executeUpdate("REPLACE INTO " + tableName + " (uuid, inventory, armor, offhand) VALUES (?, ?, ?, ?)",
                player.getUniqueId().toString(), existingInventory, encodedArmor, encodedOffhand);
    }

    public void loadArmor(Player player) throws SQLException {
        ResultSet rs = databaseManager.executeQuery("SELECT armor, offhand FROM " + tableName + " WHERE uuid = ?",
                player.getUniqueId().toString());

        if (rs.next()) {
            String encodedArmor = rs.getString("armor");
            String encodedOffhand = rs.getString("offhand");
            ItemStack[] armor = decodeItems(encodedArmor);
            ItemStack offhand = decodeItem(encodedOffhand);
            player.getInventory().setArmorContents(armor);
            player.getInventory().setItemInOffHand(offhand);
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

    private String encodeItem(ItemStack item) {
        if (item == null) {
            return "";
        }
        return gson.toJson(new ItemData(item, 0));
    }

    private ItemStack[] decodeItems(String data) {
        if (data == null || data.isEmpty()) {
            return new ItemStack[4]; // Armor slots are 4
        }

        Type listType = new TypeToken<List<ItemData>>() {}.getType();
        List<ItemData> itemDataList = gson.fromJson(data, listType);
        if (itemDataList == null) {
            itemDataList = new ArrayList<>();
        }

        ItemStack[] items = new ItemStack[4];
        for (ItemData itemData : itemDataList) {
            items[itemData.getSlot()] = itemData.getItemStack();
        }
        return items;
    }

    private ItemStack decodeItem(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        ItemData itemData = gson.fromJson(data, ItemData.class);
        return itemData.getItemStack();
    }
}