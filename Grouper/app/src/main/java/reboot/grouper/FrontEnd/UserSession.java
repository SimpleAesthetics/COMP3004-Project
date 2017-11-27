package reboot.grouper.FrontEnd;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

import reboot.grouper.Model.UserInformation;

/**
 * Created by visha on 2017-11-07.
 */

public final class UserSession {
    private static volatile UserSession instance = null;
    private static UserInformation user;

    private UserSession(UserInformation u){
        user = u;
    }
    public UserInformation getUser() { return user; }

    public static void setUser(UserInformation u) { user=u; }

    public static UserSession I(UserInformation u){
        if (instance == null) {
            synchronized(UserSession.class) {
                if (instance == null) instance = new UserSession(u);
            }
        }
        return instance;
    }

    public static UserSession I(){
        return instance;
    }


    public static String[] credentials() {
        Map<String, String> headerMap = new HashMap<String, String>();
        String[] out = new String[2];
        if(I()!=null) {
            String credentials = I().getUser().getUsername() + ":" + I().getUser().getPassword();
            String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            out[0] = "Authorization";
            out[1] = "Basic " + encodedCredentials;
        }
        return out;
    }
}
