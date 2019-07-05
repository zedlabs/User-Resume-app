package tk.zedlabs.sidb.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.POJO.DataResponseEducation;
import tk.zedlabs.sidb.POJO.Education;
import tk.zedlabs.sidb.POJO.Professional;
import tk.zedlabs.sidb.POJO.Qse;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.FileUtil;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class EducationDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int id;
    private JsonAPI jsonAPI;
    private static final int READ_CERTIFICATE_REQUEST_CODE = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_dialog);
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
        editor = sharedPref.edit();

        initializeSecondDialog();
    }

    public void initializeSecondDialog() {
        id = sharedPref.getInt("id_in",0);
        final EditText startYearEdt = findViewById(R.id.edt_edu_startYr);
        final EditText degreeEdt = findViewById(R.id.edt_edu_degree);
        final EditText organisationEdt = findViewById(R.id.edt_edu_org);
        final EditText locationEdt =  findViewById(R.id.edt_edu_location);
        final EditText endYearEdt =  findViewById(R.id.edt_edu_endYr);

        Button next2 = findViewById(R.id.buttonSubmitEdu);

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startYear = startYearEdt.getText().toString();
                String degree =   degreeEdt.getText().toString();
                String organisation =  organisationEdt.getText().toString();
                String location =  locationEdt.getText().toString();
                String endYear =  endYearEdt.getText().toString();

                Education education = new Education(startYear,degree,organisation,location,endYear);
                Call<DataResponseEducation> call = jsonAPI.setEducationDetails(id,education);
                call.enqueue(new Callback<DataResponseEducation>() {
                    @Override
                    public void onResponse(Call<DataResponseEducation> call, Response<DataResponseEducation> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Log.v("educationDialog","uploaded");
                        Toast.makeText(EducationDetailsActivity.this,"uploaded!"+id,Toast.LENGTH_SHORT).show();
                        DataResponseEducation dre = response.body();
                        int id1 = dre.getEducation().getId();
                        editor = sharedPref.edit();
                        editor.putInt("id_edu",id1);
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Call<DataResponseEducation> call, Throwable t) {
                        Log.e("educationDialog","error uploading data: "+t.getMessage());
                    }
                });
                Intent i = new Intent(EducationDetailsActivity.this, ProfessionalDetailsActivity.class);
                startActivity(i);
            }
        });

        Button AddCertificatesButton = findViewById(R.id.add_certificate_button);
        AddCertificatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
                startActivityForResult(intent, READ_CERTIFICATE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_CERTIFICATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                File file = null;
                try {
                    file = FileUtil.from(EducationDetailsActivity.this,uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int id1 = sharedPref.getInt("id_in", 0);

                RequestBody filePart = RequestBody.create(
                        MediaType.parse(getContentResolver().getType(uri)),
                        file);
                MultipartBody.Part file1 = MultipartBody.Part.createFormData("photo",file.getName(),filePart);
                RequestBody id = RequestBody.create(MultipartBody.FORM, ""+id1);

                Call<Qse> call = jsonAPI.setCertificate(file1,id);
                call.enqueue(new Callback<Qse>() {
                    @Override
                    public void onResponse(Call<Qse> call, Response<Qse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(EducationDetailsActivity.this,"unsuccessful",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Qse q = response.body();
                        Log.v("uploded cert",response.message()+"");
                        String ss = q.getRes();
                        Toast.makeText(EducationDetailsActivity.this,ss,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Qse> call, Throwable t) {
                        Log.v("EducationActivity"+t.getMessage(),"Fail");
                    }
                });
            }
        }
    }
}
