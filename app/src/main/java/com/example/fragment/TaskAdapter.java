package com.example.fragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Model> list;
    String key, name, desc, date, time, priorLevel,priorColor;
    int mYear, mMonth, mDay, mHour, mMinute;
    String userID;
    ProgressDialog loader;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ToDoList");



    public TaskAdapter(Context context, ArrayList<Model> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.retrieve_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {
        Model model = list.get(position);


        holder.taskID.setText(model.getTask_id());

        holder.taskName.setText(model.getTask_name());
        holder.priorLevel.setText(model.getPriority_level());
        if(model.getPrior_color()!= null && model.getPrior_color().equals("C Red"))
        {
            holder.btnPrior.setBackgroundColor(Color.RED);

        }

        else if(model.getPrior_color()!= null && model.getPrior_color().equals("B Yellow"))
        {
            holder.btnPrior.setBackgroundColor(Color.YELLOW);
        }

        else if(model.getPrior_color()!= null && model.getPrior_color().equals("A Green"))
        {
            holder.btnPrior.setBackgroundColor(Color.GREEN);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                // Set content view to add task
                View myView = inflater.inflate(R.layout.update_data, null);
                builder.setView(myView);

                // Create alert dialog
                AlertDialog dialog = builder.create();

                TextView mID = myView.findViewById(R.id.textViewTaskID);
                final EditText mName = myView.findViewById(R.id.editTaskName);
                final EditText mDesc = myView.findViewById(R.id.editTaskDescription);
                final EditText mDate = myView.findViewById(R.id.editTaskDate);
                mDate.setEnabled(false);

                final EditText mTime = myView.findViewById(R.id.editTaskTime);
                mTime.setEnabled(false);

                Spinner mPrior = myView.findViewById(R.id.textViewPrior);


                Button btnDate = myView.findViewById(R.id.buttonDate);
                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        mDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);

                        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                        datePickerDialog.show();

                    }
                });
                Button btnTime = myView.findViewById(R.id.buttonTime);
                btnTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        mTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });

                Button buttonUpdate = myView.findViewById(R.id.buttonUpdate);
                Button buttonDelete = myView.findViewById(R.id.buttonDelete);

                mID.setText(model.getTask_id());
                mName.setText(model.getTask_name());
                mDesc.setText(model.getTask_description());
                mDate.setText(model.getDate());
                mTime.setText(model.getTime());


                for (int i = 0; i < mPrior.getAdapter().getCount(); i++) {
                    if (mPrior.getAdapter().getItem(i).toString().contains(model.getPriority_level())) {
                        mPrior.setSelection(i);
                    }
                }

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = mID.getText().toString();
                        name = mName.getText().toString();
                        desc = mDesc.getText().toString();
                        date = mDate.getText().toString();
                        time = mTime.getText().toString();
                        priorLevel = mPrior.getSelectedItem().toString();

                        if(priorLevel.equals("Low"))
                        {
                            priorColor = "A Green";
                        }


                        if(priorLevel.equals("Medium"))
                        {
                            priorColor = "B Yellow";
                        }

                        if(priorLevel.equals("High"))
                        {
                            priorColor = "C Red";
                        }

                        if (TextUtils.isEmpty(name)) {
                            mID.setError("Code is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(desc)) {
                            mName.setError("Description is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(date)) {
                            mDesc.setError("Date is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(time)) {
                            mDate.setError("Time is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(time)) {
                            mTime.setError("Code is required.");
                            return;
                        }
                        else
                        {
                            loader = new ProgressDialog(context);
                            loader.setMessage("Update your data...");
                            loader.setCanceledOnTouchOutside(false);
                            loader.show();


                            Model model = new Model(key,name,desc,date,time,priorLevel,priorColor);

                            reference.child(key)
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"Task successfully added",Toast.LENGTH_SHORT).show();
                                            loader.dismiss();
                                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                            Fragment myFragment = new ToDoListFragment();
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error adding document",Toast.LENGTH_SHORT).show();
                                            loader.dismiss();
                                        }
                                    });

                            dialog.dismiss();
                        }
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    String data = mID.getText().toString();
                    @Override
                    public void onClick(View view)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setTitle("Alert");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which) -> {
                           reference.child(data).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Task has been deleted succesfully", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();


                                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                            Fragment myFragment = new ToDoListFragment();
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();



                                        }


                                    });

                        });

                        builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which) -> {
                            dialog.cancel();
                        });
                        builder.show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView taskName, priorLevel;
        TextView taskID;
        Button btnPrior;

        View mView;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            taskName = itemView.findViewById(R.id.textViewTaskName);
            priorLevel = itemView.findViewById(R.id.textViewPrior);
            taskID = itemView.findViewById(R.id.textViewTaskId);
            btnPrior = itemView.findViewById(R.id.buttonPriorColor);


        }
    }

}
