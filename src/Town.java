import java.util.Random;
/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private static boolean lose;
    private boolean searchedForTreasure;
    private String[] possibleTreasures = {"crown", "trophy", "gem", "dust"};
    private String foundTreasure;
    private Random random;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        lose = false;
        searchedForTreasure = false;
        random = new Random();
    }

    public String getLatestNews() {
        return printMessage;
    }
    public static boolean getLose() {
        return lose;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        int index = random.nextInt(possibleTreasures.length);
        foundTreasure = possibleTreasures[index];
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void huntForTreasure() {
        if (searchedForTreasure) {
            System.out.println("You have already searched this town.");
            return;
        }
        if (!foundTreasure.equals("dust")) {
            System.out.println("You found a " + foundTreasure + "!");
            hunter.addTreasure(foundTreasure);
        } else {
            System.out.println("You found dust. Nothing special.");
        }
        searchedForTreasure = true;
    }


    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
             if (Hunter.hasItemInKit("sword")) {
                 printMessage += "The brawler, seeing your sword, realizes he picked a losing fight and gives you his gold";
                 hunter.changeGold(goldDiff);
             } else if ((Math.random() > noTroubleChance)) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += Colors.YELLOW + "\nYou won the brawl and receive " + goldDiff + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
             } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                if (goldDiff > hunter.getGold()) {
                    printMessage += "\nYou couldn't pay up so your opponent murdered you...better luck next time!";
                    lose = true;
                } else {
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public void digTreasure(){
        int chance = random.nextInt(2 + 1) - 1;
        if (chance == 1){
            int gold = random.nextInt(20 - 1) + 1;
            System.out.println("You dug up " + gold + " gold!");
            hunter.changeGold(gold);
        } else{
            System.out.println("You dug but only found dirt. ");
        }
    }

    public String toString() {
        return Colors.CYAN + "This nice little town is surrounded by " + terrain.getTerrainName() + "." + Colors.RESET;
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .2) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .3) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .4) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .6) {
            return new Terrain("Desert", "Water");
        } else  if (rnd < .8){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    private String treasure() {
        double rnd = Math.random();
        if (rnd < .2) {
            return "crown";
        } else if (rnd < .4) {
            return  "trophy";
        } else if (rnd < .6) {
            return "gem";
        } else {
            return "dust";
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}