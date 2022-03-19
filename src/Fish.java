import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.message.Message;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.dreambot.api.Client.getLocalPlayer;
import static org.dreambot.api.methods.MethodProvider.*;

public class Fish {

    public boolean BankOrDrop = true;
    private Timer timeRan;
    private final Image paintBackground = getImage("https://i.imgur.com/bWrIucb.png");
    private DrawMouseUtil drawMouseUtil = new DrawMouseUtil();
    int things_caught = 0;
    final Area fishArea = new Area(3244, 3152, 3238, 3143);
    int beginningXP;
    int currentXp;
    int xpGained;
    private final Color color1 = new Color(51, 51, 51, 147);
    private final Color color2 = new Color(138, 54, 15);
    private final Color color3 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(5);


    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onStart() {

        SkillTracker.start(Skill.FISHING);
        beginningXP = Skills.getExperience(Skill.FISHING);
        Timer TTLV = new Timer();
        timeRan = new Timer();
        while (Inventory.isFull()) {
            bank();
        }
        if (fishArea.contains(getLocalPlayer()) && hasEquipment() == true && !Inventory.isFull()) {
            fish();
        } else {

        }
    }


    public void fish() {
        if (fishArea.contains(getLocalPlayer()) && hasEquipment() == true && !Inventory.isFull()) {
            NPC fishingSpot = NPCs.closest("Fishing spot");
            if (fishingSpot != null) {
                log("Cast Net.");
                sleepUntil(() -> getLocalPlayer().getAnimation() == -1, 15000);
                fishingSpot.interact("Net");
                sleep(4000, 6000);
                Mouse.moveMouseOutsideScreen();
                sleepUntil(() -> getLocalPlayer().getAnimation() == -1, 15000);
            }
        }
        while (!fishArea.contains(getLocalPlayer()) && Inventory.contains("Small fishing net")) {
            Walking.walk(fishArea);
            sleep(1000, 3000);
        }

    }

    public void bank() {
        if (BankOrDrop) {
            if (hasEquipment() && !Inventory.isFull()) {
                fish();
            } else if (Inventory.isEmpty()) {
                Walking.walk(org.dreambot.api.methods.container.impl.bank.Bank.getClosestBankLocation());
                Walking.walk(BankLocation.LUMBRIDGE.getCenter());
                sleep(1000, 2500);
                Bank.openClosest();
                sleep(500, 1200);
                Bank.withdraw("Small fishing net", 1);
                sleep(500, 1000);
                Bank.close();
                hasEquipment();
                fish();
            }
            if (Inventory.isFull()) {
                Walking.walk(org.dreambot.api.methods.container.impl.bank.Bank.getClosestBankLocation());
                Walking.walk(BankLocation.LUMBRIDGE.getCenter());
                sleep(1000, 2500);
                Bank.openClosest();
                sleep(500, 1200);
                Bank.depositAllExcept("Small fishing net");
                Bank.close();
            }
        } else {
            log("running drop method in main bank method");
            drop();
        }
    }

    public boolean hasEquipment() {
        boolean yes;
        if (Inventory.contains("Small fishing net")) {
            yes = true;
        } else {
            yes = false;
        }
        return yes;
    }

    public void drop() {
        log("running drop method");
        if (Inventory.isFull() && !BankOrDrop) {
            Inventory.dropAllExcept("Small fishing net");
        } else if (Inventory.isFull() && BankOrDrop) {
            bank();
        } else {
            fish();
        }
    }
}
