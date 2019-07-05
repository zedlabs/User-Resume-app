package tk.zedlabs.sidb.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.POJO.DataResponseEducation;
import tk.zedlabs.sidb.POJO.Education;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class EducationDetailsFragment extends Fragment {

    JsonAPI jsonAPI;
    int id1;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences("MainPref", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.education_details, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getCertificate(view);
        getEducationDetails(view);
    }

    private void getEducationDetails(View view) {
        final TextView startYear = view.findViewById(R.id.tv_edu_startYr);
        final TextView degree =  view.findViewById(R.id.tv_edu_degree);
        final TextView organisation =  view.findViewById(R.id.tv_edu_org);
        final TextView location =  view.findViewById(R.id.tv_edu_location);
        final TextView endYear = view.findViewById(R.id.tv_edu_endYr);

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
        id1 = sharedPref.getInt("id_in",0);
        Log.v("id value",id1+"");
        Call<DataResponseEducation> call = jsonAPI.getEducationDetails(id1);
        call.enqueue(new Callback<DataResponseEducation>() {
            @Override
            public void onResponse(Call<DataResponseEducation> call, Response<DataResponseEducation> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                DataResponseEducation dre = response.body();
                Education e = dre.getEducation();
                startYear.append(" "+e.getStart_year());
                degree.append(" "+e.getDegree());
                organisation.append(" "+e.getOrganisation());
                location.append(" "+e.getLocation());
                endYear.append(" "+e.getEnd_year());
            }

            @Override
            public void onFailure(Call<DataResponseEducation> call, Throwable t) {
                Log.v("educationFrag",t.getMessage());
            }
        });
    }

    private void getCertificate(View view) {
        id1 = sharedPref.getInt("id_in",0);
        Call<ResponseBody> call = jsonAPI.getCertificate(id1);
        final ImageView imageView = view.findViewById(R.id.certificateImageView);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.v("fragment","unsuccessful");
                    return;
                }
                Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                imageView.setImageBitmap(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("fail","upload picture fail");
            }
        });
    }
}
