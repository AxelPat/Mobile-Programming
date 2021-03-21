package id.umn.ac.uts_32597;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button inputLogin = (Button) findViewById(R.id.inputLogin);
        EditText usrname = (EditText) findViewById(R.id.usrname);
        EditText pass = (EditText) findViewById(R.id.pass);

        inputLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = usrname.getText().toString();
                String pwd = pass.getText().toString();
                if(name.equals("uasmobile") && pwd.equals("uasmobilegenap")){
                    Intent inputLogin = new Intent(LoginActivity.this, ListActivity.class);
                    startActivity(inputLogin);
                    setResult(RESULT_OK, null);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
