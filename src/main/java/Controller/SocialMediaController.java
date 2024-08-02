package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * This class will create a Javalin API with endpoints when the startAPI method is called.
 * 
 * Endpoints:
 * - POST localhost:8080/register
 * - POST localhost:8080/login
 * - POST localhost:8080/messages
 * - GET localhost:8080/messages
 * 
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should refer to prior mini-project labs and lecture materials for 
 * guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registrationHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagePostHandler);
        app.get("/messages", this::messageGetHandler);

        return app;
    }

    /**
     * POST /register
     * 
     * The body will contain a representation of a JSON Account, but will not contain an account_id.
     * 
     * The registration will be successful if and only if the username is not blank, the password is at least 
     * 4 characters long, and an Account with that username does not already exist. If all these conditions are met, 
     * the response body should contain a JSON of the Account, including its account_id. The response status should be 
     * 200 OK, which is the default. The new account should be persisted to the database.
     * 
     * If the registration is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registrationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);

        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);  // OK
        } else {
            ctx.status(400);  // client error
        }
    }

    /**
     * POST /login
     * 
     * The request body will contain a JSON representation of an Account, not containing an account_id. In the future, 
     * this action may generate a Session token to allow the user to securely use the site.
     * 
     * The login will be successful iff the username and password provided in the request body JSON match a 
     * real account existing on the database. If successful, the response body should contain a JSON of the account 
     * in the response body, including its account_id. The response status should be 200 OK, which is the default.
     * If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedinAccount = accountService.loginAccount(account);

        if (loggedinAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedinAccount));
            ctx.status(200);  // OK
        } else {
            ctx.status(401);  // unauthorized
        }
    }

    /**
     * POST /messages
     * 
     * The request body will contain a JSON representation of a message, which should be persisted to the database, 
     * but will not contain a message_id.
     * 
     * The creation of the message will be successful if and only if the message_text is not blank, is not 
     * over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should 
     * contain a JSON of the message, including its message_id. The response status should be 200, which is the default. 
     * The new message should be persisted to the database.
     * 
     * If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void messagePostHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.postMessage(message);

        if (postedMessage != null) {
            ctx.json(mapper.writeValueAsString(postedMessage));
            ctx.status(200);  // OK
        } else {
            ctx.status(400);  // unauthorized
        }
    }

    /**
     * GET /messages
     * 
     * The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
     * It is expected for the list to simply be empty if there are no messages. The response status should always be 200, 
     * which is the default.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void messageGetHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
}
