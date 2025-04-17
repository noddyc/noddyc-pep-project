package Controller;

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
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
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
        app.get("messages", this::getAllMessageHandler);
        // app.get("messages/id", xxx);
        // app.delete("messages/id", xxx);
        // app.patch("messages/id", xxx);
        // app.get("accounts/{account_id}",xxx);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context context) throws JsonMappingException,  JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(account.getUsername().length() == 0
        || account.getPassword().length() < 4){
            context.status(400);
        }else{
            String username = accountService.checkAccountExists(account);
            if(username != null){
                context.status(400);
                return ;
            }
            Account insertedAccount = accountService.insertAccount(account);
            if(insertedAccount == null){
                context.status(400);
            }else{
                context.status(200).json(insertedAccount);
            }
        }
    }

    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account matchedAccount = accountService.logIn(account);
        if(matchedAccount != null){
            context.status(200).json(matchedAccount);
        }else{
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255){
            System.out.println(message.getMessage_text().length());
            context.status(400);
            return ;
        }
        boolean accountExists = messageService.checkAccountExists(message.getPosted_by());
        if(!accountExists){
            context.status(400);
        }else{
            Message createdMessage = messageService.createMessage(message);
            if(createdMessage != null){
                context.status(200).json(createdMessage);
            }else{
                context.status(400);
            }
  
        }
    }

    private void getAllMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{

    }

}


// # Requirements

// ## 3: Our API should be able to process the creation of new messages.

// As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

// - The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
// - If the creation of the message is not successful, the response status should be 400. (Client error)

// ## 4: Our API should be able to retrieve all messages.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

// - The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

// ## 5: Our API should be able to retrieve a message by its ID.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message identified by the message_id. It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.

// ## 6: Our API should be able to delete a message identified by a message ID.

// As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

// - The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the now-deleted message. The response status should be 200, which is the default.
// - If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.

// ## 7: Our API should be able to update a message text identified by a message ID.

// As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

// - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
// - If the update of the message is not successful for any reason, the response status should be 400. (Client error)

// ## 8: Our API should be able to retrieve all messages written by a particular user.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

// - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

// # Further guidance

// Some classes are already complete and SHOULD NOT BE CHANGED - Integration tests, Model classes for Account and Message, a ConnectionUtil class. Changing any of these classes will likely result in the test cases being impossible to pass.

// The .sql script found in src/main/resources is already complete and SHOULD NOT BE CHANGED. Changing this file will likely result in the test cases being impossible to pass.

// You SHOULD be changing the SocialMediaController class to add endpoints to the StartAPI method. A main method in Main.java is also provided to allow you to run the entire application and manually play or test with the app. Changing that class will not affect the test cases at all. You could use it to perform any manual unit testing on your other classes.

// You SHOULD be creating and designing DAO and Service class to allow you to complete the project. In theory, you could design the project however you like, so long as the functionality works and you are somehow persisting data to the database - but a 3-layer architecture is a robust design pattern and following help you in the long run. You can refer to prior mini-projects and course material for help on designing your application in this way.

// # Good luck!

