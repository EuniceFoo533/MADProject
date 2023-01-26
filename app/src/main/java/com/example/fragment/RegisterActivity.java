package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail,editPassword, editConfirmPassword;
    TextView tvLogin;
    Button btnRegister;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    String email, password, confirmPassword;
    final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);

        // Go to login page
        tvLogin = findViewById(R.id.textViewLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // Register an account
        btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountInFirebase();
            }
        });

        progressBar = findViewById(R.id.progressBar);

    }

    private void createAccountInFirebase() {

        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();

        if(email.matches("")  && password.matches("") && confirmPassword.matches(""))
        {
            Toast.makeText(RegisterActivity.this,"Every fields required to fill",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(email.matches(""))
        {
            Toast.makeText(RegisterActivity.this,"Email required to fill",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(password.matches(""))
        {
            Toast.makeText(RegisterActivity.this,"Password required to fill",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(confirmPassword.matches(""))
        {
            Toast.makeText(RegisterActivity.this,"Confirm password required to fill",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(RegisterActivity.this,"Password and confirm password not matching.",Toast.LENGTH_SHORT).show();
            return;
        }

        else if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this,"Password is too weak. \nAt least 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }

        else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(RegisterActivity.this,"Must contain at least 1 special character",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(RegisterActivity.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override

                public void onComplete(@NonNull Task<AuthResult> task) {
                    changesInProgress(false);
                    if(task.isSuccessful())
                    {
                        changesInProgress(true);
                        Toast.makeText(RegisterActivity.this,"Successfully register",Toast.LENGTH_SHORT).show();

                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();

                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);



                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
            btnRegister.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }
}