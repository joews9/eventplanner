package com.event.joe.eventplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.event.joe.datastorage.MySQLiteHelper;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    MySQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView signUp = (TextView)findViewById(R.id.link_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        Button button =  (Button)findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign in logic
                EditText etUsername = (EditText)findViewById(R.id.input_username);
                EditText etPassword = (EditText)findViewById(R.id.input_password);

                String password = etPassword.getText().toString();
                String username = etUsername.getText().toString();

                sqLiteHelper = new MySQLiteHelper(SplashActivity.this);
                List<String> usernames = sqLiteHelper.getAllUsernames();
                boolean userNameFound = false;

                for(int i = 0; i < usernames.size(); i++){
                    if(usernames.get(i).equals(username)){
                        userNameFound = true;
                        break;
                    }
                }

                if(username.length() < 1 || password.length() < 1){
                    Toast.makeText(SplashActivity.this, "Not all fields have been filled in", Toast.LENGTH_SHORT).show();
                }else{
                    if(userNameFound){
                        String passwordCurrent = sqLiteHelper.getPassword(username);
                        if(passwordCurrent.equals(password)){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SplashActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SplashActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                        etUsername.getText().clear();
                        etPassword.getText().clear();
                    }

                }


            }
        });


    }
}
