package Service;

import java.util.List;

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

    /**
     * Returns a list of all messages.
     * 
     * @return a List<Message> containing all messages, which may be empty
     */
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    /**
     * Retrieves the message associated with a given message_id.
     * 
     * @param id The message_id to search for.
     * @return a Message object with the data associated with the given message_id, or null if it does not exist
     */
    public Message getMessageByID(int id) {
        return this.messageDAO.getMessageByID(id);
    }

    /**
     * Deletes the message associated with a given message_id.
     * 
     * @param id The message_id to search for.
     * @return a Message object with the data associated with the deleted message, or null if no message was deleted
     */
    public Message deleteMessageByID(int id) {
        Message message = this.messageDAO.getMessageByID(id);
        int result = this.messageDAO.deleteMessageByID(id);

        return result == 0 ? null : message;  // maybe (result == 1) would be better, since the intended effect is one deletion?
    }

    /**
     * Updates the message associated with a given message_id with the given message_content.
     * 
     * The update of a message should be successful iff the message id already exists and the new message_text is 
     * not blank and is not over 255 characters.
     * 
     * @param id The message_id to search for.
     * @param message_content The content to update the message with.
     * @return a Message object with the updated message
     */
    public Message updateMessageByID(int id, String message_content) {
        // reject if message id does not exist
        if (this.messageDAO.getMessageByID(id) == null) return null;
        // reject if message_text is blank
        if (message_content.isBlank()) return null;
        // reject if message_text is over 255 characters
        if (message_content.length() > 255) return null;

        int result = this.messageDAO.updateMessageByID(id, message_content);
        Message newMessage = this.messageDAO.getMessageByID(id);

        return result == 0 ? null : newMessage;
    }

    /**
     * Returns a list of all messages posted by the given account_id, which may be empty.
     * 
     * @param account_id the account_id to search with
     * @return a List<Message> of all messages posted by the given account_id, which may be empty
     */
    public List<Message> getMessagesByAccountID(int account_id) {
        return this.messageDAO.getMessagesByAccountID(account_id);
    }
}
