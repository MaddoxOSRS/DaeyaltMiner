package maddox;

import com.google.common.eventbus.Subscribe;
import maddox.Data.Variables;
import maddox.Data.Constants;
import maddox.Logic.UtilityLogic;
import maddox.TaskPriority.Task;
import maddox.TaskPriority.TaskSet;

import maddox.Tasks.*;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.event.BreakEndedEvent;
import org.powbot.api.event.BreakEvent;
import org.powbot.api.event.InventoryChangeEvent;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.OptionType;
import org.powbot.api.script.ScriptConfiguration;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;
import java.io.IOException;
import java.util.Arrays;


@ScriptManifest(
        name = "MaddDaeyalt",
        description = "Mines Daeyalt Essence at Darkmeyer. Must start the script inside Daeyalt Mines.",
        version = "1.00"
)

@ScriptConfiguration.List(
        {
                @ScriptConfiguration(
                        name = "Daeyalt Count",
                        description = "Total amount of Daeyalt you want to mine before stopping. Leave empty for unlimited.",
                        optionType = OptionType.INTEGER,
                        defaultValue = "0"
                )
        }
)

public class DaeyaltMiner extends AbstractScript {
    public static DaeyaltMiner instance;

    public int lastXPCount = 0;
    public long lastActivityTime = ScriptManager.INSTANCE.getRuntime(true);
    public int startMiningXP = 0;
    public int startMiningLevel = 0;
    public String currentStatus = "";

    public static DaeyaltMiner getInstance() {
        return instance;
    }

    public static void main(String[] args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("C:\\Users\\wesle\\.powbot\\android\\platform-tools\\adb.exe", "-s", "127.0.0.1:5555", "forward", "tcp:61666", "tcp:61666");
        builder.start();
        new ScriptUploader().uploadAndStart("MaddDaeyalt", "maddox", "127.0.0.1:5555", true, true);
    }


    @Override
    public void onStart() {
        System.out.println("We are initializing " + getManifest().name() + ".");
        instance = this;
        startMiningXP = Skill.Mining.experience();
        startMiningLevel = Skill.Mining.realLevel();
        lastXPCount = startMiningXP;
        lastActivityTime = ScriptManager.INSTANCE.getRuntime(true);
        setGUIValues();

        Paint p = new PaintBuilder()
                .x(30)
                .y(65)
                .addString("Current Task: ", () -> (currentStatus))
                .trackSkill(Skill.Mining)
                .trackInventoryItems(Constants.DAEYALT_SHARDS)
                .build();
        addPaint(p);
    }

    private void setGUIValues() {
        int daeyaltCount = getOption("Daeyalt Count");

        Variables.get().daeyaltNeeded = daeyaltCount;
    }

    @Override
    public void poll() {
        TaskSet tasks = new maddox.TaskPriority.TaskSet(
                new Mining()
        );
        Task task = tasks.getValidTask();
        if (task != null) {
            task.execute();
            Condition.sleep(Random.nextInt(200, 500));
        }
        if ((UtilityLogic.timeFromMark(lastActivityTime)) >= 120000) {
            System.out.println("No experience gained for over 2 minutes, failsafe activated. Shutting script down.");
            ScriptManager.INSTANCE.stop();
            return;
        }
        if (Skill.Mining.experience() > lastXPCount) {
            lastXPCount = Skill.Mining.experience();
            lastActivityTime = ScriptManager.INSTANCE.getRuntime(true);
        }
    }


    @Subscribe
    public void canBreak(BreakEvent evt){
        if(UtilityLogic.insidedaeyaltMine()){
            evt.delay(15000);
        }
    }

    @Subscribe
    public void breakEnded(BreakEndedEvent evt){
        if(UtilityLogic.insidedaeyaltMine()){
            lastActivityTime = ScriptManager.INSTANCE.getRuntime(true);
            evt.get_break();
        }
    }


    @Subscribe
    private void onInventChange(InventoryChangeEvent evt){
        if (evt.getQuantityChange() > 0 && Arrays.asList(Constants.Daeyalt).contains(evt.getItemName())) {
            Variables.get().daeyaltGained++;
        }
    }

    @Override
    public void onStop() {
        ScriptManager.INSTANCE.stop();
        currentStatus = "Stopping script...";
        System.out.println("Thanks for using Maddox's " + getManifest().name() + ".");
    }

}