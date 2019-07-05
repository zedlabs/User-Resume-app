package tk.zedlabs.sidb.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.zedlabs.sidb.Fragments.EducationDetailsFragment;
import tk.zedlabs.sidb.Fragments.PersonalDetailsFragment;
import tk.zedlabs.sidb.Fragments.ProfessionalDetailsFragment;
import tk.zedlabs.sidb.R;
import tk.zedlabs.sidb.Utils.JsonAPI;
import tk.zedlabs.sidb.Utils.TabAdapter;

public class SetupActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    JsonAPI jsonAPI;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    int id_edu;
    int id_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        sharedPref = getApplicationContext().getSharedPreferences("MainPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        id_edu = sharedPref.getInt("id_edu",0);
        id_pro = sharedPref.getInt("id_pro",0);

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
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalDetailsFragment(), "Personal");
        adapter.addFragment(new EducationDetailsFragment(), "Education");
        adapter.addFragment(new ProfessionalDetailsFragment(), "Professional");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.inflateMenu(R.menu.menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_education_details){
            Call<ResponseBody> call = jsonAPI.deleteEducationDetails(id_edu);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Snackbar.make(getParent().getCurrentFocus(), "Deleted Education Details", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            return true;
        }

        else if(item.getItemId() == R.id.delete_professional_details){
            Call<ResponseBody> call = jsonAPI.deleteProfessionalDetails(id_pro);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Snackbar.make(getParent().getCurrentFocus(), "Deleted Professional Details", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
                return true;
            }

        else
        {return super.onOptionsItemSelected(item);}

    }
}
