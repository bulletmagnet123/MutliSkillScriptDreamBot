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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.dreambot.api.methods.interactive.GameObjects.all;

@ScriptManifest(name = "MultiSkiller", description = "Weird multi skiller", author = "Bulletmagnet",
        version = 0.0, category = Category.MISC, image = "")
public class Main extends AbstractScript implements ChatListener, MouseListener {
    int WTD;
    long TaskTimer;
    private final Image showImage = getImage("https://pbs.twimg.com/profile_images/689269969519939584/TYr_hQdq_400x400.jpg");
    private final Image hideImage = getImage("https://pbs.twimg.com/profile_images/689269969519939584/TYr_hQdq_400x400.jpg");
    boolean hidePaint = false;
    Rectangle rect = new Rectangle(296, 444, 200, 100);
    Rectangle closePaint = rect;
    Rectangle openPaint = rect;
    Point p;
    Woodcut wc = new Woodcut();
    Fish fish = new Fish();
    Timer time = new Timer();
    private Timer timeRan;
    int beginningXP;
    int beginningLevel;
    int currentXp;
    int xpGained;
    private final BasicStroke stroke1 = new BasicStroke(5);
    private DrawMouseUtil drawMouseUtil = new DrawMouseUtil();
    public int TreesChopped = 0;
    public int FishCaught = 0;



    String DOINGTASK;

    public String getDOINGTASK() {
        return DOINGTASK;
    }

    public void setDOINGTASK(String DOINGTASK) {
        this.DOINGTASK = DOINGTASK;
    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
        }
        return null;
    }
    @Override
    public void onMessage(Message m) {
        if (m.getMessage().contains("You get some")) {
            TreesChopped++;
        }
        if (m.getMessage().contains("You catch some")) {
            FishCaught++;
        }
    }

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
       randomTime = rand.nextLong(3600000L, 4800000L);
        return (randomTime);
    }


    public int RandomTaskChooser() {
        Random random = new Random();
        return random.nextInt(1, 4);
    }

    @Override
    public void onStart(String... params) {

        SkillTracker.start();
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
                setDOINGTASK("FISHING");
                setDOINGTASK("FISHING TASK 1");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Fish();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 2) {
                log("DOING TASK 2");
                time.setRunTime(GetRandomTaskTime());
                setDOINGTASK("CHOPPING WOOD TASK 2");
                while (!time.finished()) {
                    Chopwood();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 3) {
                log("DOING TASK 3 RIGHT NOW SET TO CHOPWOOD");
                setDOINGTASK("CHOPPING WOOD TASK 3");
                time.setRunTime(GetRandomTaskTime());
                while (!time.finished()) {
                    Chopwood();
                    log("Time Remaning" + time.remaining());
                }
            } else if (TASK == 4) {
                log("DOING TASK 4 RIGHT NOW SET TO FISH");
                time.setRunTime(GetRandomTaskTime());
                setDOINGTASK("FISHING TASK 4");
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

    public void onPaint (Graphics2D g) {
            g.drawRect(openPaint.x, openPaint.y, openPaint.width, openPaint.height);
            g.drawImage(this.hideImage, rect.x, rect.y, rect.width, rect.height, null);
            Polygon tile = Map.getPolygon(getLocalPlayer().getTile());
            g.drawPolygon(tile);
            timeRan = new Timer();
            g.setColor(new Color(51, 51, 51, 140));
            g.drawRect(10, 250, 300, 400);
            g.fillRect(10, 250, 300, 400);
            g.setStroke(stroke1);
            g.drawPolygon(tile);
            g.setColor(Color.CYAN);
            g.drawString("Bullets MultiSkiller", 140, 280);
            g.drawString("Time Remaning Tell Next Task = " + ft(time.remaining()), 70, 295);
            g.drawString("WoodCutting level: " + Skills.getRealLevel(Skill.WOODCUTTING), 150, 355);
            g.drawString("Fishing Level: " + Skills.getRealLevel(Skill.FISHING), 150, 365);
            g.drawString("Time Ran: " + timeRan.formatTime(), 150, 385);
            g.drawString("Should use Axe = : " + wc.getAxe(), 150, 400);
            g.drawString("Trees Chopped: " + TreesChopped, 150, 420);
            g.drawString("Fish Caught: " + FishCaught, 150, 430);
            g.drawString("DOING TASK: " + getDOINGTASK(),150 ,440);
            drawMouseUtil.drawRandomMouse(g);
            drawMouseUtil.drawRandomMouseTrail(g);
            int i = 0;
            for (i = 0; i < 10; i++) {
                g.setColor(Color.RED);
                List<GameObject> Tree = all("Willow");
                Tree.get(i).getModel().drawWireFrame(g);
            }
    }

    private String ft ( long duration){
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                .toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                .toMinutes(duration));
        if (days == 0) {
            res = (hours + ":" + minutes + ":" + seconds);
        } else {
            res = (days + ":" + hours + ":" + minutes + ":" + seconds);
        }
        return res;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}


