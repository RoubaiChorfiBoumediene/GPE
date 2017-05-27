package Tools.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by Ashraf on 30/04/2017.
 */
public class ConnectionConfiguration {
    private final static String DBUSER = "root";
    private final static String DBPASS = "root";
    private final static String DBNAME = "GPE";

    public static Connection getConnection(){
        Connection connect = null;
        try {
            // This will load the MySQL driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/"+DBNAME+"?"
                    + "user="+DBUSER+"&password="+DBPASS+"&useSSL=false");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("DB connection exception");
            e.printStackTrace();
        }
        return connect;
    }

}
