import org.apache.tools.ant.Task;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.util.Random;

@ScriptManifest(name = "MultiSkiller", description = "Weird multi skiller", author = "Bulletmagnet",
        version = 0.0, category = Category.MISC, image = "")
public class Main extends AbstractScript {
    int WTD;
    long TaskTimer;

    Woodcut wc = new Woodcut();
    Fish fish = new Fish();
    Timer time = new Timer();

    public void Chopwood() {
        log("running chopwood method");
        if (Inventory.isFull() || !wc.hasEquipment()) {
            wc.bank();
        } else {
            if (wc.hasEquipment()) {
                wc.chop();
            } else {
                wc.bank();
            }
        }
    }

    public void StealFromMan() {

    }

    public void Fish() {
        log("Should fish now");
        log("Running fishing method");
        if (fish.hasEquipment() && !Inventory.isFull()) {
            fish.fish();
        } else {
            fish.bank();
        }
    }
    public long GetRandomTaskTime(){
        long randomTime;
        Random rand = new Random();
       randomTime = rand.nextLong(3600000L, 4800000);
        return (randomTime);
    }


    public int RandomTaskChooser() {
        Random random = new Random();
        return random.nextInt(1, 4);
    }


    public int DoTask(int TASK) {

        return TASK;
    }

    @Override
    public void onStart(String... params) {

        time.setRunTime(GetRandomTaskTime());
        log("ON START TIME REMANING" + time.remaining());
        log("ELAPSED TIME" + time.elapsed());
    }

    @Override
    public int onLoop() {
        log("TIME REMANING" + time.remaining());
        log("ELAPSED TIME" + time.elapsed());
        if (time.finished()){
            int TASK = 0;
            Random rand = new Random();
            TASK = rand.nextInt(1, 4);
            if (TASK == 1) {
                log("DOING TASK 1");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Fish();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 2) {
                log("DOING TASK 2");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Chopwood();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 3) {
                log("DOING TASK 3 RIGHT NOW SET TO CHOPWOOD");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Chopwood();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 4) {
                log("DOING TASK 4 RIGHT NOW SET TO FISH");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Fish();
                    log("Time Remaning" + time.remaining());
                }
            }
        } else {
            log("WENT TO ELSE STATEMENT IDK ITS BROKEN");
        }

        return 0;
    }
}


