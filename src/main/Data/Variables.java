package main.Data;



public class Variables {
    private static Variables vars;

    public static Variables get() {
        return vars == null? vars = new Variables() : vars;
    }
    public static void reset() {
        vars = new Variables();
    }

    public int daeyaltGained = 0;
    public int daeyaltNeeded = 0;
    public int stopAtMiningLevel = 0;

    public String currentPickaxe = "";
    public boolean readyToMine = false;
}

