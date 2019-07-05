package tk.zedlabs.sidb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tk.zedlabs.sidb.R;

public class TransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        Button transition = findViewById(R.id.button_transition);
        transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransitionActivity.this,PersonalDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}
