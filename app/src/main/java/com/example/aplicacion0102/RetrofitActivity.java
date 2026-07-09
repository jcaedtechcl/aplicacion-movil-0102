package com.example.aplicacion0102;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";
    private TextView textViewResult;
    private Button btnSaveFirebase;
    private List<Device> deviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_retrofit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewResult = findViewById(R.id.textViewRetrofitResult);
        Button btnFetch = findViewById(R.id.btnFetchData);
        btnSaveFirebase = findViewById(R.id.btnSaveFirebase);

        btnFetch.setOnClickListener(v -> fetchDevices());

        btnSaveFirebase.setOnClickListener(v -> saveToFirebase());
    }

    private void saveToFirebase() {
        if (deviceList.isEmpty()) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        for (Device device : deviceList) {
            // Si el dispositivo tiene ID de la API, lo usamos como ID de documento,
            // si no, dejamos que Firestore genere uno.
            if (device.getId() != null && !device.getId().isEmpty()) {
                batch.set(db.collection("devices").document(device.getId()), device);
            } else {
                batch.set(db.collection("devices").document(), device);
            }
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Datos guardados en Firestore correctamente", Toast.LENGTH_LONG).show();
            btnSaveFirebase.setEnabled(false);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error al guardar en Firestore", e);
            Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchDevices() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Device>> call = apiService.getObjects();

        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    deviceList = response.body();
                    StringBuilder sb = new StringBuilder();
                    for (Device device : deviceList) {
                        sb.append(device.toString()).append("\n\n");
                    }
                    textViewResult.setText(sb.toString());
                    btnSaveFirebase.setEnabled(true);
                } else {
                    Toast.makeText(RetrofitActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(RetrofitActivity.this, "Falla en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}