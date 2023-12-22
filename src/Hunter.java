/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private static String[] kit;
    private String[] collectedTreasures;
    private int gold;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        if (TreasureHunter.getSamuraiMode()) {
            kit = new String[8];
        } else {
            kit = new String[7];
        }
        collectedTreasures = new String[3];
        gold = startingGold;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    public int getGold() {
        return gold;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            gold = 0;
        }
    }

    /**
     * Sets kit to have all items from shop
     */
    public void setKit() {
        kit[0] = Colors.PURPLE + "rope" + Colors.RESET;
        kit[1] = Colors.PURPLE + "water" + Colors.RESET;
        kit[2] = Colors.PURPLE + "machete" + Colors.RESET;
        kit[3] = Colors.PURPLE + "horse" + Colors.RESET;
        kit[4] = Colors.PURPLE + "boat" + Colors.RESET;
        kit[5] = Colors.PURPLE + "boots" + Colors.RESET;
        kit[6] = Colors.PURPLE + "shovel" + Colors.RESET;
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (item.equals("sword") || hasItemInKit("sword")) {
            addItem(item);
            return true;
        }
        if (costOfItem == 0 || gold < costOfItem || hasItemInKit(item)) {
            return false;
        }

        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * Adds a treasure to the hunter's collected treasures.
     *
     * @param treasure The treasure to be added.
     */
    public void addTreasure(String treasure) {
        for (int i = 0; i < collectedTreasures.length; i++) {
            if (collectedTreasures[i] == null) {
                collectedTreasures[i] = treasure;
                System.out.println("You added a " + treasure + " to your collection!");
                if (!(collectedTreasures[0] == null) && !(collectedTreasures[1] == (null)) & !(collectedTreasures[2] == (null))) {
                    System.out.println("Congratulations, you have found the last of the three treasures, you win!");
                    System.exit(0);
                }
                return;
            } else if (collectedTreasures[i].equals(treasure)) {
                System.out.println("You already have a " + treasure + ".");
                return;
            }
        }
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }

        return false;
    }

    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public static boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }

        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
     public String getInventory() {
         String printableKit = "";
         String space = " ";

         for (String item : kit) {
             if (item != null) {
                 printableKit += Colors.PURPLE + item + space + Colors.RESET;
             }
         }

         return printableKit;
     }


    /**
     * @return A string representation of the hunter.
     */
    public String toString() {
        String str = hunterName + " has " + Colors.YELLOW + gold + " gold" + Colors.RESET;
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }
        if(!treasureEmpty()){
            str += "\nTreasures found: " + Colors.PURPLE + getTreasures() + Colors.RESET;
        } else if(treasureEmpty()){
            str += "\nTreasures found: " + Colors.PURPLE + "None" + Colors.RESET;
        }
        return str;
    }

    public String getTreasures(){
        String treasures = Colors.PURPLE + "" + Colors.RESET;
        String space = " ";
        for (String treasure : collectedTreasures) {
            if(treasure != null){
                treasures += treasure + space;
            }
        }
        return treasures;
    }

    private boolean treasureEmpty() {
        for (String string : collectedTreasures) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }

        return -1;
    }
}