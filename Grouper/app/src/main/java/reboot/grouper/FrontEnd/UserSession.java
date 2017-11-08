package reboot.grouper.FrontEnd;

import reboot.grouper.Model.UserInformation;

/**
 * Created by visha on 2017-11-07.
 */

public final class UserSession {
    private static volatile UserSession instance = null;
    private UserInformation user;

    private UserSession(){
        user = new UserInformation(100994856,"Vishahan","Thilagakumar","Advent1013","vishahan@live.com");
    }

    public UserInformation getUser() { return user; }

    public static UserSession I(){
        if (instance == null) {
            synchronized(UserSession.class) {
                if (instance == null) instance = new UserSession();
            }
        }
        return instance;
    }

}
