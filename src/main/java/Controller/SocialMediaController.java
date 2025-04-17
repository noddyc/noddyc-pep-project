package Controller;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;


/**
 * SocialMediaController -- Jian He
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.get("messages", this::getAllMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageTextHandler);
        app.get("accounts/{account_id}/messages",this::getAllMessagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to process new User registrations (Part-1)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonMappingException,  JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account processedAccount = accountService.register(account);
        if(processedAccount == null){
            context.status(400);
        }else{
            context.status(200).json(processedAccount);
        }
    }

     /**
     * Handler to process new User registrations (Part-2)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account matchedAccount = accountService.logIn(account);
        if(matchedAccount == null){
            context.status(401);
        }else{
            context.status(200).json(matchedAccount);
        }
    }


    /**
     * Handler to process the creation of new messages (Part-3)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage == null){
            context.status(400);
        }else{
            context.status(200).json(createdMessage);
        }
    }

    /**
     * Handler to retrieve all messages (Part-4)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessageHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }


    /**
     * Handler to retrieve a message by its ID (Part-5)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context context){
        // String idParam = context.pathParam("message_id");
        Message messageByID = messageService.getMessageById(context.pathParam("message_id"));
        if(messageByID == null){
            context.status(200);
        }else{
            context.status(200).json(messageByID);
        }
        // Integer id;
        // try{
        //     id = Integer.parseInt(idParam);
        // }catch (NumberFormatException e) {
        //     context.status(200);
        //     return ;
        // }

        // Message message = messageService.getMessageById(id);
        // if(message == null){
        //     context.status(200);
        // }else{
        //     context.status(200).json(message);
        // }
    }

    /**
     * Handler to delete a message identified by message ID (Part-6)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context context){
        Message deletedMessage = messageService.deleteMessageById(context.pathParam("message_id"));
        if(deletedMessage == null){
            context.status(200);
        }else{
            context.status(200).json(deletedMessage);
        }
        // String idParam = context.pathParam("message_id");
        // Integer id;
        // try{
        //     id = Integer.parseInt(idParam);
        // }catch (NumberFormatException e) {
        //     context.status(200);
        //     return ;
        // }

        // System.out.println(id);

        // Message message = messageService.deleteMessageById(id);

        // if(message == null){
        //     context.status(200);
        // }else{
        //     context.status(200).json(message);
        // }
    }

    /**
    * Handler to update a message text identified by a message ID (Part-7)
    * @param context The Javalin Context object manages information about both the HTTP request and response.
    */
    public void updateMessageTextHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String idParam = context.pathParam("message_id");
        Message updatedMessage = messageService.updateMessageText(message, idParam);
        if(updatedMessage == null){
            context.status(400);
        }else{
            context.status(200).json(updatedMessage);
        }
        // Integer id;
        // try{
        //     id = Integer.parseInt(idParam);
        // }catch (NumberFormatException e) {

        //     context.status(400);
        //     return ;
        // }
        // if(message.getMessage_text().length() == 0 || 
        // message.getMessage_text().length() > 255){
    
        //     context.status(400);
        //     return ;
        // }


        // if(messageService.getMessageById(id) == null){
        //     context.status(400);
        //     return ;
        // }

        // Message deletedMessage = messageService.updateMessageText(message, id);

        // if(deletedMessage == null){
        //     context.status(400);
        // }else{
        //     context.status(200).json(deletedMessage);
        // }
    }

    /**
     * Handler to retrieve all messages written by a particular user (Part-8)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getAllMessagesByAccountIdHandler(Context context){
        List<Message> messages = new ArrayList<>();
        String idParam = context.pathParam("account_id");
        Integer id;
        try{
            id = Integer.parseInt(idParam);
        }catch (NumberFormatException e) {
            context.status(200);
            return ;
        }

        if(!messageService.checkAccountExists(id)){
            context.status(200).json(messages);
            return;
        }

        messages = messageService.getAllMessagesByAccountId(id);
        if(messages.size() == 0){
            context.status(200).json(messages);
        }else{
            context.status(200).json(messages);
        }
    }
}


