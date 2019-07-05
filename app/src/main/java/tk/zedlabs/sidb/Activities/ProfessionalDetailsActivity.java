package tk.zedlabs.sidb.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ProfessionalDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int id;
    private JsonAPI jsonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_dialog);
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.59.65.145:9090")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonAPI = retrofit.create(JsonAPI.class);
        sharedPref = this.getSharedPreferences("MainPref", Context.MODE_PRIVATE);

        initializeThirdDialog();

    }
    public void initializeThirdDialog() {
        id = sharedPref.getInt("id_in",0);
        final EditText endDateEdt = findViewById(R.id.edt_pro_endYr);
        final EditText organisationEdt = findViewById(R.id.edt_pro_org);
        final EditText designationEdt = findViewById(R.id.edt_pro_desig);
        final EditText startDateEdt =  findViewById(R.id.edt_pro_startYr);

        Button submit = findViewById(R.id.buttonSubmitPro);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endData = endDateEdt.getText().toString();
                String organisation = organisationEdt.getText().toString();
                String designation = designationEdt.getText().toString();
                String startDate = startDateEdt.getText().toString();

                Professional professional = new Professional(endData,organisation,designation,startDate);
                Call<DataResponseProfessional> call = jsonAPI.setProfessionalData(id,professional);
                call.enqueue(new Callback<DataResponseProfessional>() {
                    @Override
                    public void onResponse(Call<DataResponseProfessional> call, Response<DataResponseProfessional> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Log.v("professionalDialog","uploaded");
                        Toast.makeText(ProfessionalDetailsActivity.this,"uploaded!",Toast.LENGTH_SHORT).show();
                        int id2 = response.body().getProfessional().getId();
                        editor = sharedPref.edit();
                        editor.putInt("id_pro",id2);
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Call<DataResponseProfessional> call, Throwable t) {

                        Log.e("professionalDialog","error uploading data: "+t.getMessage());
                    }
                });
                Intent intent = new Intent(ProfessionalDetailsActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });
    }
}
