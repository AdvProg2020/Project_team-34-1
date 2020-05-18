package account;

import cart.Cart;
import database.AccountDataBase;

/**
 * @author rpirayadi
 * @since 0.0.1
 */
public class Customer extends Account {
    private String cartIdentifier;

    public Customer(String userName, String name, String familyName, String email, String phoneNumber, String password, int credit) {
        super(userName, name, familyName, email, phoneNumber, password, credit,true);
        this.cartIdentifier = new Cart(this).getIdentifier();
        AccountDataBase.add(this);
    }

    public Customer(String userName, String name, String familyName, String email, String phoneNumber, String password,
                    int credit,  String cartIdentifier, boolean isAvailable) {
        super(userName, name, familyName, email, phoneNumber, password, credit,isAvailable);
        this.cartIdentifier = cartIdentifier;

    }

    public Cart getCart() {
        return Cart.getCartById(cartIdentifier);
    }

    public void setCart(Cart cart) {
        this.cartIdentifier = cart.getIdentifier();
        AccountDataBase.update(this);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", credit=" + credit +
                '}';
    }
}
