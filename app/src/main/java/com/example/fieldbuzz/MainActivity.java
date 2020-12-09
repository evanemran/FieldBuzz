package com.example.fieldbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText fieldEmail, fieldPass;
    ProgressBar progressBar;
    String url = "https://recruitment.fisdev.com/api/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.loginButton);
        fieldEmail = findViewById(R.id.emailField);
        fieldPass = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.loader);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fieldEmail.getText().toString().isEmpty() && !fieldPass.getText().toString().isEmpty())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    authenticate();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Fill all credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void authenticate()
    {
        User user = new User(fieldEmail.getText().toString(),fieldPass.getText().toString());

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        APILogin apiLogin = retrofit.create(APILogin.class);
        Call<User> call = apiLogin.login(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                String token = response.body().getToken();
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
