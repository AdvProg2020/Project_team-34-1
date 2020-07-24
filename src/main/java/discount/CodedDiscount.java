package discount;

import account.Customer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.CodedDiscountDataBase;
import server.communications.Utils;

import java.util.*;

/**
 * @author soheil
 * @since 0.0.1
 */
public class CodedDiscount extends Discount{
    private static final String LETTERS_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final ArrayList<CodedDiscount> codedDiscounts = new ArrayList<>();
    private final String discountCode;
    private int maxDiscountAmount;
    private final HashMap<Customer, Integer> usedDiscountPerCustomer;
    private final HashMap<Customer, Integer> maximumNumberOfUsagePerCustomer;
    private final ArrayList<Customer> customers;

    public CodedDiscount(String json) {
        super(new Date(Long.parseLong((new JsonParser().parse(json).getAsJsonObject()).get("start").getAsString())),
                new Date(Long.parseLong((new JsonParser().parse(json).getAsJsonObject()).get("end").getAsString())),
                Integer.parseInt((new JsonParser().parse(json).getAsJsonObject()).get("percent").getAsString()));
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        this.discountCode = jsonObject.get("discountCode").getAsString();
        this.maxDiscountAmount = Integer.parseInt(jsonObject.get("maxDiscountAmount").getAsString());
        this.usedDiscountPerCustomer = Utils.convertJsonElementCustomerToIntegerHashMap(jsonObject.get("usedDiscountPerCustomer"));
        this.maximumNumberOfUsagePerCustomer = Utils.convertJsonElementCustomerToIntegerHashMap(jsonObject.get("maximumNumberOfUsagePerCustomer"));
        this.customers = Utils.convertJsonElementToCustomerArrayList(jsonObject.get("customers"));
    }

    public String toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        jsonObject.add("start", jsonParser.parse(Utils.convertObjectToJsonString(String.valueOf(start.getTime()))));
        jsonObject.add("end", jsonParser.parse(Utils.convertObjectToJsonString(String.valueOf(end.getTime()))));
        jsonObject.add("percent", jsonParser.parse(Utils.convertObjectToJsonString(String.valueOf(percent))));
        jsonObject.add("discountCode", jsonParser.parse(Utils.convertObjectToJsonString(discountCode)));
        jsonObject.add("maxDiscountAmount", jsonParser.parse(Utils.convertObjectToJsonString(String.valueOf(
                maxDiscountAmount))));
        jsonObject.add("usedDiscountPerCustomer", Utils.convertCustomerToIntegerHashMapToJsonElement(usedDiscountPerCustomer));
        jsonObject.add("maximumNumberOfUsagePerCustomer", Utils.convertCustomerToIntegerHashMapToJsonElement(maximumNumberOfUsagePerCustomer));
        jsonObject.add("customers", Utils.convertCustomerArrayListToJsonElement(customers));
        return jsonObject.toString();
    }

    public CodedDiscount(String code, Date start, Date end, int percent, int maxDiscountAmount,
                         HashMap<Customer, Integer> maximumNumberOfUsagePerCustomer) {
        super(start, end, percent);
        this.discountCode = code;
        this.maxDiscountAmount = maxDiscountAmount;
        this.maximumNumberOfUsagePerCustomer = maximumNumberOfUsagePerCustomer;
        usedDiscountPerCustomer = new HashMap<>();
        customers = new ArrayList<>();
        for (Customer customer : maximumNumberOfUsagePerCustomer.keySet()) {
            usedDiscountPerCustomer.put(customer, 0);
            customers.add(customer);
        }
        codedDiscounts.add(this);
        CodedDiscountDataBase.add(this);
    }

    // Added by rpirayadi
    public CodedDiscount(Date start, Date end, int percent, String discountCode, int maxDiscountAmount, HashMap<Customer,
            Integer> usedDiscountPerCustomer, HashMap<Customer,Integer > maximumNumberOfUsagePerCustomer) {
        super(start, end, percent);
        this.discountCode = discountCode;
        this.maxDiscountAmount = maxDiscountAmount;
        this.usedDiscountPerCustomer = usedDiscountPerCustomer;
        this.maximumNumberOfUsagePerCustomer = maximumNumberOfUsagePerCustomer;
        customers = new ArrayList<>();
        customers.addAll(maximumNumberOfUsagePerCustomer.keySet());
        codedDiscounts.add(this);
    }

    public static String codeGenerator(){
        Random rand = new Random();
        int upperBound = LETTERS_SET.length()-1;
        StringBuilder code = new StringBuilder();
        for(int i = 0;i < 8;i++){
            code.append(LETTERS_SET.charAt(rand.nextInt(upperBound)));
        }
        return code.toString();
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public int getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public HashMap<Customer, Integer> getUsedDiscountPerCustomer() {
        return usedDiscountPerCustomer;
    }

    public HashMap<Customer, Integer> getMaximumNumberOfUsagePerCustomer() {
        return maximumNumberOfUsagePerCustomer;
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public void removeCustomer(Customer customer){
        customers.remove(customer);
    }

    public void addUsedCountForCustomer(Customer customer){
        usedDiscountPerCustomer.put(customer, usedDiscountPerCustomer.get(customer)+1);
        CodedDiscountDataBase.update(this);
    }

    public boolean canCustomerUseCode(Customer customer){
        Date present = new Date(System.currentTimeMillis());
        return !usedDiscountPerCustomer.get(customer).equals(maximumNumberOfUsagePerCustomer.get(customer)) && (!present.before(start)) && (!present.after(end));
    }

    public static ArrayList<CodedDiscount> getCodedDiscounts() {
        return codedDiscounts;
    }

    public static void removeCodeFromList(CodedDiscount codedDiscount){
        codedDiscounts.remove(codedDiscount);
        CodedDiscountDataBase.delete(codedDiscount.getDiscountCode());
    }

    public static CodedDiscount getCodedDiscountByCode(String code){
        ///signed as static by AA
        for (CodedDiscount codedDiscount : codedDiscounts) {
            if(codedDiscount.getDiscountCode().equals(code)){
                return codedDiscount;
            }
        }
        return null;
    }

    public int getRemainedNumberByCustomer(Customer customer){
        for (Customer eachCustomer : usedDiscountPerCustomer.keySet()) {
            if(eachCustomer.equals(customer))
                return maximumNumberOfUsagePerCustomer.get(customer) - usedDiscountPerCustomer.get(customer);
        }
        return 0;
    }

    public void setMaxDiscountAmount(int maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public int discountAmountFor(int initialAmount) {
        //Aryan
        return Integer.min((initialAmount * percent) / 100, maxDiscountAmount);
    }

    public static CodedDiscount convertJsonStringToCodedDiscount(String jsonString){
        return new CodedDiscount(jsonString);
//        return (CodedDiscount) Utils.convertStringToObject(jsonString, "discount.CodedDiscount");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodedDiscount that = (CodedDiscount) o;
        return Objects.equals(discountCode, that.discountCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountCode);
    }

    @Override
    public String toString() {

        StringBuilder returning =
                new StringBuilder("Discount Code :" + discountCode + '\n' +
                        ", Max discount percent: " + maxDiscountAmount + '\n' +
                        ", Percent: " + percent + '\n' +
                        ", Customers :\n");
        for (Customer customer : customers) {
            returning.append(customer.getUserName()).append("\n");
        }
        return returning.toString();
    }
}
