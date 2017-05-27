package Tools;

import java.util.prefs.Preferences;

/**
 * Created by Ashraf on 02/05/2017.
 */
public class Session {
    private Preferences prefs;

    public Session(){
        prefs = Preferences.userRoot().node(this.getClass().getName());
    }

    public String getUser(){return prefs.get("user",null); }
    public void setUser(String userName) {prefs.put("user",userName);}

    public void setPrefs(String userName, boolean loggedIn, String type){
        prefs.put("user",userName);
        prefs.putBoolean("isLoggedIn",loggedIn);
        prefs.put("accType",type);
    }

    public void setType(String type){prefs.put("accType",type);}
    public String getType(){return prefs.get("accType",null);}

    public void removeAll(){
        prefs.remove("user");
        prefs.remove("isLoggedIn");
        prefs.remove("accType");
    }

}
