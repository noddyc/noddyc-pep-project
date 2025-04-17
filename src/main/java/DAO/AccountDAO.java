package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import Model.Account;
import Util.ConnectionUtil;

/**
 * DAO class to interact with Account table
 */
public class AccountDAO {
    
    /**
     * register new account
     * @param account
     * @return new account
     */
    public Account registerAccount(Account account){
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
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * check if old account existed before registration
     * @param account
     * @return ID of existed account
     */
    public String checkAccountExists(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return rs.getString("username");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * authentication for login
     * @param account
     * @return authenticated account
     */
    public Account logIn(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
