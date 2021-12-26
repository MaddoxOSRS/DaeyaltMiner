package main.Tasks;

import main.Data.Constants;
import main.DaeyaltMiner;
import main.Logic.InteractLogic;
import main.Logic.UtilityLogic;
import main.TaskPriority.Priority;
import main.TaskPriority.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.mobile.script.ScriptManager;


public class Mining implements Task {
    private DaeyaltMiner main = DaeyaltMiner.getInstance();


    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        if (UtilityLogic.insidedaeyaltMine()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean execute() {
        switch (getSubTask()) {

            case WALK_TO_MINE:
                main.currentStatus = "Walking to Daeyalt block to mine...";
                System.out.println("Walking to nearest Daeyalt block.");

                GameObject daeyalt = InteractLogic.getNearestObject(39095, 25);

                if (!daeyalt.valid() || !daeyalt.inViewport()) {
                    System.out.println("Daeyalt essence mine is not on-screen, walking to closest location...");
                    Movement.moveTo(daeyalt.getTile());
                    Camera.turnTo(daeyalt);
                    return Condition.wait(daeyalt::inViewport, 800, 10);
                }

                System.out.println("Daeyalt essence is on-screen, interacting to mine it...");
                if (!daeyalt.interact("Mine")) {
                    System.out.println("Cannot interact with Daeyalt essence...");
                    return false;
                }
                if (Condition.wait(() -> Players.local().inMotion(), 200, 5)) {
                    System.out.println("We are moving");
                    return Condition.wait(this::areWeMining, 800, 10);
                }
                break;


            case MINING:
                main.currentStatus = "Currently Mining...";
                if (areWeMining()) {
                    main.lastActivityTime = ScriptManager.INSTANCE.getRuntime(true);
                    Game.closeOpenTab();
                }
                break;
        }

        return false;
    }


    enum SubTask {
        WALK_TO_MINE,
        MINING
    }

    private SubTask getSubTask() {
        if (!areWeMining())
            return SubTask.WALK_TO_MINE;
        else
            return SubTask.MINING;
    }

    private boolean areWeMining() {
        return Players.local().animation() == Constants.MINING_ANIMATION;
    }

}
