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
    
    public Message createMessage(Message message){
        return messageDAO.createMessage(message);
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
