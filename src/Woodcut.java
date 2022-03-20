import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import java.awt.*;


import static org.dreambot.api.Client.getLocalPlayer;
import static org.dreambot.api.methods.MethodProvider.*;

public class Woodcut {
    Area WillowLevel = new Area(3056, 3250, 3063, 3255);
    Area TreeLevel = new Area(3069, 3262, 3087, 3274);
    Area OakLevel = new Area(3102, 3240, 3098, 3247);

    String axe = null;



    public String BankOrDrop;



    public void bank() {
        setAxe();
        if (hasEquipment() && !Inventory.isFull()) {
            chop();
        } else if (!Inventory.contains(getAxe())) {

            Walking.walk(Bank.getClosestBankLocation());
            sleep(1000, 2500);
            Bank.openClosest();
            sleep(500, 1200);
            Bank.depositAllItems();
            sleep(500, 1200);
            Bank.withdraw(getAxe(), 1);
            sleep(500, 1000);
            Bank.close();
        } else if (Inventory.isFull()) {

            Walking.walk(Bank.getClosestBankLocation());
            sleep(1000, 2500);
            Bank.openClosest();
            sleep(500, 1200);
            Bank.depositAllExcept(getAxe());
            Bank.close();
            Walking.walk(Bank.getClosestBankLocation());
        } else if (Inventory.onlyContains(getAxe())) {
            chop();
        } else {
            chop();
        }
    }

    public void drop() {
        sleep(500, 1300);
        Inventory.dropAllExcept(axe);
        sleep(1000, 2500);
    }

    public void chop() {
        GameObject TREE = null;
        int wcLvl = Skills.getRealLevel(Skill.WOODCUTTING);
        Area CHOPHERE = null;
        if (wcLvl <= 14) {
            CHOPHERE = TreeLevel;
            TREE = GameObjects.closest("Tree");
            log("Set CHOPEHERE to Dryanor trees");
        }
        if (wcLvl >= 15) {
            CHOPHERE = OakLevel;
            TREE = GameObjects.closest("Oak");
            log("Set CHOPEHERE to Dryanor OAKS");
        }
        if (wcLvl >= 30) {
            CHOPHERE = WillowLevel;
            TREE = GameObjects.closest("Willow");
            log("Set CHOPEHERE to WILLOWS");
        }
        if (Inventory.isFull()) {
            if (BankOrDrop == "BANK") {
                bank();
            } else if (BankOrDrop == "DROP") {
                drop();
            }
        }
        if (!CHOPHERE.contains(getLocalPlayer())) {
            sleep(1000, 2500);
            Walking.walk(CHOPHERE);
            sleep(1280, 2700);
        } else if (CHOPHERE.contains(getLocalPlayer())) {
            if (TREE != null && CHOPHERE.contains(TREE)) {
                sleep(1000, 1500);
                TREE.interact("Chop down");
                sleep(1500, 2500);
                if (getLocalPlayer().isAnimating()){
                    while (getLocalPlayer().isAnimating()){
                        Mouse.moveMouseOutsideScreen();
                    }
                }
                sleepUntil(() -> getLocalPlayer().getAnimation() == -1, 240000);
            }
        }

    }


    public boolean hasEquipment() {
        setAxe();
        boolean yes;
        if (Inventory.contains(getAxe())) {
            yes = true;
        } else {
            yes = false;
        }
        return yes;
    }

    public String getAxe() {
        return axe;
    }

    public void setAxe() {
        int wcLvl = Skills.getRealLevel(Skill.WOODCUTTING);
        if (wcLvl <= 5) {
            axe = "Bronze axe";
        }
        if (wcLvl >= 6) {
            axe = "Steel axe";
        }
        if (wcLvl >= 11) {
            axe = "Black axe";
        }
        if (wcLvl >= 21) {
            axe = "Mithril axe";
        }
        if (wcLvl >= 31) {
            axe = "Adamant axe";
        }
        if (wcLvl >= 41) {
            axe = "Rune axe";
        }
        if (wcLvl >= 61) {
            axe = "Dragon axe";
        }
    }

}
