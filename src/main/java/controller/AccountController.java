package controller;

import account.*;
import cart.Cart;
import cart.ProductInCart;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import communications.ControllerSource;
import communications.Utils;
import exceptionalMassage.ExceptionalMassage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import product.Product;

import java.io.IOException;
import java.util.ArrayList;

public class AccountController {
    private static final long WEEK = 7 * 24 * 60 * 60000;

    private static final int BOUND = 100;

    private final Controller mainController;

    public AccountController(Controller mainController) {
        this.mainController = mainController;
    }

    public JsonElement communication(String function, JsonArray inputs) throws ExceptionalMassage {
        return new JsonParser().parse(mainController.
                communication(function, inputs, ControllerSource.ACCOUNT_CONTROLLER).getContent());
    }

    public Account getAccount() throws ExceptionalMassage {
        JsonElement jsonElement = communication("getAccount", new JsonArray());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.get(0).getAsString().equals("Customer"))
            return Customer.convertJsonStringToCustomer(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supervisor"))
            return Supervisor.convertJsonStringToSupervisor(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supplier"))
            return Supplier.convertJsonStringToSupplier(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supporter"))
            return Supporter.convertJsonStringToSupporter(jsonArray.get(1).getAsString());
        return null;
    }

    public boolean hasSomeOneLoggedIn() throws ExceptionalMassage {
        JsonElement response = communication("hasSomeOneLoggedIn", new JsonArray());
        return response.getAsBoolean();
    }

    public String loggedInAccountType() throws ExceptionalMassage {
        JsonElement jsonElement = communication("loggedInAccountType", new JsonArray());
        return jsonElement.getAsString();
    }

    public void controlCreateAccount(String type, String username, String password, String firstName, String lastName,
                                     String email, String phoneNumber, String nameOfCompany, String bankUsername,
                                     String bankPassword, boolean alsoBank) throws ExceptionalMassage {
        if (!type.equals("customer") && !type.equals("supervisor") && !type.equals("supplier") && !type.equals("supporter"))
            throw new ExceptionalMassage("Invalid account type");
        if (username.trim().length() == 0)
            throw new ExceptionalMassage("Username can't be empty");
        if (!username.matches("\\w+"))
            throw new ExceptionalMassage("Invalid username");
        if (password.trim().length() == 0)
            throw new ExceptionalMassage("Password can't be empty");
        if (!password.matches("\\w+"))
            throw new ExceptionalMassage("Invalid password");
        if (firstName.trim().length() == 0)
            throw new ExceptionalMassage("First name can't be empty");
        if (lastName.trim().length() == 0)
            throw new ExceptionalMassage("Last name can't be empty");
        if (email.trim().length() == 0)
            throw new ExceptionalMassage("Email can't be empty");
        if (phoneNumber.trim().length() == 0)
            throw new ExceptionalMassage("Phone number can't be empty");
        if (type.equals("supplier") && nameOfCompany.trim().length() == 0)
            throw new ExceptionalMassage("Name of company cannot be empty");
        if (alsoBank) {
            if (bankUsername.trim().length() == 0)
                throw new ExceptionalMassage("Bank username can't be empty");
            if (bankPassword.trim().length() == 0)
                throw new ExceptionalMassage("Bank password can't be empty");
        }
        JsonArray inputs = new JsonArray();
        inputs.add(type);
        inputs.add(username);
        inputs.add(password);
        inputs.add(firstName);
        inputs.add(lastName);
        inputs.add(email);
        inputs.add(phoneNumber);
        inputs.add(nameOfCompany);
        inputs.add(bankUsername);
        inputs.add(bankPassword);
        inputs.add(String.valueOf(alsoBank));
        communication("controlCreateAccount", inputs);
    }

    public void controlLogin(String username, String password) throws ExceptionalMassage {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(username);
        jsonArray.add(password);
        communication("controlLogin", jsonArray);
    }

    public void controlLogout() throws ExceptionalMassage {
        communication("controlLogout", new JsonArray());
    }

    public void controlRequestDynamicPassword(String username) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(username);
        communication("controlRequestDynamicPassword", inputs);
    }

    public void controlAuthenticate(String dynamicPass) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(dynamicPass);
        communication("controlAuthenticate", inputs);
    }

    public String controlViewPersonalInfo() throws ExceptionalMassage {
        JsonElement response = communication("controlViewPersonalInfo", new JsonArray());
        return response.getAsString();
    }

    public int controlViewBalance() throws ExceptionalMassage {
        JsonElement response = communication("controlViewBalance", new JsonArray());
        return response.getAsInt();
    }

    public void controlEditField(String field, String newValue) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(field);
        input.add(newValue);
        communication("controlEditField", input);
    }

    public void editAllFields(String name, String familyName, String email, String phoneNumber, String password,
                              String nameOfCompany) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(name);
        input.add(familyName);
        input.add(email);
        input.add(phoneNumber);
        input.add(password);
        input.add(nameOfCompany);
        communication("editAllFields", input);
    }

    public String controlGetListOfAccounts() throws ExceptionalMassage {
        JsonElement accounts = communication("controlGetListOfAccounts", new JsonArray());
        return accounts.getAsString();
    }

    public ArrayList<String> controlGetListOfAccountUserNames() throws ExceptionalMassage {
        JsonElement response = communication("controlGetListOfAccountUserNames", new JsonArray());
        ArrayList<String> allUsername = Utils.convertJsonElementToStringArrayList(response);
        return allUsername;
    }

    public String controlViewUserInfo(String username) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(username);
        JsonElement account = communication("controlViewUserInfo", input);
        return account.getAsString();
    }

    public void controlDeleteUser(String username) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(username);
        communication("controlDeleteUser", input);
    }

    public String controlViewCompanyInfo() throws ExceptionalMassage {
        JsonElement companyInfo = communication("controlViewCompanyInfo", new JsonArray());
        return companyInfo.getAsString();
    }

    /*public String controlGetListOfProductsForThisSupplier() throws  ExceptionalMassage{
        Account account = mainController.getAccount();
        if (!(account instanceof Supplier)) {
            throw new ExceptionalMassage("Sign in as a supplier");
        }
        else {
            ArrayList<Product> products = Product.getProductForSupplier((Supplier)account);
            String.valueOf(products);
            StringBuilder result = new StringBuilder("");
            for (Product product : products) {
                result.append(product.getName()).append("   ").append(product.getProductId()).append("\n");
            }
            return String.valueOf(result);
        }
    }*/

    public void controlAddToCart(String productId, String supplierNameOfCompany) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(productId);
        input.add(supplierNameOfCompany);
        communication("controlAddToCart", input);
    }

    public void increaseProductQuantity(String productId, String supplierNameOfCompany) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(productId);
        input.add(supplierNameOfCompany);
        communication("increaseProductQuantity", input);
    }

    public void decreaseProductQuantity(String productId, String supplierNameOfCompany) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(productId);
        input.add(supplierNameOfCompany);
        communication("decreaseProductQuantity", input);
    }

    public Cart controlViewCart() throws ExceptionalMassage {
        JsonElement cart = communication("controlViewCart", new JsonArray());
        return Cart.convertJsonStringToCart(cart.toString());
    }

    public String controlViewCartInfo() throws ExceptionalMassage {
        JsonElement cartString = communication("controlViewCartInfo", new JsonArray());
        return cartString.getAsString();
    }

    public void controlSubmitShippingInfo(String firstName, String lastName, String city, String address,
                                          String postalCode, String phoneNumber) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(firstName);
        inputs.add(lastName);
        inputs.add(city);
        inputs.add(address);
        inputs.add(postalCode);
        inputs.add(phoneNumber);
        communication("controlSubmitShippingInfo", inputs);
    }

    public void controlRemoveShippingInfo() throws ExceptionalMassage {
        communication("controlRemoveShippingInfo", new JsonArray());
    }

    public void controlSubmitDiscountCode(String discountCode) throws ExceptionalMassage {
        JsonArray input = new JsonArray();
        input.add(discountCode);
        communication("controlSubmitDiscountCode", input);
    }

    public void controlRemoveDiscountCode() throws ExceptionalMassage {
        communication("controlRemoveDiscountCode", new JsonArray());
    }

    public void checkFinalizeOrder() throws ExceptionalMassage{
        communication("checkFinalizeOrder", new JsonArray());
    }

    public boolean finalizeOrder() throws ExceptionalMassage {
        return Boolean.parseBoolean(communication("finalizeOrder", new JsonArray()).getAsString());
    }

    public void controlGetLinksToDownloadFileProducts() throws ExceptionalMassage {
        JsonArray jsonArray =communication("controlGetLinksToDownloadFileProducts", new JsonArray()).getAsJsonArray();
        JsonObject jsonObject;
        for (JsonElement jsonElement : jsonArray) {
            jsonObject =  jsonElement.getAsJsonObject();
            controlInsiderDownloadProductWithP2P(jsonObject.get("filePath").getAsString(), jsonObject.get("port").getAsInt());
        }
    }

    private void controlInsiderDownloadProductWithP2P (String filePath, int port) throws ExceptionalMassage{
            System.out.println("daram mifrestam be khoda");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mainController.getPeerNode().sendRequest(filePath, "localhost", port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    public String getAccountUsername() throws ExceptionalMassage {
        return communication("getAccountUsername", new JsonArray()).getAsString();
    }

    public String getAccountName() throws ExceptionalMassage {
        return communication("getAccountName", new JsonArray()).getAsString();
    }

    public String getAccountFamilyName() throws ExceptionalMassage {
        return communication("getAccountFamilyName", new JsonArray()).getAsString();
    }

    public String getAccountEmail() throws ExceptionalMassage {
        return communication("getAccountEmail", new JsonArray()).getAsString();
    }

    public String getAccountPhoneNumber() throws ExceptionalMassage {
        return communication("getAccountPhoneNumber", new JsonArray()).getAsString();
    }

    public int getAccountCredit() throws ExceptionalMassage {
        return Integer.parseInt(communication("getAccountCredit", new JsonArray()).getAsString());
    }

    public String getAccountNameOfCompany() throws ExceptionalMassage {
        return communication("", new JsonArray()).getAsString();
    }

    public ArrayList<String> getCustomerLogs() throws ExceptionalMassage {
        return Utils.convertJsonElementToStringArrayList(communication("getCustomerLogs", new JsonArray()));
    }

    public ArrayList<String> getSupplierLogs() throws ExceptionalMassage {
        return Utils.convertJsonElementToStringArrayList(communication("getSupplierLogs", new JsonArray()));
    }

    public ArrayList<String> getSupervisorLogs() throws ExceptionalMassage {
        return Utils.convertJsonElementToStringArrayList(communication("getSupervisorLogs", new JsonArray()));
    }

    public String getCustomerLogById(String id) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        return communication("getCustomerLogById", inputs).getAsString();
    }

    public String getSupplierLogById(String id) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        return communication("getSupplierLogById", inputs).getAsString();
    }

    public boolean proceedCustomerLog(String id) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        return Boolean.parseBoolean(communication("proceedCustomerLog", inputs).getAsString());
    }

    public boolean isLogProcessable(String id) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        return Boolean.parseBoolean(communication("isLogProcessable", inputs).getAsString());
    }

    public int numberOfProductInCart(ProductInCart productInCart) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(productInCart.getIdentifier());
        return Integer.parseInt(communication("numberOfProductInCart", inputs).getAsString());
    }

    public void controlClearCart() throws ExceptionalMassage {
        communication("controlClearCart", new JsonArray());
    }

    public ArrayList<Product> controlGetRequestForLoggedInSupplier() throws ExceptionalMassage {
        return Utils.convertJsonElementToProductArrayList(communication("controlGetRequestForLoggedInSupplier",
                new JsonArray()));
    }

    public void controlCreateCodedDiscountForLoggedInCustomer() throws ExceptionalMassage {
        communication("controlCreateCodedDiscountForLoggedInCustomer", new JsonArray());
    }

    public ArrayList<Customer> getCustomerObservableList() throws ExceptionalMassage {
        ArrayList<Customer> arrayList = Utils.convertJsonElementToCustomerArrayList(communication(
                "getCustomerObservableList", new JsonArray()));
        return arrayList;
    }

    public ArrayList<Supplier> getSupplierObservableList() throws ExceptionalMassage {
        ArrayList<Supplier> arrayList = Utils.convertJsonElementToSupplierArrayList(communication(
                "getSupplierObservableList", new JsonArray()));
        return arrayList;
    }

    public ArrayList<Supervisor> getSupervisorObservableList() throws ExceptionalMassage {
        ArrayList<Supervisor> arrayList = Utils.convertJsonElementToSupervisorArrayList(communication(
                "getSupervisorObservableList", new JsonArray()));
        return arrayList;
    }

    public ArrayList<Supporter> getSupporterObservableList() throws ExceptionalMassage{
        ArrayList<Supporter> arrayList = Utils.convertJsonElementToSupporterArrayList(communication(
                "getSupporterObservableList", new JsonArray()));
        return arrayList;
    }

    public Supplier getSupplierByCompanyName(String companyName) throws ExceptionalMassage {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(companyName);
        return Supplier.convertJsonStringToSupplier(communication("getSupplierByCompanyName",jsonArray).toString());
    }

    public Account getAccountByUsernameWithinAvailable(String username) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(username);
        JsonElement jsonElement = communication("getAccountByUsernameWithinAvailable", inputs);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.get(0).getAsString().equals("Customer"))
            return Customer.convertJsonStringToCustomer(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supervisor"))
            return Supervisor.convertJsonStringToSupervisor(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supplier"))
            return Supplier.convertJsonStringToSupplier(jsonArray.get(1).getAsString());
        if (jsonArray.get(0).getAsString().equals("Supporter"))
            return Supporter.convertJsonStringToSupporter(jsonArray.get(1).getAsString());
        return null;
    }

    public void controlUpdateCart() throws ExceptionalMassage {
        communication("controlUpdateCart", new JsonArray());
    }

    public String createChatRoomBetweenSupporterAndCustomer(Supporter supporter) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(supporter.getUserName());
        return communication("createChatRoomBetweenSupporterAndCustomer", inputs).getAsString();

    }

    public void addMessageToChatRoom(String senderUsername,String content,String chatRoomId) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(senderUsername);
        inputs.add(content);
        inputs.add(chatRoomId);
        communication("addMessageToChatRoom",inputs);
    }

    public void controlSetWageAndMinimum(int wage , int minimum ) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(wage);
        inputs.add(minimum);
        communication("controlSetWageAndMinimum",inputs);
    }

    public int controlGetWage() throws ExceptionalMassage {
        return Integer.parseInt(communication("controlGetWage", new JsonArray()).getAsString());
    }

    public int controlGetMinimum() throws ExceptionalMassage {
        return Integer.parseInt(communication("controlGetMinimum", new JsonArray()).getAsString());
    }

    public ArrayList<Customer> getOnlineCustomers() throws ExceptionalMassage {
        return Utils.convertJsonElementToCustomerArrayList(communication("getOnlineCustomers", new JsonArray()));
    }

    public ArrayList<Supplier> getOnlineSuppliers() throws ExceptionalMassage {
        return Utils.convertJsonElementToSupplierArrayList(communication("getOnlineSuppliers", new JsonArray()));
    }

    public ArrayList<Supervisor> getOnlineSupervisors() throws ExceptionalMassage {
        return Utils.convertJsonElementToSupervisorArrayList(communication("getOnlineSupervisors", new JsonArray()));
    }

    public ArrayList<Supporter> getOnlineSupporters() throws ExceptionalMassage {
        return Utils.convertJsonElementToSupporterArrayList(communication("getOnlineSupporters", new JsonArray()));
    }

    public ArrayList<Message> getAllMessagesOfChatRoomById(String chatRoomId) throws ExceptionalMassage{
        JsonArray input = new JsonArray();
        input.add(chatRoomId);
        return Utils.convertJsonElementToMessageArrayList(communication("getAllMessagesOfChatRoomById",input));
    }

    public ArrayList<String> getRequestingCustomersBySupporter() throws ExceptionalMassage{
        return Utils.convertJsonElementToStringArrayList(communication("getRequestingCustomersBySupporter",new JsonArray()));
    }

    public Customer getCustomerOfAChatRoom(String id) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        return Customer.convertJsonStringToCustomer(communication("getCustomerOfAChatRoom", inputs).toString());
    }

    public void controlCloseChatRoomById(String id) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(id);
        communication("controlCloseChatRoomById", inputs);
    }

    public ArrayList<String> controlGetMembersOfChatRoom(String chatRoomId) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(chatRoomId);
        return Utils.convertJsonElementToStringArrayList(communication("controlGetMembersOfChatRoom", inputs));
    }

    public void controlJoinChatRoom(String chatRoomId) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(chatRoomId);
        communication("controlJoinChatRoom", inputs);
    }

    public void controlLeaveChatRoom(String chatRoomId) throws ExceptionalMassage{
        JsonArray inputs = new JsonArray();
        inputs.add(chatRoomId);
        communication("controlLeaveChatRoom",inputs);
    }

    public void controlPayBack(int accountNumber, int amount) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(String.valueOf(accountNumber));
        inputs.add(String.valueOf(amount));
        communication("controlPayBack", inputs);
    }

    public void controlPay(String username, int accountNumber, String password, int amount) throws ExceptionalMassage {
        JsonArray inputs = new JsonArray();
        inputs.add(username);
        inputs.add(password);
        inputs.add(String.valueOf(accountNumber));
        inputs.add(String.valueOf(amount));
        communication("controlPay", inputs);
    }
}
