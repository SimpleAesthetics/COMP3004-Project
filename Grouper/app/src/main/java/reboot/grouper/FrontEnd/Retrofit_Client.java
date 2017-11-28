package reboot.grouper.FrontEnd;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by visha on 2017-11-15.
 */

public class Retrofit_Client {
    private static Retrofit retrofit = null;
    //private static String address = "http://172.17.135.12:8080/";
    //private static String address = "http://192.168.2.69:8080/";
    private static String address = "http://grouper-server-deployment.cfapps.io/";

    public static Retrofit I() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(address)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit_API getAPI(){
        return I().create(Retrofit_API.class);
    }
}
