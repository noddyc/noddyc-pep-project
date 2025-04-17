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
        return accountDAO.insertAccount(account);
    }

    public String checkAccountExists(Account account){
        return accountDAO.checkAccountExists(account);
    }

    public Account insertAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    public Account logIn(Account account){
        return accountDAO.logIn(account);
    }
}
