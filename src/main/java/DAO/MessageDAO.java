package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public boolean checkAccountExists(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_account_id, message.getPosted_by(), 
                message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
