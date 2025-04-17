package Service;

import DAO.MessageDAO;
import Model.Message;

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
}
