package com.example.foodalgorithms.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodalgorithms.MainActivity;
import com.example.foodalgorithms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText registerEmailInput;
    EditText registerPasswordInput;
    Button registerButton;

    Button registerToLoginButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        registerEmailInput = findViewById(R.id.RegisterEmailInput);
        registerPasswordInput = findViewById(R.id.RegisterPasswordInput);
        registerButton = findViewById(R.id.RegisterButton);
        registerToLoginButton = findViewById(R.id.RegisterToLoginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = registerEmailInput.getText().toString();
                String password = registerPasswordInput.getText().toString();
                if(!username.isEmpty() && !password.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent loggedIn = new Intent(RegisterActivity.this, MainActivity.class);
                                loggedIn.putExtra("currentUser", username);
                                startActivity(loggedIn);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Enter a username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginPage = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(toLoginPage);
                finish();
            }
        });



    }
}