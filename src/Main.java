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

    Woodcut wc = new Woodcut();
    Fish fish = new Fish();

    public void Chopwood() {
        if (Inventory.isFull()) {
            wc.bank();
        } else {
            if (wc.hasEquipment()) {
                wc.chop();
            } else {
                wc.bank();
            }
        }
    }
    public void StealFromMan(){

    }
    public void Fish(){
        if (fish.hasEquipment() && !Inventory.isFull()){
            fish.fish();
        } else {
            fish.bank();
        }

    }
    public enum STATE{
        STOP, LOGOUT, FISH, WOODCUT, STEAL, BANK,
    }



    @Override
    public int onLoop() {
        int WTDbound = 4;
        Random rand = new Random();
        int WTD = rand.nextInt(WTDbound);
        log(WTDbound);
        if (WTD == 1){
            Chopwood();
        } else if (WTD == 2){
            Fish();
        } else if (WTD == 3){
            sleep(267000, 545000);
        } else if (WTD == 4){
            log("Should do task 4");
        }
        return 0;
    }
}

