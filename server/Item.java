/**
 * Represents an item in the auction.
 */
public class Item {
    /**
     * The name of the item.
     */
    private String name = "";
    /**
     * The last bid for the item.
     */
    private double bid = 0.0;
    /**
     * The IP address of the last bidder.
     */
    private String bidderIP = null;

    /**
     * Constructs a new item with the specified name.
     *
     * @param name the name of the item.
     */
    public Item(String name) {
        this.name = name;
        bid = 0.0;
        bidderIP = null;
    }

    /**
     * Registers a new bid if it is a valid (bid > current bid).
     *
     * @param bid      the last bid for the item.
     * @param bidderIP the bidder IP address.
     * @return true if the bid was accepted, otherwise false.
     */
    public boolean newBid(double bid, String bidderIP) {
        if (bid > this.bid) {
            this.bid = bid;
            this.bidderIP = bidderIP;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of this item.
     *
     * @return the string representation of this item.
     */
    @Override
    public String toString() {
        return String.format("%s : %.2f : %s", name, bid, (bid == 0 ? "<no bids>" : bidderIP));
    }
}
