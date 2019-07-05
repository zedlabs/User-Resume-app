package tk.zedlabs.sidb.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.Activities.MainActivity;
import tk.zedlabs.sidb.POJO.DataResponsePersonal;
import tk.zedlabs.sidb.POJO.Personal;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class PersonalDetailsFragment extends Fragment {

    JsonAPI jsonAPI;
    Integer id;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences("MainPref", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.personal_details,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPersonalDetails(view);
        getProfilePicture(view);
    }

    private void getProfilePicture(View view) {
        id = sharedPref.getInt("id_in",0);
        Call<ResponseBody> call = jsonAPI.getProfilePicture(id);
        final ImageView imageView11 = view.findViewById(R.id.imageView_profile_pic);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.v("fragment","unsuccessful");
                    return;
                }
                Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                imageView11.setImageBitmap(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("fail","upload picture fail");
            }
        });
    }

    public void getPersonalDetails(View view) {
        final TextView skills = view.findViewById(R.id.tv_per_hobbies);
        final TextView mobile =  view.findViewById(R.id.tv_per_mobno);
        final TextView name =  view.findViewById(R.id.tv_per_name);
        final TextView links = view.findViewById(R.id.tv_per_links);
        final TextView location =  view.findViewById(R.id.tv_per_location);
        final TextView email = view.findViewById(R.id.tv_per_email);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.59.65.145:9090")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonAPI = retrofit.create(JsonAPI.class);
        id = sharedPref.getInt("id_in",0);


        Call<DataResponsePersonal> call = jsonAPI.getPersonalDetails(id);
        call.enqueue(new Callback<DataResponsePersonal>() {
            @Override
            public void onResponse(Call<DataResponsePersonal> call, Response<DataResponsePersonal> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                DataResponsePersonal drp = response.body();
                Personal p = drp.getPersonalData();
                skills.append(" "+p.getSkills());
                mobile.append(" "+p.getMobile_no());
                name.append(" " + p.getName());
                links.append(" "+p.getLinks());
                location.append(" "+ p.getLocation());
                email.append(" "+ p.getEmail());
            }

            @Override
            public void onFailure(Call<DataResponsePersonal> call, Throwable t) {
                Log.v("personalFrag",t.getMessage());
            }
        });
    }
}
