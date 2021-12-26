package maddox.Logic;

import org.powbot.api.rt4.*;


public class InteractLogic {

// Interact with Objects
    public static GameObject getNearestObject(String name, int distance) {
        return Objects.stream().within(distance).nearest().name(name).first();
    }

    public static GameObject getNearestObject(int id, int distance) {
        return Objects.stream().within(distance).nearest().id(id).first();
    }

    public static boolean isObjectPresent(String name, int distance) {
        return Objects.stream().within(distance).nearest().name(name).isNotEmpty();
    }


// Interact with NPCs
    public static Npc getNearestNPC(String name, int distance) {
        return Npcs.stream().within(distance).nearest().name(name).first();
    }

    public static boolean isNPCPresent(String name, int distance) {
        return Npcs.stream().within(distance).nearest().name(name).isNotEmpty();
    }



}
