package tk.zedlabs.sidb.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.POJO.DataResponse;
import tk.zedlabs.sidb.POJO.User;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.JsonAPI;

public class SignupActivity extends AppCompatActivity {

    JsonAPI jsonAPI;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedPref = getApplicationContext().getSharedPreferences("MainPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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

        final EditText emailEdt = findViewById(R.id.editText_signup);
        final EditText passwordEdt = findViewById(R.id.editText2_signup);
        final Button signUpButton = findViewById(R.id.signup_button);
        TextView gotoLogin = findViewById(R.id.goto_login_text);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                createUser(email,password);
                Intent i = new Intent(SignupActivity.this, TransitionActivity.class);
                startActivity(i);
            }
        });
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void createUser(String email, String password) {
        User user = new User(email, password);
        Call<DataResponse> call = jsonAPI.createUser(user);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("signUp","signUp unsuccessful" + response.code());
                    return;
                }
                DataResponse data = response.body();
                Integer id = data.getAuthData().getId();
                String email = data.getAuthData().getEmail();
                String content = id + " , " + email + "\n";
                Log.v("signUp done", content);
                editor.putInt("id_in", id);
                editor.commit();
                Toast.makeText(SignupActivity.this,"id : "+ sharedPref.getInt("id_in",0),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.v("onFailure",t.getMessage());
            }
        });
    }
}
