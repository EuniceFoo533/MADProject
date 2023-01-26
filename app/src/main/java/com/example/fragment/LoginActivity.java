package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvForgetPassword,tvRegister;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference;

    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.editEmailAddress);
        edtPassword = findViewById(R.id.editPassword);

        //Button for login
        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccountInFirebase();
            }
        });

        //Reset password
        tvForgetPassword = findViewById(R.id.textViewForgetPassword);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Button register
        tvRegister = findViewById(R.id.textViewRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        progressBar = findViewById(R.id.progressBar);

    }

    private void loginAccountInFirebase()
    {
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            edtEmail.setError("Email is required");
        }

        if(TextUtils.isEmpty(password))
        {
            edtPassword.setError("Password is required");
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtEmail.setError("Email is invalid");

        }

        if(password.length()<=7)
        {
            edtPassword.setError("Password must contain 8 characters");

        }

        else{
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();

            changesInProgress(true);

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    changesInProgress(false);

                    if(task.isSuccessful())
                    {
                        if(firebaseAuth.getCurrentUser().isEmailVerified())
                        {
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }

                        else if(!firebaseAuth.getCurrentUser().isEmailVerified())
                        {
                            Toast.makeText(LoginActivity.this,"Email not verified.Please check your inbox.",Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(LoginActivity.this,"User not found.",Toast.LENGTH_SHORT).show();

                        }
                    }

                    else
                    {
                        Toast.makeText(LoginActivity.this,"Invalid username and password",Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }

    private void changesInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

}