package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.Optional;

/**
 * Service layer for account 
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * method to register 
     * @param account
     * @return registered account
     */
    public Optional<Account> register(Account account){
        if(account.getUsername() == null || 
            account.getUsername().isEmpty() 
        || account.getPassword().length() < 4){
            return Optional.empty();
        }else{
            boolean accountExists = accountDAO.checkAccountExists(account);
            if(accountExists){
                return Optional.empty();
            }
            Optional<Account> insertedAccount = accountDAO.registerAccount(account);
            return insertedAccount;
        }
    }

    /**
     * method to login
     * @param account
     * @return authenticated account
     */
    public Optional<Account> logIn(Account account){
        return accountDAO.logIn(account);
    }
}
