import com.google.firebase.example.messaging.kotlin.com.nabard.sarnevesht.Content;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    // as we are making get request so we are displaying
    // GET as annotation.
    // and inside we are passing last parameter for our url.
    @GET("test.json")

    // as we are calling data from array so we are calling
    // it with array list and naming that method as getAllCourses();
    Call<ArrayList<Content>> getAll();
}