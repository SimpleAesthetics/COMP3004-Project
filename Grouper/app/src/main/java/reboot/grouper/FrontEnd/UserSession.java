package reboot.grouper.FrontEnd;

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

}
