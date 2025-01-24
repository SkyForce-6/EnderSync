package org.skyforce.endersync.Data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemData {
    private final String serializedItem;
    private final int slot;

    public ItemData(ItemStack itemStack, int slot) {
        if (itemStack == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }
        this.serializedItem = itemStackToBase64(itemStack);
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        try {
            return itemStackFromBase64(serializedItem);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSlot() {
        return slot;
    }

    private String itemStackToBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack", e);
        }
    }

    private ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();

            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type", e);
        }
    }
}