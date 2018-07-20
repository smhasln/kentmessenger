package com.kentkolej.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomYeniGrupListe extends BaseAdapter {

    String dizi_basliklar[];
    String dizi_icerikler[];
    String dizi_idler[];

    LayoutInflater layoutInflater = null;


    public CustomYeniGrupListe(YeniGrup kisiler, String basliklar[], String icerikler[], String idler[])
    {
        this.dizi_basliklar = basliklar;
        this.dizi_icerikler = icerikler;
        this.dizi_idler = idler;

        layoutInflater = LayoutInflater.from(kisiler.getApplicationContext());
    }

    @Override
    public int getCount() {
        return dizi_idler.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View RowView = layoutInflater.inflate(R.layout.custom_arkadaslarim_liste,null);

        TextView txt_baslik = RowView.findViewById(R.id.custom_baslik);
        TextView txt_icerik = RowView.findViewById(R.id.custom_icerik);
        ImageView img = RowView.findViewById(R.id.custom_profil);

        txt_baslik.setText(dizi_basliklar[position]);
        txt_icerik.setText(dizi_icerikler[position]);



        return RowView;
    }
}
