package Tools.DB;

import Entities.Account;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashraf on 30/04/2017.
 */
public class DBHelper {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public boolean checkLoginAccess (String username, String password){
        boolean result = false;
        connect = ConnectionConfiguration.getConnection();
        try {
            preparedStatement = connect.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,passHash(password));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.isBeforeFirst()){ //checking if we have at least 1 result
                result = true; //means we that the account exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){ // testing if preparedStatement is open(pointing to !null)
                try {
                    preparedStatement.close(); // closing the preparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //search for account via username
    public Account getAccountByUsername(String username) {
        Account account = new Account();
        connect = ConnectionConfiguration.getConnection();
        try {
            preparedStatement = connect.prepareStatement("SELECT * FROM account WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){ //checking if there is no result
                account = null; //the account doesn't exists
            } else { //means we have a result
                while (resultSet.next()){//retrieving the result and storing it on account
                    account.setUsername(resultSet.getString("username"));
                    account.setPassword(resultSet.getString("password"));
                    account.setAccType(resultSet.getString("type"));
                    account.setEmail(resultSet.getString("email"));
                }
            }
        } catch (SQLException e) { //handling exception of  connect + preparedstatement + resultset
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){ // testing if preparedStatement is open(pointing to !null)
                try {
                    preparedStatement.close(); // closing the preparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return account;
    }

    //to get accounts that have similar user names
    public ArrayList<Account> getAccountsByUsername(String username) {
        ArrayList<Account> accountArrayList = new ArrayList<>();
        connect = ConnectionConfiguration.getConnection();
        try {
            preparedStatement = connect.prepareStatement("SELECT * FROM account WHERE username LIKE ?");
            preparedStatement.setString(1,'%'+username+'%');
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){ //checking if there is no result
                accountArrayList = null; //the account doesn't exists
            } else { //means we have a result
                while (resultSet.next()){//retrieving the result and storing it on account
                    Account account = new Account();
                    account.setUsername(resultSet.getString("username"));
                    account.setPassword(resultSet.getString("password"));
                    account.setAccType(resultSet.getString("type"));
                    account.setEmail(resultSet.getString("email"));
                    accountArrayList.add(account);
                }
            }
        } catch (SQLException e) { //handling exception of  connect + preparedstatement + resultset
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){ // testing if preparedStatement is open(pointing to !null)
                try {
                    preparedStatement.close(); // closing the preparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return accountArrayList;
    }

    //search for account via email
    public Account getAccountByEmail(String email) {
        Account account = new Account();
        connect = ConnectionConfiguration.getConnection();
        try {
            preparedStatement = connect.prepareStatement("SELECT * FROM account WHERE email = ?");
            preparedStatement.setString(1,email);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){ //checking if there is no result
                account = null; //the account doesn't exists
            } else { //means we have a result
                while (resultSet.next()){//retrieving the result and storing it on account
                    account.setUsername(resultSet.getString("username"));
                    account.setPassword(resultSet.getString("password"));
                    account.setAccType(resultSet.getString("type"));
                    account.setEmail(resultSet.getString("email"));
                }
            }
        } catch (SQLException e) { //handling exception of  connect + preparedstatement + resultset
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){ // testing if preparedStatement is open(pointing to !null)
                try {
                    preparedStatement.close(); // closing the preparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return account;
    }


    //changing account password
    public boolean changeAccountPassword(String username,String password){
        boolean updated = false;
        Account account = getAccountByUsername(username);
        if(account !=null){
            try {
                connect = ConnectionConfiguration.getConnection();
                preparedStatement = connect.prepareStatement("UPDATE account SET password = ? WHERE username = ? ");
                preparedStatement.setString(1,passHash(password));
                preparedStatement.setString(2,username);
                preparedStatement.executeUpdate();
                updated = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }finally { // closing preparedstatement and connect
                if (preparedStatement != null) { // closing the preparedstatement
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connect != null) { // closing the connection
                    try {
                        connect.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return updated;
    }

    public boolean addAccount(String username, String password, String email, String accType){
        boolean result = false;
        try {
            connect = ConnectionConfiguration.getConnection();
            preparedStatement = connect.prepareStatement("insert into  account " +
                    "(username, password, type, email) values (?, ?, ?, ?)");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,passHash(password));
            preparedStatement.setString(3,accType);
            preparedStatement.setString(4,email);
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { // preparedstatement + connect of select query
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    //delete account
    public boolean deleteAccount(String username){
        boolean result = false;
        try {
            connect = ConnectionConfiguration.getConnection();
            preparedStatement = connect.prepareStatement("DELETE FROM  account WHERE username = ?");
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { //closing  preparedStatment + connect
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connect != null){
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public ArrayList<Account> getAccountList(){
        //temporary array that will be filled with the table data
        ArrayList<Account> tmpArray = new ArrayList<Account>();
        connect = ConnectionConfiguration.getConnection();
        Statement state = null;
        try {
            state = connect.createStatement();
            //getting all accounts general info from the account table
            ResultSet res = state.executeQuery("SELECT * FROM account");
            while (res.next()){
                //creating a account variable which will contain the information of the current account from the Database
                Account tmp = new Account();
                tmp.setUsername(res.getString("username"));
                tmp.setPassword(res.getString("password"));
                tmp.setAccType(res.getString("type"));
                tmp.setEmail(res.getString("email"));
                //adding the current account to the temporary list
                tmpArray.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { //closing the connection and statments
            if(state!= null){try {state.close();} catch (SQLException e) {e.printStackTrace();}}
            if (connect != null) {try {connect.close();} catch (SQLException e) {e.printStackTrace();}}
        }
        //returning the temporary list filled with all account's general info
        return tmpArray;
    }

    public static String passHash(String passwordToHash){
        String hashtext = null;
        MessageDigest md;
        try {
            //Getting the md5 with salt hash
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(passwordToHash.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashtext= bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }
}
