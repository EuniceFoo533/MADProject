package com.example.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;


public class TimerFragment extends Fragment {

    private static final long START_TIME_IN_MILIS = 1500000;

    private ProgressBar progressBar;
    private Button buttonStart, buttonReset,buttonSet;
    private TextView textViewTimer;
    private EditText editTimer;

    private CountDownTimer countDownTimer;
    private  boolean timerRunning;
    private boolean timerStopping;

    private long timeLeftMilis;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    // Use to return root view
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = getView().findViewById(R.id.progressBar2);
        textViewTimer = getView().findViewById(R.id.tvCountDown);

        buttonStart = getView().findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        buttonReset = getView().findViewById(R.id.buttonStop);
        buttonReset.setVisibility(View.INVISIBLE);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                progressBar.setProgress(1500000);
            }
        });
        updateCountDownText();

    }

    //Method start timer
    private void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeftMilis,1000) {
            @Override
            public void onTick(long msUntilFinished) {
                timeLeftMilis = msUntilFinished;
                updateCountDownText();
                progressBar.setMax((int)START_TIME_IN_MILIS);
                progressBar.setProgress((int)msUntilFinished);

            }

            @Override
            public void onFinish() {
                timerRunning = false;
                buttonStart.setText("Start");
                buttonStart.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);

            }
        }.start();

        timerRunning = true;
        buttonStart.setText("Pause");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pause_timer, 0);
        buttonReset.setVisibility(View.INVISIBLE);

    }

    //Method pause the timer
    private void pauseTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;
        buttonStart.setText("Resume");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.start_timer, 0);
        buttonReset.setVisibility(View.VISIBLE);

    }

    private void resetTimer()
    {
        timeLeftMilis = START_TIME_IN_MILIS;
        updateCountDownText();
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.VISIBLE);
        buttonStart.setText("Start");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.start_timer, 0);



    }

    private void updateCountDownText()
    {

        int minutes = (int) (timeLeftMilis/1000)/60;
        int seconds = (int) (timeLeftMilis/1000)%60;
        int hours = 0;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        textViewTimer.setText(timeLeftFormatted);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_timer, container, false);
    }
}