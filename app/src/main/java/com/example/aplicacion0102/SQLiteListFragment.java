package com.example.aplicacion0102;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SQLiteListFragment extends Fragment {

    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sqlite_list, container, false);
        ListView listView = view.findViewById(R.id.listViewSQLite);

        dbHelper = new DatabaseHelper(getContext());
        loadData(listView);

        return view;
    }

    private void loadData(ListView listView) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_ID + " AS _id", DatabaseHelper.COLUMN_TITULO, DatabaseHelper.COLUMN_DESCRIPCION},
                null, null, null, null, DatabaseHelper.COLUMN_ID + " DESC"
        );

        String[] from = new String[]{DatabaseHelper.COLUMN_TITULO, DatabaseHelper.COLUMN_DESCRIPCION};
        int[] to = new int[]{R.id.textoTitulo, R.id.textoSubtitulo};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.list_item,
                cursor,
                from,
                to,
                0
        );

        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}