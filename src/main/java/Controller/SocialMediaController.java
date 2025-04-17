package Controller;

import java.util.List;
import java.util.Optional;

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
        Optional<Account> processedAccount = accountService.register(account);
        processedAccount.ifPresentOrElse(
            registeredAccount -> context.status(200).json(registeredAccount),
            () -> context.status(400)
        );     
    }

     /**
     * Handler to process new User registrations (Part-2)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Optional<Account> processedAccount = accountService.logIn(account);
        processedAccount.ifPresentOrElse(
            matchedAccount -> context.status(200).json(matchedAccount),
            () -> context.status(401)
        );     
    }


    /**
     * Handler to process the creation of new messages (Part-3)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Optional<Message> processedMessage = messageService.createMessage(message);
        processedMessage.ifPresentOrElse(
            createdMessage -> context.status(200).json(createdMessage),
            () -> context.status(400)
        );     
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

        Optional<Message> messageByID = messageService.getMessageById(context.pathParam("message_id"));
        messageByID.ifPresentOrElse(
            message -> context.status(200).json(message),
            () -> context.status(200)
        );   
    }

    /**
     * Handler to delete a message identified by message ID (Part-6)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context context){

        Optional<Message> deletedMessage = messageService.deleteMessageById(context.pathParam("message_id"));
        deletedMessage.ifPresentOrElse(
            message -> context.status(200).json(message),
            () -> context.status(200)
        );   
    }

    /**
    * Handler to update a message text identified by a message ID (Part-7)
    * @param context The Javalin Context object manages information about both the HTTP request and response.
    */
    public void updateMessageTextHandler(Context context) throws JsonMappingException, JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Optional<Message> processedMessage = messageService.updateMessageText(message, context.pathParam("message_id"));

        processedMessage.ifPresentOrElse(
            updatedMessage -> context.status(200).json(updatedMessage),
            () -> context.status(400)
        );   
    }

    /**
     * Handler to retrieve all messages written by a particular user (Part-8)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getAllMessagesByAccountIdHandler(Context context){

        List<Message> messages = messageService.getAllMessagesByAccountId(context.pathParam("account_id"));
        context.status(200).json(messages);
    }
}


