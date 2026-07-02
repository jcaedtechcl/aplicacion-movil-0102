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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";
    private TextView textViewResult;

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

        btnFetch.setOnClickListener(v -> fetchDevices());
    }

    private void fetchDevices() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Device>> call = apiService.getObjects();

        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Device> devices = response.body();
                    StringBuilder sb = new StringBuilder();
                    for (Device device : devices) {
                        sb.append(device.toString()).append("\n\n");
                    }
                    textViewResult.setText(sb.toString());
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