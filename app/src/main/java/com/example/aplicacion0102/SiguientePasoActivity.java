package com.example.aplicacion0102;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SiguientePasoActivity extends AppCompatActivity {

    private static final String TAG = "SiguientePasoActivity";

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;

            if (batteryPct <= 15) {
                Toast.makeText(context, "Advertencia: Batería baja (" + (int) batteryPct + "%)", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_siguiente_paso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewResult = findViewById(R.id.textViewResult);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("userInput");
            if (value != null) {
                textViewResult.setText(value);
            }
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ListView listView = findViewById(R.id.miLista);
        String[] titulos = getResources().getStringArray(R.array.deportes_equipo);
        String[] subtitulos = getResources().getStringArray(R.array.deportes_equipo_desc);

        ListAdapter adapter = new ListAdapter(this, titulos, subtitulos);
        listView.setAdapter(adapter);

        String[] noticias = getResources().getStringArray(R.array.deportes_equipo_noticias);
        TypedArray imagenesTypedArray = getResources().obtainTypedArray(R.array.deportes_equipo_imagenes);
        int[] imagenIds = new int[imagenesTypedArray.length()];
        for (int i = 0; i < imagenesTypedArray.length(); i++) {
            imagenIds[i] = imagenesTypedArray.getResourceId(i, -1);
        }
        imagenesTypedArray.recycle();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            FragmentoEjemplo fragmentNews = (FragmentoEjemplo) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (fragmentNews != null) {
                fragmentNews.updateNews(noticias[position]);
            }

            FragmentoImagen fragmentImage = (FragmentoImagen) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerImage);
            if (fragmentImage != null) {
                fragmentImage.updateImage(imagenIds[position]);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }
}