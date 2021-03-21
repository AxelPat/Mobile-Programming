package id.umn.ac.uts_32597;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnProfile = (Button) findViewById(R.id.btnProfile);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent profile = new Intent(MainActivity.this, Profile.class);
                startActivity(profile);
                setResult(RESULT_OK, null);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                setResult(RESULT_OK, null);
            }
        });
    }
}