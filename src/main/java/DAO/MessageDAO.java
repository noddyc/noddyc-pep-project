package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Model.Message;
import Util.ConnectionUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


/**
 * DAO layer to interact with Message table
 */
public class MessageDAO {
    
    public Optional<Message> createMessage(Message message){
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
                return Optional.of(new Message(generated_account_id, message.getPosted_by(), 
                message.getMessage_text(), message.getTime_posted_epoch()));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(msg);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Optional<Message> getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return Optional.of(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }


    public Optional<Message> deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sqlFetch = "Select * from Message where message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(sqlFetch);
            selectStatement.setInt(1, id);
            ResultSet rs = selectStatement.executeQuery();
            Message messageOnDeleted = null;
            if(rs.next()){
                messageOnDeleted = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
            if(messageOnDeleted == null){
                return Optional.empty();
            }

            String sqlDelete = "Delete from Message where message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
            deleteStatement.setInt(1, id);
            int rowsEffected = deleteStatement.executeUpdate();
            if(rowsEffected > 0){
                return Optional.of(messageOnDeleted);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Message> updateMessageText(Message message, int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sqlUpdate= "update Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
            updateStatement.setString(1, message.getMessage_text());
            updateStatement.setInt(2, id);
            updateStatement.executeUpdate();
            ResultSet pkeyResultSet = updateStatement.getGeneratedKeys();

            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                String sqlSelect = "select * from message where message_id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
                selectStatement.setInt(1, generated_account_id);
                ResultSet rs = selectStatement.executeQuery();
                while(rs.next()){
                    return Optional.of(new Message(rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")));
                }

            }else{
                return Optional.empty();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Message> getAllMessagesByAccountId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(msg);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
