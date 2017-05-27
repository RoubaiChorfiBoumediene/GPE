package Entities;

/**
 * Created by Ashraf on 30/04/2017.
 */
public class Account {
    private String username;
    private String password;
    private String email;
    private String accType;

    public Account(){}
    public Account(String user, String pass){
        username = user;
        password = pass;
    }
    public Account(String user, String pass, String type,String email){
        username = user;
        password = pass;
        accType = type;
        this.email = email;
    }

    @Override
    public String toString (){
        return "Username : "+username+" | Password : ******"+" | Account type : "+accType;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccType() {return accType;}
    public void setAccType(String accType) {this.accType = accType;}
}
