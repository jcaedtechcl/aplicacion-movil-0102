package com.example.aplicacion0102;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] titulos;
    private final String[] subtitulos;

    public ListAdapter(Context context, String[] titulos, String[] subtitulos) {
        super(context, R.layout.list_item, titulos);
        this.context = context;
        this.titulos = titulos;
        this.subtitulos = subtitulos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView tvTitulo = rowView.findViewById(R.id.textoTitulo);
        TextView tvSubtitulo = rowView.findViewById(R.id.textoSubtitulo);

        tvTitulo.setText(titulos[position]);
        tvSubtitulo.setText(subtitulos[position]);

        return rowView;
    }
}