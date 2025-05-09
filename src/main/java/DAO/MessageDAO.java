package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. Just in case you forgot.
 * 
 * database message:
 *   message_id integer primary key auto_increment,
 *   posted_by integer,
 *   message_text varchar(255),
 *   time_posted_epoch long,
 *   foreign key (posted_by) references Account(account_id)
 */
public class MessageDAO {
    /**
     * Posts a message. 
     * 
     * @param message The Message object containing the data to post, without a message_id.
     * @return a Message object containing the required information, including a generated message_id
     */
    public Message postMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // don't set message_id manually, should be automatically generated
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());  // shouldn't this be set by the service/controller?
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, 
                                   message.getPosted_by(), 
                                   message.getMessage_text(),
                                   message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
    /**
     * Returns a list of all messages.
     * 
     * @return a List<Message> containing all messages, which may be empty
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /**
     * Retrieves the message associated with a given message_id.
     * 
     * @param id The message_id to search for.
     * @return a Message object with the account data associated with the given message, or null if it does not exist
     */
    public Message getMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Deletes the message associated with a given message_id.
     * 
     * @param id The message_id to search for.
     * @return the number of rows affected
     */
    public int deleteMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "delete from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /**
     * Updates the message associated with a given message_id with the given message_content.
     * 
     * @param id The message_id to search for.
     * @param message_content The content to update the message with.
     * @return the number of rows affected
     */
    public int updateMessageByID(int id, String message_content) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "update message set message_text = ? where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message_content);
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /**
     * Returns a list of all messages posted by the given account_id, which may be empty.
     * 
     * @param account_id the account_id to search with
     * @return a List<Message> of all messages posted by the given account_id, which may be empty
     */
    public List<Message> getMessagesByAccountID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
            
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
}
