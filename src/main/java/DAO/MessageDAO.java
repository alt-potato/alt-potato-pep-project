package DAO;

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
    
}
