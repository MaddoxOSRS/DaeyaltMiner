package main.Logic;


import org.powbot.api.rt4.*;

import java.util.Arrays;
import java.util.function.Predicate;

public class InventoryLogic {


    public static boolean contains(String... names) {
        return !Inventory.stream().name(names).isEmpty();
    }

    public static boolean containsOnly(String... names) {
        final Predicate<Item> INVALID_ITEM = item ->
                Arrays.stream(names).noneMatch(n -> n.equals(item.name()));
        return Inventory.stream().noneMatch(INVALID_ITEM);
    }

    public static Item getFirst(String name) {
        return Inventory.stream().name(name).first();
    }

    public static boolean isInventoryFull() {
        return Inventory.isFull();
    }

}
