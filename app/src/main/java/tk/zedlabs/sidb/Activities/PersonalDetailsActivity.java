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
import tk.zedlabs.sidb.POJO.DataResponsePersonal;
import tk.zedlabs.sidb.POJO.Education;
import tk.zedlabs.sidb.POJO.Personal;
import tk.zedlabs.sidb.POJO.Qse;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.FileUtil;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class PersonalDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int id;
    private JsonAPI jsonAPI;
    private static final int READ_PROFILE_PICTURE_REQUEST_CODE = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_dialog);
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

        initializeDialogs();

    }
    public void initializeDialogs(){

        id = sharedPref.getInt("id_in",0);
        final EditText skillsEdt = findViewById(R.id.edt_hobbies_per);
        final EditText mobileEdt = findViewById(R.id.edt_per_mobno);
        final EditText nameEdt =  findViewById(R.id.edt_per_name);
        final EditText linksEdt =  findViewById(R.id.edt_per_links);
        final EditText locationEdt =findViewById(R.id.edt_per_location);
        final EditText emailEdt = findViewById(R.id.edt_per_email);

        Button next1 = findViewById(R.id.buttonSubmitPer);

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skills = skillsEdt.getText().toString();
                String mobile = mobileEdt.getText().toString();
                String name = nameEdt.getText().toString();
                String links = linksEdt.getText().toString();
                String location = locationEdt.getText().toString();
                String email = emailEdt.getText().toString();

                Personal p = new Personal(skills,mobile,name,links,location,email);
                Call<DataResponsePersonal> call = jsonAPI.setPersonalDetails(id,p);
                call.enqueue(new Callback<DataResponsePersonal>() {
                    @Override
                    public void onResponse(Call<DataResponsePersonal> call, Response<DataResponsePersonal> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Log.v("personalDialog","successfully uploaded");
                        Toast.makeText(PersonalDetailsActivity.this,"uploaded!"+id,Toast.LENGTH_SHORT).show();
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Call<DataResponsePersonal> call, Throwable t) {
                        Log.e("personalDialog", "uploading failed");
                    }
                });
                Intent i = new Intent(PersonalDetailsActivity.this, EducationDetailsActivity.class);
                startActivity(i);
            }
        });

        Button uploadProfilePicture = findViewById(R.id.upload_profile_picture);
        uploadProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
                startActivityForResult(intent, READ_PROFILE_PICTURE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_PROFILE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                File file = null;
                try {
                    file = FileUtil.from(PersonalDetailsActivity.this,uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int id1 = sharedPref.getInt("id_in", 0);

                RequestBody filePart = RequestBody.create(
                        MediaType.parse(getContentResolver().getType(uri)),
                        file);
                MultipartBody.Part file1 = MultipartBody.Part.createFormData("photo",file.getName(),filePart);
                RequestBody id = RequestBody.create(MultipartBody.FORM, ""+id1);

                Call<Qse> call = jsonAPI.setProfilePicture(file1,id);
                call.enqueue(new Callback<Qse>() {
                    @Override
                    public void onResponse(Call<Qse> call, Response<Qse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(PersonalDetailsActivity.this,"unsuccessful",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Qse q = response.body();
                        Log.v("MainActivityinside",response.message()+"");
                        Toast.makeText(PersonalDetailsActivity.this,"it worked",Toast.LENGTH_SHORT).show();

                        String ss = q.getRes();
                        Toast.makeText(PersonalDetailsActivity.this,ss,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Qse> call, Throwable t) {
                        Log.v("PersonalDetaislActivity"+t.getMessage(),"Fail");
                    }
                });

            }
        }
    }
}
