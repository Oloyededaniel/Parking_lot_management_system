package com.example.parkinglot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Locale;

public class ParkingDetailsActivity extends AppCompatActivity {

    private TextView parkingLotTextView;
    private EditText licensePlateEditText;
    private TimePicker timePicker;
    private TextView countdownTextView;
    private TextView expirationTextView;
    private Button buyButton;

    private CountDownTimer countDownTimer;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "ParkingAlertChannel";
    private static final String CHANNEL_NAME = "Parking Alert";
    private static final String CHANNEL_DESCRIPTION = "Channel for parking alerts";

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        // Get the parking lot name from the intent
        Intent intent = getIntent();
        String parkingLot = intent.getStringExtra("parkingLot");

        // Set the parking lot name in the TextView
        parkingLotTextView = findViewById(R.id.parking_lot_name);
        parkingLotTextView.setText(parkingLot);

        // Initialize views
        licensePlateEditText = findViewById(R.id.license_plate_input);
        timePicker = findViewById(R.id.duration_picker);
        countdownTextView = findViewById(R.id.parking_timer);
        expirationTextView = findViewById(R.id.parking_expiration);
        buyButton = findViewById(R.id.buy_parking_button);

        // Set the minimum time for the time picker
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        // Set click listener on buy button
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the license plate
                String licensePlate = licensePlateEditText.getText().toString().trim();
                if (licensePlate.isEmpty() || licensePlate.matches("^\\s*$")) {
                    licensePlateEditText.setError("License plate cannot be blank");
                    return;
                }

                // Get the selected duration from the time picker
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                long durationMillis = (hour * 60 + minute) * 60 * 1000;

                // Calculate the expiration time
                long expirationTimeMillis = System.currentTimeMillis() + durationMillis;
                Calendar expirationCalendar = Calendar.getInstance();
                expirationCalendar.setTimeInMillis(expirationTimeMillis);
                String expirationTime = String.format(Locale.getDefault(), "Expiration Time: %02d:%02d %s",
                        expirationCalendar.get(Calendar.HOUR),
                        expirationCalendar.get(Calendar.MINUTE),
                        expirationCalendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
                expirationTextView.setText(expirationTime);

                // Display a toast message to show parking was bought successfully
                Toast.makeText(ParkingDetailsActivity.this, "Parking bought successfully!", Toast.LENGTH_SHORT).show();

                // Start the countdown timer
                startCountDownTimer(durationMillis);

                // Create a notification channel (for API
                // Create a notification channel (for API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(CHANNEL_DESCRIPTION);
                    notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                } else {
                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                }
            }

            private void startCountDownTimer(long durationMillis) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                countDownTimer = new CountDownTimer(durationMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // Update the countdown timer TextView
                        long minutes = millisUntilFinished / 60000;
                        long seconds = (millisUntilFinished / 1000) % 60;
                        String countdown = String.format(Locale.getDefault(), "Time Left: %02d:%02d", minutes, seconds);
                        countdownTextView.setText(countdown);
                    }

                    @Override
                    public void onFinish() {
                        // Update the countdown timer TextView and show a notification
                        countdownTextView.setText("Parking Expired");
                        showNotification();
                        playSound();
                    }
                }.start();
            }

            private void showNotification() {
                Intent intent = new Intent(ParkingDetailsActivity.this, ParkingDetailsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ParkingDetailsActivity.this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ParkingDetailsActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_parking)
                        .setContentTitle("Parking Alert")
                        .setContentText("Parking Expired")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                notificationManager.notify(0, builder.build());
            }

            private void playSound() {
                MediaPlayer mediaPlayer = MediaPlayer.create(ParkingDetailsActivity.this, R.raw.alarm_sound);
                mediaPlayer.start();
            }
        });
    }
}

