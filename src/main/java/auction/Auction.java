package auction;

import account.ChatRoom;
import account.Customer;
import account.Supplier;
import cart.Cart;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptionalMassage.ExceptionalMassage;
import log.CustomerLog;
import product.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Auction {
    private static final ArrayList<Auction> ALL_AUCTIONS = new ArrayList<>();
    private static int allAuctionsCount = 0;
    public static Runnable endChecker = () -> {
        for (Auction auction : ALL_AUCTIONS) {
            auction.checkForEnd();
        }
    };

    private final String identifier;
    private final String chatRoomIdentifier;
    private final Product product;
    private final Supplier supplier;
    private Customer highestPromoter;
    private Integer highestPromotion;
    private final Date end;
    private int wage;

    public Auction(Product product, Supplier supplier, long endLong, int wage) {
        this.identifier = Auction.generateIdentifier();
        this.chatRoomIdentifier = new ChatRoom().getChatRoomId();
        this.product = product;
        this.supplier = supplier;
        this.highestPromoter = null;
        this.highestPromotion = null;
        this.end = new Date(endLong);
        this.wage = wage;
        ALL_AUCTIONS.add(this);
        allAuctionsCount++;
    }

    public Auction(String identifier, String chatRoomIdentifier, Product product, Supplier supplier,
                   Customer highestPromoter, Integer highestPromotion, Date end, int wage) {
        this.identifier = identifier;
        this.chatRoomIdentifier = chatRoomIdentifier;
        this.product = product;
        this.supplier = supplier;
        this.highestPromoter = highestPromoter;
        this.highestPromotion = highestPromotion;
        this.end = end;
        this.wage = wage;
        ALL_AUCTIONS.add(this);
        allAuctionsCount++;
    }

    public Auction(String json) {
        this.identifier = identifier;
        this.chatRoomIdentifier = chatRoomIdentifier;
        this.product = product;
        this.supplier = supplier;
        this.highestPromoter = highestPromoter;
        this.highestPromotion = highestPromotion;
        this.end = end;
    }

    public static Runnable getEndChecker() {
        return endChecker;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getChatRoomIdentifier() {
        return chatRoomIdentifier;
    }

    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Customer getHighestPromoter() {
        return highestPromoter;
    }

    public Integer getHighestPromotion() {
        return highestPromotion;
    }

    public Date getEnd() {
        return end;
    }

    public int getWage() {
        return wage;
    }

    private static String generateIdentifier() {
        return "T34AC" + String.format("%015d", allAuctionsCount + 1);
    }

    public static Auction getAuctionByIdentifier(String identifier) {
        for (Auction auction : ALL_AUCTIONS) {
            if (auction.getIdentifier().equals(identifier)) {
                return auction;
            }
        }
        return null;
    }

    public static boolean isThisProductInAuction(Product product, Supplier supplier) {
        for (Auction auction : ALL_AUCTIONS) {
            if (auction.getProduct().equals(product) && auction.getSupplier().equals(supplier)) {
                return true;
            }
        }
        return false;
    }

    public void promote(Customer customer, int promotionAmount, int minimumCreditRequired) throws ExceptionalMassage {
        if (System.currentTimeMillis() >= end.getTime()) {
            throw new ExceptionalMassage("Auction ended.");
        }
        if (customer.getCredit() < promotionAmount + minimumCreditRequired) {
            throw new ExceptionalMassage("You don't have enough money. required" + promotionAmount + " + " +
                    minimumCreditRequired + ".");
        }
        if (highestPromoter != null) {
            if (highestPromotion >= promotionAmount) {
                throw new ExceptionalMassage("You must promote higher than " + highestPromotion + ".");
            }
            highestPromoter.setCredit(highestPromoter.getCredit() + highestPromotion);
        }
        customer.setCredit(customer.getCredit() - promotionAmount);
        highestPromoter = customer;
        highestPromotion = promotionAmount;
        Objects.requireNonNull(ChatRoom.getChatRoomById(chatRoomIdentifier)).getJoinedAccounts().add(customer);
    }

    public void end() {
        if (highestPromoter != null) {
            try {
                new CustomerLog(this, wage);
            } catch (ExceptionalMassage exceptionalMassage) {
                System.err.println("Couldn't add log.");
            }
        }
        //product out
    }

    public void checkForEnd() {
        if (System.currentTimeMillis() >= end.getTime()) {
            end();
        }
    }

    public String toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();

        return jsonObject.toString();
    }

    public Auction convertJsonStringToAuction(String json) {
        return new Auction(json);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(identifier, auction.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
