package com.example.aplicacion0102;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewSharedPreferenceActivity extends AppCompatActivity {

    private static final String MIS_PREFERENCIAS = "MisPreferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_shared_preference);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewSelection = findViewById(R.id.textViewSelection);
        Button btnClearPrefs = findViewById(R.id.btnClearPrefs);

        mostrarUltimaSeleccion(textViewSelection);

        btnClearPrefs.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            textViewSelection.setText(getString(R.string.sin_preferencias));
            Toast.makeText(this, R.string.preferencias_eliminadas, Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarUltimaSeleccion(TextView textView) {
        SharedPreferences prefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        
        // Verificamos si existe al menos el título para saber si hay algo guardado
        if (prefs.contains("ultimo_titulo")) {
            String titulo = prefs.getString("ultimo_titulo", "");
            String descripcion = prefs.getString("ultima_descripcion", "");
            String noticia = prefs.getString("ultima_noticia", "");
            String imagen = prefs.getString("ultima_imagen_nombre", "");

            String resultado = "Equipo: " + titulo + "\n\n" +
                               "Descripción: " + descripcion + "\n\n" +
                               "Noticia: " + noticia + "\n\n" +
                               "Recurso imagen: " + imagen;
            
            textView.setText(resultado);
        } else {
            textView.setText(getString(R.string.sin_preferencias));
        }
    }
}