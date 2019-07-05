package tk.zedlabs.sidb.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tk.zedlabs.sidb.POJO.Qse;
import tk.zedlabs.sidb.Utils.FileUtil;
import tk.zedlabs.sidb.Utils.JsonAPI;
import tk.zedlabs.sidb.POJO.Test;
import tk.zedlabs.sidb.R;

public class MainActivity extends AppCompatActivity {

    private TextView testResult;
    private JsonAPI jsonAPI;
    private ImageView imageView11;
    private int id;
    public String uniqueUserId = "id";
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_PROFILE_PICTURE_REQUEST_CODE = 77;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private TextView ttvc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttvc = findViewById(R.id.tv_per_hobbies);

        Intent i = new Intent(MainActivity.this,SignupActivity.class);
        startActivity(i);

        //getStatus();
        //performFileSearch();
        //getProfilePicture(id);
        //setProfilePicture();
        //setPersonalDetails();
    }


    private void getProfilePicture(int id) {
        Call<ResponseBody> call = jsonAPI.getProfilePicture(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    testResult.setText(response.code());
                    return;
                }
                Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                imageView11.setImageBitmap(image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                testResult.setText(t.getMessage());
            }
        });
    }

    private void getStatus() {
        Call<Test> call = jsonAPI.getTestResult();
        call.enqueue(new Callback<Test>() {
            @Override
            public void onResponse(Call<Test> call, Response<Test> response) {
                if (!response.isSuccessful()) {
                    testResult.setText(response.code());
                    return;
                }
                Test testResults = response.body();
                testResult.append(testResults.getServerName() + "\n");
                testResult.append(testResults.getUrl() + "\n");
                testResult.append(testResults.getStatus());
            }

            @Override
            public void onFailure(Call<Test> call, Throwable t) {
                testResult.setText(t.getMessage());
            }
        });
    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                ImageView iv = findViewById(R.id.imageView2);
                try {
                    iv.setImageBitmap(getBitmapFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
