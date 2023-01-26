package com.example.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FeedbackFragment extends Fragment {

    EditText email,suggestion;
    RadioButton btnYes,btnNo;
    Button btnSubmit;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uEmail = firebaseAuth.getCurrentUser().getEmail();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


    String uKey,uSelect,uSuggest;
    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = getView().findViewById(R.id.editEmail);
        email.setText(uEmail);
        email.setEnabled(false);

        suggestion = getView().findViewById(R.id.editSuggestion);
        btnYes = getView().findViewById(R.id.yes);
        btnNo = getView().findViewById(R.id.no);



        btnSubmit = getView().findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uKey = myRef.push().getKey();
                uEmail = email.getText().toString();
                if(btnYes.isChecked())
                {
                    uSelect = btnYes.getText().toString();
                }
                else if(btnNo.isChecked())
                {
                    uSelect = btnNo.getText().toString();
                }

                uSuggest = suggestion.getText().toString();
                Feedback feedback = new Feedback(uEmail,uSelect,uSuggest);

                myRef.child("Feedback")
                        .child(uKey)
                        .setValue(feedback)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(),"Thanks for your feedback", Toast.LENGTH_SHORT).show();
                                suggestion.setText("");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"Opps... Please try again later...",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }
}