package com.example.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;


public class ToDoListFragment extends Fragment{


    int mYear, mMonth, mDay, mHour, mMinute;
    EditText edtDate,edtTime;
    Button btnDate, btnTime;
    FloatingActionButton btnAdd;
    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    FirebaseFirestore db;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ToDoList");

    ProgressDialog loader;

    ArrayList<Model> taskList;
    TaskAdapter adapterTask;

    String priorColor;
    String userID = firebaseAuth.getCurrentUser().getUid();


    public ToDoListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get current userID
        //userID = firebaseAuth.getCurrentUser().getUid();

        btnAdd = getView().findViewById(R.id.buttonAddTask);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        recyclerView = getView().findViewById(R.id.recyclerViewPrior);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(getContext());

        db = FirebaseFirestore.getInstance();
        taskList = new ArrayList<Model>();
        adapterTask = new TaskAdapter(getContext(),taskList);
        recyclerView.setAdapter(adapterTask);

        reference
                .orderByChild("prior_color").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren())
                {
                    if(userID!= null && item.child("user_id").getValue(String.class).equals(userID))
                    {
                        String id = item.child("task_id").getValue(String.class);
                        String name = item.child("task_name").getValue(String.class);
                        String desc = item.child("task_description").getValue(String.class);
                        String date = item.child("date").getValue(String.class);
                        String time = item.child("time").getValue(String.class);
                        String prior = item.child("priority_level").getValue(String.class);
                        String color = item.child("prior_color").getValue(String.class);
                        String uID = item.child("user_id").getValue(String.class);
                        Model model = new Model(id,name,desc,date,time,prior,color,uID);

                        taskList.add(model);
                    }

                }
                adapterTask.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void addTask()
    {
        // Set alert dialog
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Set content view to add task
        View myView = inflater.inflate(R.layout.input_data,null);
        myDialog.setView(myView);

        // Create alert dialog
        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);


        // Bind the element by findViewById
        final EditText task = myView.findViewById(R.id.editTaskName);
        final EditText desc = myView.findViewById(R.id.editTaskDesc);
        final EditText date = myView.findViewById(R.id.editTaskDate);
        final EditText time = myView.findViewById(R.id.editTaskTime);

        Spinner tvPrior  = myView.findViewById(R.id.textViewPrior);
        Button save = myView.findViewById(R.id.saveTask);
        Button cancel = myView.findViewById(R.id.cancelTask);

        edtDate = myView.findViewById(R.id.editTaskDate);
        edtDate.setEnabled(false);
        edtTime = myView.findViewById(R.id.editTaskTime);
        edtTime.setEnabled(false);

        btnDate = myView.findViewById(R.id.buttonDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();

            }
        });
        btnTime = myView.findViewById(R.id.buttonTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });


        // When cancel button click
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //When save button click
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set random task id
                String mTask = task.getText().toString();
                String mDescription = desc.getText().toString();
                String mDate = date.getText().toString();
                String mTime = time.getText().toString();
                String priorLevel = tvPrior.getSelectedItem().toString();

                if(priorLevel.equals("Low"))
                {
                    priorColor = "A Green";
                }

                else if(priorLevel.equals("Medium"))
                {
                    priorColor = "B Yellow";
                }

                else if(priorLevel.equals("High"))
                {
                    priorColor = "C Red";
                }


                if(TextUtils.isEmpty(mTask) || TextUtils.isEmpty(mDescription) || TextUtils.isEmpty(mDate) || TextUtils.isEmpty(mTime) )
                {
                    task.setError("Field Required");
                    return;
                }

                else
                {
                    loader.setMessage("Adding your data");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String key = reference.push().getKey();


                    Model model = new Model(key,mTask,mDescription,mDate,mTime,priorLevel,priorColor,userID);

                    reference.child(key)
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(),"Task successfully added",Toast.LENGTH_SHORT).show();
                                    loader.dismiss();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    Fragment myFragment = new ToDoListFragment();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error adding document",Toast.LENGTH_SHORT).show();
                                    loader.dismiss();
                                }
                            });

                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }
}