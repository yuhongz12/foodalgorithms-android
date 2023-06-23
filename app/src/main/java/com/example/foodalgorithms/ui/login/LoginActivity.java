package com.example.foodalgorithms.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailInput;
    EditText loginPasswordInput;
    Button loginButton;
    Button loginToRegisterButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        firebaseAuth = FirebaseAuth.getInstance();

        loginEmailInput = findViewById(R.id.LoginEmailInput);
        loginPasswordInput = findViewById(R.id.LoginPasswordInput);
        loginButton = findViewById(R.id.LoginButton);
        loginToRegisterButton = findViewById(R.id.LoginToRegisterButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loginEmailInput.getText().toString();
                String password = loginPasswordInput.getText().toString();
                // check if the username and password are correct and valid
                if(!username.isEmpty() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent loggedIn = new Intent(LoginActivity.this, MainActivity.class);
                                loggedIn.putExtra("currentUser", username);
                                startActivity(loggedIn);
                                finish();
                            }else{
                                if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(LoginActivity.this, "Username doesn't exist. Please register!", Toast.LENGTH_SHORT).show();
                                }else  {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Enter a username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterPage = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegisterPage);
                finish();
            }
        });
    }
}