package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import Model.Account;
import Util.ConnectionUtil;
import java.util.Optional;

/**
 * DAO layer to interact with Account table
 */
public class AccountDAO {
    /**
     * register new account
     * @param account
     * @return new account
     */
    public Optional<Account> registerAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into Account (username, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return Optional.of(new Account(generated_account_id, account.getUsername(), account.getPassword()));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * check if account exists by username
     * @param account
     * @return ID of existed account
     */
    public boolean checkAccountExists(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * check if account exists by account ID
     * @param account_id
     * @return ID of existed account
     */
    public boolean checkAccountExists(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    /**
     * authentication for login
     * @param account
     * @return authenticated account
     */
    public Optional<Account> logIn(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return Optional.of(new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}
