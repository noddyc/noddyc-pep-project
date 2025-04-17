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
            Message createdMessage = createMessage(message);
            if(createdMessage == null){
                return null;
            }else{
                return createdMessage;
            }
        }
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    public List<Message> getAllMessagesByAccountId(int id){
        return messageDAO.getAllMessagesByAccountId(id);
    }

    public Message updateMessageText(Message message, int id){
        return messageDAO.updateMessageText(message, id);
    }
}
