package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class LectureNotesActivity extends AppCompatActivity {

    // Constants
    private static final int REQUEST_AUDIO_PERMISSION = 200;
    private static final String NOTES_PREF = "LectureNotes";
    private static final String NOTES_KEY = "saved_notes";

    // UI Components
    private MaterialButton btnSelectAudio, btnRecord, btnPlay, btnPause, btnStop, btnVoiceToText;
    private EditText etNotes;
    private ImageView beeIcon;

    // Audio Components
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private Uri selectedAudioUri;

    // States
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean permissionGranted = false;

    // Activity Result Launchers
    private ActivityResultLauncher<String> audioPicker;
    private ActivityResultLauncher<Intent> speechRecognizerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_notes);

        // Initialize views
        initViews();

        // Request permissions
        requestAudioPermission();

        // Setup audio file path
        setupAudioFilePath();

        // Load saved notes
        loadSavedNotes();

        // Setup Activity Result Launchers
        setupActivityResultLaunchers();

        // Setup listeners
        setupListeners();

        // Setup bee animation (optional)
        setupBeeAnimation();
    }

    private void initViews() {
        btnSelectAudio = findViewById(R.id.btn_select_audio);
        btnRecord = findViewById(R.id.btn_record);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        etNotes = findViewById(R.id.et_notes);
        beeIcon = findViewById(R.id.beeIcon);

        // Initially disable play controls
        updatePlayControlsState(false);
    }

    private void setupActivityResultLaunchers() {
        // Audio Picker Launcher
        audioPicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedAudioUri = uri;
                        Toast.makeText(this, "✅ Audio selected!", Toast.LENGTH_SHORT).show();
                        setupMediaPlayer(uri);
                        updatePlayControlsState(true);
                    }
                }
        );

        // Speech Recognizer Launcher (GRATUIT - Android natif)
        speechRecognizerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        if (matches != null && !matches.isEmpty()) {
                            String recognizedText = matches.get(0);

                            // Ajoute le texte à la suite des notes existantes
                            String currentText = etNotes.getText().toString();
                            if (!currentText.isEmpty()) {
                                etNotes.append("\n\n" + recognizedText);
                            } else {
                                etNotes.setText(recognizedText);
                            }

                            Toast.makeText(this, "✅ Voice transcribed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "❌ Voice recognition cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupListeners() {
        // Select Audio Button
        btnSelectAudio.setOnClickListener(v -> audioPicker.launch("audio/*"));

        // Record Button (Enregistrement classique)
        btnRecord.setOnClickListener(v -> {
            if (permissionGranted) {
                if (!isRecording) {
                    startRecording();
                } else {
                    stopRecording();
                }
            } else {
                Toast.makeText(this, "Permission denied! Please grant audio permission.", Toast.LENGTH_SHORT).show();
                requestAudioPermission();
            }
        });

        // Play Button
        btnPlay.setOnClickListener(v -> startPlaying());

        // Pause Button
        btnPause.setOnClickListener(v -> pausePlaying());

        // Stop Button
        btnStop.setOnClickListener(v -> stopPlaying());

        // Auto-save notes
        etNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveNotes();
            }
        });
    }

    private void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_AUDIO_PERMISSION);
        } else {
            permissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            permissionGranted = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionGranted) {
                Toast.makeText(this, "Audio permission is required for recording", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupAudioFilePath() {
        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "StudyBee");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        audioFilePath = dir.getAbsolutePath() + "/lecture_recording.3gp";
    }

    // ==================== RECORDING ====================

    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            btnRecord.setText("Stop Recording");
            btnRecord.setBackgroundTintList(getColorStateList(android.R.color.holo_red_light));
            btnRecord.setTextColor(getColor(android.R.color.white));

            Toast.makeText(this, "🎙️ Recording started...", Toast.LENGTH_SHORT).show();

            // Disable other controls while recording
            btnSelectAudio.setEnabled(false);
            updatePlayControlsState(false);

        } catch (IOException e) {
            Toast.makeText(this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                isRecording = false;
                btnRecord.setText("Record");
                btnRecord.setBackgroundTintList(getColorStateList(android.R.color.holo_orange_light));
                btnRecord.setTextColor(getColor(android.R.color.black));

                Toast.makeText(this, "✅ Recording saved!", Toast.LENGTH_SHORT).show();

                // Enable play controls for the recorded audio
                selectedAudioUri = Uri.fromFile(new File(audioFilePath));
                setupMediaPlayer(selectedAudioUri);
                updatePlayControlsState(true);
                btnSelectAudio.setEnabled(true);

            } catch (RuntimeException e) {
                Toast.makeText(this, "Stop recording failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ==================== PLAYBACK ====================

    private void setupMediaPlayer(Uri uri) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, uri);

        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                stopPlaying();
                Toast.makeText(this, "✅ Playback completed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void startPlaying() {
        if (selectedAudioUri == null && !new File(audioFilePath).exists()) {
            Toast.makeText(this, "⚠️ No audio file available. Please record or select one.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            btnPlay.setEnabled(false);
            btnPause.setEnabled(true);
            btnStop.setEnabled(true);
            Toast.makeText(this, "▶️ Playing audio...", Toast.LENGTH_SHORT).show();
        }
    }

    private void pausePlaying() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
            Toast.makeText(this, "⏸️ Paused", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;

            if (selectedAudioUri != null) {
                setupMediaPlayer(selectedAudioUri);
            }

            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
            btnStop.setEnabled(false);
        }
    }

    // ==================== RECONNAISSANCE VOCALE GRATUITE ====================

    /**
     * Lance la reconnaissance vocale Android native (100% GRATUIT)
     * Fonctionne en temps réel sans enregistrer de fichier audio
     */
    private void startVoiceToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "🎤 Speak now to transcribe...");

        try {
            speechRecognizerLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Voice recognition not available on this device", Toast.LENGTH_SHORT).show();
        }
    }

    // ==================== NOTES MANAGEMENT ====================

    private void saveNotes() {
        getSharedPreferences(NOTES_PREF, MODE_PRIVATE)
                .edit()
                .putString(NOTES_KEY, etNotes.getText().toString())
                .apply();
    }

    private void loadSavedNotes() {
        SharedPreferences prefs = getSharedPreferences(NOTES_PREF, MODE_PRIVATE);
        etNotes.setText(prefs.getString(NOTES_KEY, ""));
    }

    // ==================== UI HELPERS ====================

    private void updatePlayControlsState(boolean enabled) {
        btnPlay.setEnabled(enabled);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
    }

    private void setupBeeAnimation() {
        if (beeIcon != null) {
            // Clic sur l'abeille lance la reconnaissance vocale GRATUITE
            beeIcon.setOnClickListener(v -> {
                Toast.makeText(this, "🐝 Click to transcribe speech!", Toast.LENGTH_SHORT).show();
                startVoiceToText();
            });
        }
    }

    // ==================== LIFECYCLE ====================

    @Override
    protected void onPause() {
        super.onPause();
        saveNotes();
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNotes();

        // Release recording resources
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        // Release playback resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        saveNotes();
        super.onBackPressed();
    }
}