package com.example.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ToDoListFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String userID;

    int mYear, mMonth, mDay, mHour, mMinute;
    EditText edtDate,edtTime;
    Button btnDate, btnTime;
    FloatingActionButton btnAdd;
    RecyclerView recyclerView;

    FirebaseFirestore db;
    DocumentReference ref;

    ProgressDialog loader;

    ArrayList<Model> taskList;
    TaskAdapter adapterTask;

    String priorColor;

    public ToDoListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }
}