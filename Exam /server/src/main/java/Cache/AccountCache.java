package Cache;
import server.repository.AccountRepository;
import server.model.Account;
import server.util.Config;

import java.sql.SQLException;
import java.util.ArrayList;


public class AccountCache {
    private ArrayList<Account> accounts;


    // Time cache should be alive
    private long ttl;
    // Sets when cache has been created
    private long created;
    private AccountRepository accountRepository;

    public AccountCache() {
        this.ttl = Config.getCacheTtl();
    }
    public ArrayList<Account> getAccounts(Boolean forceUpdate) throws SQLException {


        // If we wish to clear cache, we can set force update.
        // Otherwise we look at the age of the cache and figure out if we should update.
        // If the list is empty we also check for new users
        if (forceUpdate
                || ((this.created + this.ttl) <= (System.currentTimeMillis() / 10L))
                || this.accounts == null) {
            // Get users from controller, since we wish to update.
            ArrayList<Account> accounts = new AccountRepository().getAllAccounts();
            // Set users for the instance and set created timestamp
            this.accounts = accounts;
            this.created = System.currentTimeMillis() / 10L;
        }
        return this.accounts;
    }


    /**
     *Based on the cached getAllAccounts - only gets accounts for one customer
     * @param customerID input string
     * @return customerAccount
     */
    public ArrayList<Account> getAccountsforCustomer(int customerID) {
        ArrayList<Account> customerAccount = new ArrayList<>();
        for (Account account : accounts) {
            if (customerID == account.getCustomer_id()) {
                System.out.println(account);
                customerAccount.add(account);
            }
        }
            return customerAccount;
    }


    /**
     *Based on the cached getAllAccounts - only gets one account for one customer
     * @param accountId input string
     * @return account
     */
    public Account getOneAccountforCustomer(int accountId) {
        for (Account account : accounts) {
            if (accountId == account.getAccount_id()) {
                System.out.println(account);
                return account;
            }
        }
        return null;
}
}


