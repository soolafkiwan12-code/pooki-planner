package com.example.pookiplanner.fragments;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pookiplanner.R;
import com.example.pookiplanner.utils.SoundHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class TimerFragment extends Fragment {

    private static final long FOCUS_MS = 25 * 60 * 1000L;
    private static final long BREAK_MS = 5 * 60 * 1000L;

    private TextView timerDisplay, modeLabel;
    private MaterialCardView timerCard;
    private MaterialButton btnFocus, btnBreak, btnStart, btnPause, btnReset;
    private boolean isFocusMode = true;
    private long remainingMs = FOCUS_MS;
    private CountDownTimer timer;
    private boolean running;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timerDisplay = view.findViewById(R.id.textTimer);
        modeLabel = view.findViewById(R.id.textTimerMode);
        timerCard = view.findViewById(R.id.cardTimer);
        btnFocus = view.findViewById(R.id.btnFocusMode);
        btnBreak = view.findViewById(R.id.btnBreakMode);
        btnStart = view.findViewById(R.id.btnStart);
        btnPause = view.findViewById(R.id.btnPause);
        btnReset = view.findViewById(R.id.btnReset);

        updateDisplay();
        btnFocus.setOnClickListener(v -> switchMode(true));
        btnBreak.setOnClickListener(v -> switchMode(false));
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void switchMode(boolean focus) {
        pauseTimer();
        isFocusMode = focus;
        remainingMs = focus ? FOCUS_MS : BREAK_MS;
        modeLabel.setText(focus ? R.string.focus_mode : R.string.break_mode);
        int color = ContextCompat.getColor(requireContext(),
                focus ? R.color.timer_focus : R.color.timer_break);
        timerCard.setCardBackgroundColor(color);
        btnFocus.setTypeface(null, focus ? Typeface.BOLD : Typeface.NORMAL);
        btnBreak.setTypeface(null, focus ? Typeface.NORMAL : Typeface.BOLD);
        updateDisplay();
    }

    private void startTimer() {
        if (running) return;

        AudioManager audio = (AudioManager) requireContext().getSystemService(android.content.Context.AUDIO_SERVICE);
        if (audio != null && audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION) == 0
                && audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            Toast.makeText(requireContext(), R.string.timer_sound_hint, Toast.LENGTH_LONG).show();
        }

        running = true;
        final boolean wasFocus = isFocusMode;
        timer = new CountDownTimer(remainingMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMs = millisUntilFinished;
                updateDisplay();
            }
            @Override
            public void onFinish() {
                running = false;
                remainingMs = 0;
                updateDisplay();
                SoundHelper.playTimerAlarm(requireContext());
                Toast.makeText(requireContext(),
                        wasFocus ? R.string.focus_complete : R.string.break_complete,
                        Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    private void pauseTimer() {
        if (timer != null) timer.cancel();
        running = false;
    }

    private void resetTimer() {
        pauseTimer();
        remainingMs = isFocusMode ? FOCUS_MS : BREAK_MS;
        updateDisplay();
    }

    private void updateDisplay() {
        int total = (int) (remainingMs / 1000);
        int m = total / 60;
        int s = total % 60;
        timerDisplay.setText(String.format("%02d:%02d", m, s));
    }

    @Override
    public void onDestroyView() {
        pauseTimer();
        super.onDestroyView();
    }
}
