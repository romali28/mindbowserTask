package com.example.mindbowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.mindbowser.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ContactAdapter contactAdapter;
    TextView empty_view;
    LinearLayoutManager mLayoutManager;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://testapi.rrlab.co.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    private Api jsonPlaceHolder = retrofit.create(Api.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ContactAdapter.recyclerType = 2;
        mRecyclerView = findViewById(R.id.fav_recycler_view);

        getContact(this);
    }

    private void getContact(final Context context) {
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        contactAdapter = new ContactAdapter(new ArrayList<Contact>());

        Call<List<Contact>> call = jsonPlaceHolder.getContact();
        call.enqueue(new Callback<List<Contact>>() {


            private String myTag = "test";

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> contactList = response.body();
                //Creating an String array for the ListView
                ArrayList<Integer> idArrayList = new ArrayList<Integer>();
                ArrayList<String> nameArrayList = new ArrayList<String>();
                ArrayList<String> numberArrayList = new ArrayList<String>();
                ArrayList<String> imageArrayList = new ArrayList<String>();
                ArrayList<Boolean> favArrayList = new ArrayList<Boolean>();
                ArrayList<Boolean> delArrayList = new ArrayList<Boolean>();

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).isFavorite()) {
                        idArrayList.add(contactList.get(i).getId());
                        nameArrayList.add(contactList.get(i).getContactName());
                        numberArrayList.add(contactList.get(i).getContactNumber());
                        imageArrayList.add(contactList.get(i).getContactImage());
                        favArrayList.add(contactList.get(i).isFavorite());
                        delArrayList.add(contactList.get(i).isDeleted());
                    }
//                    Log.d("test", contactList.get(i).getContactName() );
                }

                Log.d(myTag, "Size of list :" + nameArrayList.size());
                prepareDemoContent(
                        idArrayList.toArray(new Integer[idArrayList.size()]),
                        nameArrayList.toArray(new String[nameArrayList.size()]),
                        numberArrayList.toArray(new String[numberArrayList.size()]),
                        imageArrayList.toArray(new String[imageArrayList.size()]),
                        favArrayList.toArray(new Boolean[favArrayList.size()]),
                        delArrayList.toArray(new Boolean[delArrayList.size()])
                );
            }


            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

                Log.d("Test", "Url");
            }


        });
    }

    private void prepareDemoContent(Integer[] id,String[] contactName, String[] contactNumber, String[] contactImage, Boolean[] favArray, Boolean[] delArray) {

//        String[] contactName = new String[] {"Romali","Dave","Shauna"};
//        String[] contactNumber = new String[] {"75684625","7543554535","8566542574"};
        CommonUtils.showLoading(FavoriteActivity.this);
        new Handler().postDelayed(() -> {
            CommonUtils.hideLoading();
            ArrayList<Contact> contactList = new ArrayList<>();
            for (int i = 0; i < contactName.length; i++) {
                contactList.add(new Contact(id[i],contactName[i], contactNumber[i], contactImage[i], favArray[i], delArray[i]));
            }
            contactAdapter.addItems(contactList);
            mRecyclerView.setAdapter(contactAdapter);
        }, 300);


    }


}
