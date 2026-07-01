package com.example.aplicacion0102;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContentProviderActivity extends AppCompatActivity {

    private static final String TAG = "SettingsApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content_provider);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewSettingsList = findViewById(R.id.textViewSettingsList);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        consultarConfiguraciones(textViewSettingsList);

        btnRefresh.setOnClickListener(v -> consultarConfiguraciones(textViewSettingsList));
    }

    private void consultarConfiguraciones(TextView textView) {
        ContentResolver contentResolver = getContentResolver();
        StringBuilder sb = new StringBuilder();

        try {
            // 1. Consultar de la tabla System (Brillo de la pantalla)
            int brilloPantalla = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            String brilloMsg = "Brillo de pantalla actual: " + brilloPantalla;
            Log.d(TAG, brilloMsg);
            sb.append(brilloMsg).append("\n\n");

            // 2. Consultar de la tabla Secure (Opciones de desarrollador)
            int opcionesDesarrollador = Settings.Global.getInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
            String devOptsMsg = "Opciones de desarrollador activas: " + (opcionesDesarrollador == 1);
            Log.d(TAG, devOptsMsg);
            sb.append(devOptsMsg).append("\n\n");

            // 3. Consultar de la tabla Global (Modo Avión)
            int modoAvion = Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0);
            String modoAvionMsg = "Modo Avión activo: " + (modoAvion == 1);
            Log.d(TAG, modoAvionMsg);
            sb.append(modoAvionMsg);

        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "La configuración solicitada no fue encontrada", e);
            sb.append("Error: Alguna configuración no fue encontrada.");
        }

        textView.setText(sb.toString());
    }
}