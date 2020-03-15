package com.example.mindbowser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private boolean favourite_status,deleted_status ;
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    static int recyclerType = 0;
    Context mContext;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://testapi.rrlab.co.in/")
//            .baseUrl("https://mindbowser.free.beeceptor.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private List<Contact> mContactList;
    private Api jsonPlaceHolder = retrofit.create(Api.class);
    private String myTag = "test";


    public ContactAdapter(List<Contact> contactList) {
        mContactList = contactList;

    }


    @Override
    public int getItemViewType(int position) {
        if (mContactList != null && mContactList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (recyclerType) {
            case 1:
                if (viewType == 1) {
                    return new ContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout, parent, false));
                } else {
                    return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
                }
            case 2:
                if (viewType == 1) {
                    return new ContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_layout, parent, false));
                } else {
                    return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
                }
            case 3:
                if (viewType == 1) {
                    return new ContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_list_layout, parent, false));
                } else {
                    return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
                }
            default:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (mContactList != null && mContactList.size() > 0) {
            return mContactList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Contact> contactList) {
        mContactList.addAll(contactList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends BaseViewHolder {


        @BindView(R.id.person_photo)
        ImageView contact_image_view;

        @BindView(R.id.person_name)
        TextView contact_name_tv;

        @BindView(R.id.person_number)
        TextView contact_number_tv;

        @Nullable
        @BindView(R.id.icons_star)
        ImageView icons_star;

        @Nullable
        @BindView(R.id.delete)
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            contact_name_tv.setText("");
            contact_number_tv.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Contact contact = mContactList.get(position);


            if (contact.getContactImage() != null && contact.getContactImage() != "") {
                Log.d("test", contact.getContactImage() + " Found");
                Glide.with(itemView.getContext())
                        .load(contact.getContactImage())
                        .into(contact_image_view);
            } else {

                if (contact.getContactName() != null && contact.getContactName() != "") {
                    Log.d("test", "image not found");
                    String firstLetter = contact.getContactName().substring(0, 1);
                    firstLetter = firstLetter.toUpperCase();
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRect(firstLetter, Color.parseColor("#86D0F5"));
                    contact_image_view.setImageDrawable(drawable);
                } else {
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRect("NA", Color.parseColor("#86D0F5"));
                    contact_image_view.setImageDrawable(drawable);
                }
            }


            if (contact.getContactName() != null) {
                contact_name_tv.setText(contact.getContactName());
                contact_number_tv.setText(contact.getContactNumber());
                if (recyclerType != 3) {
                    if (contact.isFavorite()) {
                        favourite_status = true;
                        icons_star.setImageResource(R.mipmap.fillstar);
                    } else {
                        favourite_status = false;
                        icons_star.setImageResource(R.mipmap.star);
                    }
                    icons_star.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {
                            JsonObject paramObject = new JsonObject();
                            paramObject.addProperty("id", contact.getId());
                            if (!contact.isFavorite()) {
                                try {
                                    Call<List<Contact>> call = jsonPlaceHolder.addFav(paramObject);
                                    call.enqueue(new retrofit2.Callback<List<Contact>>() {
                                        @Override
                                        public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                                            Log.i(myTag, "Add to fav post submitted to API." );
                                        }

                                        @Override
                                        public void onFailure(Call<List<Contact>> call, Throwable t) {
                                            Log.e(myTag, "Unable to submit post to API.");
                                        }
                                    });
                                } catch (JsonIOException e) {
                                    e.printStackTrace();
                                }
                                contact.setFavorite(true);
                                icons_star.setImageResource(R.mipmap.fillstar);
                            } else {
                                try {
                                    Call<List<Contact>> call = jsonPlaceHolder.removeFav(paramObject);
                                    call.enqueue(new retrofit2.Callback<List<Contact>>() {
                                        @Override
                                        public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                                            Log.i(myTag, "Remove from Fav post submitted to API.");
                                        }

                                        @Override
                                        public void onFailure(Call<List<Contact>> call, Throwable t) {
                                            Log.e(myTag, "Unable to submit post to API.");
                                        }
                                    });

                                } catch (JsonIOException e) {
                                    e.printStackTrace();
                                }
                                if(recyclerType==2){
                                    removeAt(getCurrentPosition());
                                }
                                contact.setFavorite(false);
                                icons_star.setImageResource(R.mipmap.star);
                            }
                        }
                    });
                }
            }


            if (recyclerType != 2) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(getCurrentPosition());
                        JsonObject paramObject = new JsonObject();
                        paramObject.addProperty("id", contact.getId());
                        if(recyclerType==1){
                            //delete api
                            Call<List<Contact>> call = jsonPlaceHolder.deleteContact(paramObject);
                            call.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                                    Log.i(myTag, "post submitted to API." + response.body().toString());
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call, Throwable t) {
                                    Log.e(myTag, "Unable to submit post to API.");
                                }
                            });
                        }
                        else {
                            //restore api
                            Call<List<Contact>> call = jsonPlaceHolder.restoreContact(paramObject);
                            call.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                                    Log.i(myTag, "post submitted to API." + response.body().toString());
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call, Throwable t) {
                                    Log.e(myTag, "Unable to submit post to API.");
                                }
                            });
                        }
                    }
                });
            }
            itemView.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setMessage(contact.getContactNumber())
                        .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                Log.d("test", "Calling");
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.getContactNumber(), null));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                v.getRootView().getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("SMS", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("test", "Sending Message");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.getContactNumber(), null));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                v.getRootView().getContext().startActivity(intent);
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("test", "canceled");
                            }
                        });
                AlertDialog dialog = builder.create();
                if (recyclerType != 3) {
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }
                Log.d("test", contact.getContactName() + " Clicked");
            });


        }

        public void removeAt(int position) {
            mContactList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mContactList.size());
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
        @BindView(R.id.empty_text_view)
        TextView messageTextView;

        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }

}

