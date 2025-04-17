package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
        private MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public boolean checkAccountExists(int id){
        return messageDAO.checkAccountExists(id);
    }
    
    /**
     * method to create new message for existed user
     * @param message
     * @return new message
     */
    public Message createMessage(Message message){
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255){
            return null;
        }
        boolean accountExists = checkAccountExists(message.getPosted_by());
        if(!accountExists){
            return null;
        }else{
            Message createdMessage = messageDAO.createMessage(message);
            if(createdMessage != null){
                return createdMessage;
            }else{
                return null;
            }
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
    public Message getMessageById(String idParam){
        // return messageDAO.getMessageById(id);

        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return null;
        }

        Message message = messageDAO.getMessageById(id);
        if(message == null){
            return null;
        }else{
            return message;
        }
    }

    public Message deleteMessageById(String idParam){
        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return null;
        }

        Message message = messageDAO.deleteMessageById(id);

        if(message == null){
            return null;
        }else{
            return message;
        }
        // return messageDAO.deleteMessageById(id);
    }

    public List<Message> getAllMessagesByAccountId(int id){
        return messageDAO.getAllMessagesByAccountId(id);
    }

    public Message updateMessageText(Message message, String idParam){

        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            return null;
        }
        if(message.getMessage_text().length() == 0 || 
        message.getMessage_text().length() > 255){
            return null;
        }

        if(messageDAO.getMessageById(id) == null){
            return null;
        }

        Message deletedMessage = messageDAO.updateMessageText(message, id);

        if(deletedMessage == null){
            return null;
        }else{
            return deletedMessage;
        }
        // return messageDAO.updateMessageText(message, id);
    }
}
