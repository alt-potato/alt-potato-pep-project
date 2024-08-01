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
    public Account registerAccount(Account account) {
        
    }

    /**
     * Retrieves the account associated with a given username.
     * @param username The username to search for.
     * @return an Account object with the account data associated with the given username, null if does not exist
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
}
