import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.NPC;
import static org.dreambot.api.Client.getLocalPlayer;
import static org.dreambot.api.methods.MethodProvider.*;

public class Fish {

    final Area fishArea = new Area(3244, 3152, 3238, 3143);

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
        } else if (!fishArea.contains(getLocalPlayer()) && Inventory.onlyContains("Small fishing net")) {
            Walking.walk(fishArea.getRandomTile());
            sleep(1200, 1890);
        } else {
            bank();
        }

    }

    public void bank() {
        if (hasEquipment() && !Inventory.isFull()) {
            fish();
        } else if (!Inventory.contains("Small fishing net")) {
            Walking.walk(Bank.getClosestBankLocation());
            sleep(1000, 2500);
            Bank.openClosest();
            sleep(500, 1200);
            Bank.depositAllItems();
            sleep(500, 1200);
            Bank.withdraw("Small fishing net", 1);
            sleep(500, 1000);
            Bank.close();
        } else if (Inventory.isFull()) {

            Walking.walk(Bank.getClosestBankLocation());
            sleep(1000, 2500);
            Bank.openClosest();
            sleep(500, 1200);
            Bank.depositAllExcept("Small fishing net");
            Bank.close();
            Walking.walk(Bank.getClosestBankLocation());
        } else if (Inventory.onlyContains("Small fishing net")) {
            fish();
        } else {
            fish();
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

}
