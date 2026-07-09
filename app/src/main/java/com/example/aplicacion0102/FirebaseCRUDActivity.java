package com.example.aplicacion0102;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCRUDActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseCRUD";
    private EditText editName;
    private Button btnSave;
    private ListView listView;
    private FirebaseFirestore db;
    
    private List<String> deviceIds = new ArrayList<>();
    private List<String> deviceDisplayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    
    private String selectedDeviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase_crud);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        editName = findViewById(R.id.editDeviceName);
        btnSave = findViewById(R.id.btnCreateDevice);
        listView = findViewById(R.id.listViewFirebase);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceDisplayList);
        listView.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            if (selectedDeviceId == null) {
                createDevice();
            } else {
                updateDevice();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedDeviceId = deviceIds.get(position);
            String currentName = deviceDisplayList.get(position);
            editName.setText(currentName);
            btnSave.setText(R.string.actualizar_btn);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteDevice(deviceIds.get(position));
            return true;
        });

        listenToChanges();
    }

    private void listenToChanges() {
        db.collection("devices").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Listen failed.", error);
                return;
            }

            deviceIds.clear();
            deviceDisplayList.clear();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    deviceIds.add(doc.getId());
                    deviceDisplayList.add(doc.getString("name"));
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void createDevice() {
        String name = editName.getText().toString();
        if (name.isEmpty()) return;

        Map<String, Object> device = new HashMap<>();
        device.put("name", name);

        db.collection("devices").add(device)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Creado: " + name, Toast.LENGTH_SHORT).show();
                    editName.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(this, R.string.error_firebase, Toast.LENGTH_SHORT).show());
    }

    private void updateDevice() {
        String name = editName.getText().toString();
        if (name.isEmpty() || selectedDeviceId == null) return;

        db.collection("devices").document(selectedDeviceId)
                .update("name", name)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, R.string.dispositivo_actualizado, Toast.LENGTH_SHORT).show();
                    resetForm();
                })
                .addOnFailureListener(e -> Toast.makeText(this, R.string.error_firebase, Toast.LENGTH_SHORT).show());
    }

    private void deleteDevice(String id) {
        db.collection("devices").document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, R.string.dispositivo_eliminado, Toast.LENGTH_SHORT).show();
                    if (id.equals(selectedDeviceId)) resetForm();
                })
                .addOnFailureListener(e -> Toast.makeText(this, R.string.error_firebase, Toast.LENGTH_SHORT).show());
    }

    private void resetForm() {
        editName.setText("");
        btnSave.setText(R.string.save);
        selectedDeviceId = null;
    }
}