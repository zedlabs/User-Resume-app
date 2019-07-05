package tk.zedlabs.sidb.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.POJO.DataResponseProfessional;
import tk.zedlabs.sidb.POJO.Professional;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class ProfessionalDetailsFragment extends Fragment {
    JsonAPI jsonAPI;
    int id;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences("MainPref", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.professional_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfessionalDetails(view);
    }

    private void getProfessionalDetails(View view) {
        final TextView endYear = view.findViewById(R.id.tv_pro_endYr);
        final TextView organisation =  view.findViewById(R.id.tv_pro_org);
        final TextView designation =  view.findViewById(R.id.tv_pro_desig);
        final TextView startyear = view.findViewById(R.id.tv_startYr);

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

        Call<DataResponseProfessional> call = jsonAPI.getProfessionalDetails(id);
        call.enqueue(new Callback<DataResponseProfessional>() {
            @Override
            public void onResponse(Call<DataResponseProfessional> call, Response<DataResponseProfessional> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                DataResponseProfessional drp = response.body();
                Professional p = drp.getProfessional();
                endYear.append(" "+p.getEnd_date());
                organisation.append(" "+p.getOrganisation());
                designation.append(" "+p.getDesignation());
                startyear.append(" "+p.getStart_date());
            }

            @Override
            public void onFailure(Call<DataResponseProfessional> call, Throwable t) {

            }
        });
    }
}
