package account;

import database.AccountDataBase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * @author rpirayadi
 * @since 0.0.1
 */

public abstract class Account {
    protected String userName;
    protected String name;
    protected String familyName;
    protected String email;
    protected String phoneNumber;
    protected int credit;
    protected boolean isAvailable ;
    protected int bankAccountNumber;
    private static final ArrayList<Account> allAccounts = new ArrayList<>();

    public Account(String userName, String name, String familyName, String email, String phoneNumber, int credit,
                   boolean isAvailable , int bankAccountNumber) {
        this.userName = userName;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.credit = credit;
        this.isAvailable = isAvailable;
        this.bankAccountNumber = bankAccountNumber;
        allAccounts.add(this);
    }

    public static int getStoreBankAccount() {
        return getASupervisor() != null ? getASupervisor().getBankAccountNumber() : 0;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getCredit() {
        return credit;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public int getBankAccountNumber() {
        return bankAccountNumber;
    }

    public static ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public static ArrayList<String> getAllUsername(){
        ArrayList<Account> availableAccounts = getAllAvailableAccounts();
        ArrayList <String> allUsername= new ArrayList<>();
        for (Account account :availableAccounts) {
            allUsername.add(account.getUserName());
        }
        return allUsername;
    }

    public void setName(String name) {
        this.name = name;
        AccountDataBase.update(this);
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
        AccountDataBase.update(this);
    }

    public void setEmail(String email) {
        this.email = email;
        AccountDataBase.update(this);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        AccountDataBase.update(this);
    }

    public void setCredit(int credit) {
        this.credit = credit;
        AccountDataBase.update(this);
    }

    public void setIsAvailable(boolean IsAvailable) {
        isAvailable = IsAvailable;
        AccountDataBase.update(this);
    }

    public static boolean isUsernameAvailable(String userName) {
        for (Account account : allAccounts) {
            if (account.getUserName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    public static Account getAccountByUsernameWithinAvailable(String userName) {
        ArrayList<Account> availableAccounts = getAllAvailableAccounts();
        if( availableAccounts.size() != 0) {
            for (Account account : availableAccounts) {
                if (account.getUserName().equals(userName)) {
                    return account;
                }
            }
        }
        return null;
    }

    public static Account getAccountByUsernameWithinAll(String userName) {
        if( allAccounts.size() != 0) {
            for (Account account : allAccounts) {
                if (account.getUserName().equals(userName)) {
                    return account;
                }
            }
        }
        return null;
    }

    public void removeAccount (){
       setIsAvailable(false);
    }

    public static Supplier getSupplierByCompanyName(String companyName){
        ArrayList<Account> availableAccounts = getAllAvailableAccounts();
        for (Account account : availableAccounts) {
            if(account instanceof  Supplier && ((Supplier) account).getNameOfCompany().equals(companyName))
                return (Supplier)account;
        }
        return null;
    }

    private static ArrayList<Account> getAllAvailableAccounts (){
        ArrayList<Account> availableAccounts = new ArrayList<>();
        for (Account account : allAccounts) {
            if(account.isAvailable)
                availableAccounts.add(account);
        }
        return availableAccounts;
    }

    public static boolean isSupervisorCreated() {
        if (allAccounts.size() != 0) {
            for (Account account : allAccounts) {
                if (account instanceof Supervisor) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Account getASupervisor() {
        if (allAccounts.size() != 0) {
            for (Account account : allAccounts) {
                if (account instanceof Supervisor) {
                    return account;
                }
            }
        }
        return null;
    }

    public void editAllFields(String name, String familyName, String email, String phoneNumber) {
        this.userName = userName;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        AccountDataBase.update(this);
    }

    public String getAccountType(){
        if(this instanceof Customer)
            return "Customer";
        if(this instanceof Supervisor)
            return "Supervisor";
        if(this instanceof Supplier)
            return "Supplier";
        return "Supporter";
    }

    public static ArrayList<Supervisor> getSupervisorsList() {
        ArrayList<Supervisor> allSupervisors = new ArrayList<>();
        for (Account account: allAccounts) {
            if (account instanceof Supervisor && account.isAvailable) {
                allSupervisors.add((Supervisor) account);
            }
        }
        return allSupervisors;
    }

    public static ArrayList<Supporter> getSupportersList() {
        ArrayList<Supporter> allSupporters = new ArrayList<>();
        for (Account account: allAccounts) {
            if (account instanceof Supporter && account.isAvailable) {
                allSupporters.add((Supporter) account);
            }
        }
        return allSupporters;
    }

    public static ArrayList<Supplier> getSuppliersList() {
        ArrayList<Supplier> allSuppliers = new ArrayList<>();
        for (Account account: allAccounts) {
            if (account instanceof Supplier && account.isAvailable) {
                allSuppliers.add((Supplier) account);
            }
        }
        return allSuppliers;
    }

    public static ArrayList<Customer> getCustomersList() {
        ArrayList<Customer> allCustomers = new ArrayList<>();
        for (Account account: allAccounts) {
            if (account instanceof Customer && account.isAvailable) {
                allCustomers.add((Customer) account);
            }
        }
        return allCustomers;
    }

    public static boolean isFirstSupervisorCreated() {
        for (Account account : allAccounts) {
            if (account.isAvailable && account instanceof Supervisor) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Customer> getRandomCustomers() {
        ArrayList<Customer> randomCustomers = new ArrayList<>();
        for (Account account : allAccounts) {
            if (account instanceof Customer) {
                Random random = new Random();
                if (random.nextInt(100) > 90) {
                    randomCustomers.add((Customer) account);
                }
            }
        }
        return randomCustomers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(userName, account.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
