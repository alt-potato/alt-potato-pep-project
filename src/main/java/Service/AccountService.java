package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Attempts to register a new account given a new Account object without an account_id.
     * 
     * The registration will be successful iff the username is not blank, the password is at least 
     * 4 characters long, and an Account with that username does not already exist.
     * 
     * @param account an Account object without an account_id
     * @return a valid Account with the given data on success, null on failure
     */
    public Account registerAccount(Account account) {
        // reject if username is blank
        if (account.getUsername().isBlank()) return null;
        // reject if password is less than 4 characters long
        if (account.getPassword().length() < 4) return null;  // come on. you could brute force that in like 10 minutes.
        // reject if username already exists
        if (accountDAO.getAccount(account.getUsername()) != null) return null;

        return accountDAO.registerAccount(account);
    }

    /**
     * Attempts to log in an existing account given an Account object without an account_id.
     * 
     * The login will be successful iff the username and password match those of a 
     * real account existing on the database.
     * 
     * @param account an Account object without an account_id
     * @return a valid Account with the given data on success, null on failure
     */
    public Account loginAccount(Account account) {
        return accountDAO.loginAccount(account.getUsername(), account.getPassword());
    }
}
