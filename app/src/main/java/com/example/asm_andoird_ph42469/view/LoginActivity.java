package com.example.asm_andoird_ph42469.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_andoird_ph42469.Home;
import com.example.asm_andoird_ph42469.R;
import com.example.asm_andoird_ph42469.Modal.Response;
import com.example.asm_andoird_ph42469.Modal.User;
import com.example.asm_andoird_ph42469.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {

    EditText txtuser,txtpass;
    Button btndangnhap;
    TextView btndangnhapPhone,btndangky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getView();

        Intent intent = getIntent();
        if (intent != null){
            String user = intent.getStringExtra("user");
            String pass = intent.getStringExtra("pass");
            txtuser.setText(user);
            txtpass.setText(pass);
        }
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtuser.getText().toString();
                String password = txtpass.getText().toString();
                if (password.isEmpty() || username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User(username,password);
                    Call<Response<User>> call = ApiService.apiService.loginUser(user);
                    call.enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if(response.isSuccessful()){
                                if (response.body().getStatus() == 200){
                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,Home.class));
                                    Log.d("Login", "acc" + response.body().getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login fail"+ t, Toast.LENGTH_SHORT).show();
                            Log.d("Login", "err  : " + t);
                        }
                    });

                }
            }
        });
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void getView (){
        txtuser = findViewById(R.id.ed_username);
        txtpass = findViewById(R.id.ed_password);
        btndangnhap = findViewById(R.id.btn_login);
        btndangky = findViewById(R.id.daKy);
    }
}