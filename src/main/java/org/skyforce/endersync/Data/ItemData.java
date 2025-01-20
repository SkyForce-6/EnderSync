package org.skyforce.endersync.models;

public class ItemData {
    private final String name;
    private final int amount;
    private final int slot;

    public ItemData(String name, int amount, int slot) {
        this.name = name;
        this.amount = amount;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getSlot() {
        return slot;
    }
}