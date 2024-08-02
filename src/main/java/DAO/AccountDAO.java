package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. Just in case you forgot.
 * 
 * database account:
 *   account_id integer primary key auto_increment,
 *   username varchar(255) unique,
 *   password varchar(255)
 */
public class AccountDAO {
    /**
     * Creates a new account.
     * 
     * @param account An Account object containing the data to create a new account, without an account_id.
     * @return a Message object containing the required information, including a generated account_id
     */
    public Account registerAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "insert into account (username, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // don't set account_id manually, should be automatically generated
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, 
                                   account.getUsername(), 
                                   account.getPassword());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the account associated with a given username.
     * 
     * @param username The username to search for.
     * @return an Account object with the account data associated with the given username, or null if it does not exist
     */
    public Account getAccount(String username) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "select * from account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account acc = new Account(rs.getInt("account_id"),
                                          rs.getString("username"),
                                          rs.getString("password"));
                return acc;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the account associated with a given account_id.
     * 
     * @param id The account_id to search for.
     * @return an Account object with the account data associated with the given account_id, or null if it does not exist
     */
    public Account getAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "select * from account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account acc = new Account(rs.getInt("account_id"),
                                          rs.getString("username"),
                                          rs.getString("password"));
                return acc;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the account associated with a given username and password.
     * 
     * @param username The username to match.
     * @param password The password to match.
     * @return an Account object with the account data associated with the given info, or null if there is not one
     */
    public Account loginAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "select * from account where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account acc = new Account(rs.getInt("account_id"),
                                          rs.getString("username"),
                                          rs.getString("password"));  // this is probably not very secure
                return acc;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
