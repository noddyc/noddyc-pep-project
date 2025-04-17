package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Service layer for message
 */
public class MessageService {
    
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    /**
     * method to create new message for existed user
     * @param message
     * @return new message
     */
    public Optional<Message> createMessage(Message message){
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255){
            return Optional.empty();
        }
        boolean accountExists = accountDAO.checkAccountExists(message.getPosted_by());
        if(!accountExists){
            return Optional.empty();
        }else{
            Optional<Message> createdMessage = messageDAO.createMessage(message);
            return createdMessage;
        }
    }


    /**
     * method to retrieve all messages
     * @param message
     * @return all messages
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }


    /**
     * method to fetch message by ID
     * @param message
     * @return message fetched by ID
     */
    public Optional<Message> getMessageById(String idParam){
        // return messageDAO.getMessageById(id);

        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return Optional.empty();
        }

        Optional<Message> message = messageDAO.getMessageById(id);
        return message;
    }

    /**
     * method to delete message by ID
     * @param idParam
     * @return message deleted
     */
    public Optional<Message> deleteMessageById(String idParam){
        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return Optional.empty();
        }

        Optional<Message> message = messageDAO.deleteMessageById(id);
        return message;
    }

    /**
     * method to update message text by ID
     * @param message 
     * @param idParam the ID of the message to be updated
     * @return message updated
     */
    public Optional<Message> updateMessageText(Message message, String idParam){

        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return Optional.empty();
        }
        if(message.getMessage_text().length() == 0 || 
        message.getMessage_text().length() > 255){
            return Optional.empty();
        }

        if(messageDAO.getMessageById(id) == null){
            return Optional.empty();
        }

        Optional<Message> deletedMessage = messageDAO.updateMessageText(message, id);

        return deletedMessage;
    }

    /**
     * method to get all messages by account ID
     * @param idParam the account ID of fetched messages
     * @return messages
     */
    public List<Message> getAllMessagesByAccountId(String idParam){

        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return new ArrayList<>();
        }

        if(!accountDAO.checkAccountExists(id)){
            return new ArrayList<>();
        }

        return messageDAO.getAllMessagesByAccountId(id);
    }

}
