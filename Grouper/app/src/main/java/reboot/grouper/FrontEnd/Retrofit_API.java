package reboot.grouper.FrontEnd;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

import reboot.grouper.Model.*;
/**
 * Created by visha on 2017-11-15.
 */

public interface Retrofit_API {
    @GET("universities")
    Call<List<University>> getUniversities();

    @POST("universities")
    void postUniversity(@Body University univ);

    @GET("universities/{university}/courses")
    Call<List<Course>> getCourses(@Path("university") String univ);

    @POST("universities/{university}/courses")
    void postCourse(@Path("university") String univ, @Body Course cour);

    @GET("universities/{university}/courses/{course}/environments")
    Call<List<Environment>> getEnvironments(@Path("university") String univ, @Path("course") String cour);

    @POST("universities/{university}/courses/{course}/environments")
    void postEnvironment(@Path("university") String univ, @Path("course") String cour, @Body Environment Envi);
}
