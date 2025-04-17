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
            context.status(400);
        }else{
            String username = accountService.checkAccountExists(account);
            if(username != null){
                context.status(400);
                return ;
            }
            Account insertedAccount = accountService.insertAccount(account);
            if(insertedAccount == null){
                context.status(400);
            }else{
                context.status(200).json(insertedAccount);
            }
        }
    }

    public Account registerUser(Account account){
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
