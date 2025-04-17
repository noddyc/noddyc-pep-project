package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account){
        if(account.getUsername().length() == 0
        || account.getPassword().length() < 4){
            return null;
        }else{
            String username = checkAccountExists(account);
            if(username != null){
                return null;
            }
            Account insertedAccount = registerAccount(account);
            if(insertedAccount == null){
                return null;
            }else{
                return insertedAccount;
            }
        }
    }

    /**
     * method to check if account exists before registration
     * @param account
     * @return ID of existed account
     */
    public String checkAccountExists(Account account){
        return accountDAO.checkAccountExists(account);
    }

    /**
     * method to register new account
     * @param account
     * @return new account 
     */
    public Account registerAccount(Account account){
        return accountDAO.registerAccount(account);
    }


    /**
     * method to login
     * @param account
     * @return authenticated account
     */
    public Account logIn(Account account){
        return accountDAO.logIn(account);
    }
}
