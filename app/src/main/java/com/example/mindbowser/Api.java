package com.example.mindbowser;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    //    String BASE_URL = "http://ec2-18-219-219-177.us-east-2.compute.amazonaws.com:10000/get_contacts/";
    //String BASE_URL = "https://mindbowser.free.beeceptor.com/get_contacts/";
    @GET("get_contacts/")
    Call<List<Contact>> getContact();

    @Headers({"Content-type: application/json",
            "Accept: */*"})
    @POST("add_favorite/")
    Call<List<Contact>> addFav(@Body JsonObject id);

    @POST("remove_favorite/")
    Call<List<Contact>> removeFav(@Body JsonObject id);

    @POST("delete_contact/")
    Call<List<Contact>> deleteContact(@Body JsonObject id);

    @POST("restore_contact/")
    Call<List<Contact>> restoreContact(@Body JsonObject id);

}
