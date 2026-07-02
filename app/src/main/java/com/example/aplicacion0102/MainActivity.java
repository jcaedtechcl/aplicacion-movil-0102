package com.example.aplicacion0102;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;

            if (batteryPct == 15) {
                Toast.makeText(context, "Advertencia: Batería baja (" + (int) batteryPct + "%)", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextName = findViewById(R.id.editTextName);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnContentProviders = findViewById(R.id.btnContentProviders);
        Button btnSharedPrefs = findViewById(R.id.btnSharedPrefs);
        Button btnSQLite = findViewById(R.id.btnSQLite);

        btnNext.setOnClickListener(v -> {
            String textToSend = editTextName.getText().toString();
            Intent intent = new Intent(MainActivity.this, SiguientePasoActivity.class);
            intent.putExtra("userInput", textToSend);
            startActivity(intent);
        });

        btnContentProviders.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContentProviderActivity.class);
            startActivity(intent);
        });

        btnSharedPrefs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewSharedPreferenceActivity.class);
            startActivity(intent);
        });

        btnSQLite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SQLiteActivity.class);
            startActivity(intent);
        });

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }
}