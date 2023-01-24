package com.example.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


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

        db.collection("Task")
                .whereEqualTo("user_id",userID)
                .orderBy("prior_color", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        taskList.clear();

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {

                            Model model= new Model(snapshot.getId(),
                                    snapshot.getString("task_name"),
                                    snapshot.getString("task_desc"),
                                    snapshot.getString("task_date"),
                                    snapshot.getString("task_time"),
                                    snapshot.getString("prior_level"),
                                    snapshot.getString("prior_color"),
                                    snapshot.getString("user_id"));
                            taskList.add(model);
                        }
                        adapterTask.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapterTask);
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
                    priorColor = "C Green";
                }

                else if(priorLevel.equals("Medium"))
                {
                    priorColor = "B Yellow";
                }

                else if(priorLevel.equals("High"))
                {
                    priorColor = "A Red";
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

                    ref = db.collection("Task").document();
                    String id = ref.getId();

                    Map<String,Object> tasks = new HashMap<>();
                    tasks.put("user_id",userID);
                    tasks.put("task_name",mTask);
                    tasks.put("task_desc",mDescription);
                    tasks.put("task_date",mDate);
                    tasks.put("task_time",mTime);
                    tasks.put("prior_level",priorLevel);
                    tasks.put("prior_color",priorColor);

                    db.collection("Task")
                            .add(tasks)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(getContext(),"Task successfully added",Toast.LENGTH_SHORT).show();
                                    loader.dismiss();
                                    getActivity().finish();
                                    startActivity(getActivity().getIntent());

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
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