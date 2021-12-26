package maddox.Logic;

import org.powbot.api.rt4.walking.model.Skill;


public class UtilityLogic {
    private static final long J = System.nanoTime();
    private static final long M = System.currentTimeMillis();


    public static int getMiningLevel() {
        return Skill.Mining.realLevel();
    }

    public static boolean insidedaeyaltMine() {
        return InteractLogic.isObjectPresent("Daeyalt Essence", 25);
    }

    public static long timeFromMark(long t) {
        return currentTimeMillis() - t;
    }

    public static long currentTimeMillis() {
        return M + (System.nanoTime() - J) / 1000000L;
    }


}
