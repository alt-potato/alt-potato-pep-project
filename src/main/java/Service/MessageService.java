package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */
public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    /**
     * Attempts to post a message given a Message object without a message_id.
     * 
     * The creation of the message will be successful if and only if the message_text is not blank, is not 
     * over 255 characters, and posted_by refers to a real, existing user. 
     * The new message should be persisted to the database.
     * 
     * @param message a Message object without an account_id
     * @return a valid Account with the given data on success, null on failure
     */
    public Message postMessage(Message message) {
        // reject if message_text is blank
        if (message.getMessage_text().isBlank()) return null;
        // reject if message_text is over 255 characters
        if (message.getMessage_text().length() > 255) return null;  // what is this, X, formerly twitter?
        // reject if posted_by is not a real, existing user
        if (accountDAO.getAccount(message.getPosted_by()) == null) return null;

        return messageDAO.postMessage(message);
    }
}
