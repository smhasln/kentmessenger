package com.kentkolej.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomKatilimcilarListeAdapter extends BaseAdapter {

    private String dizi_isimler[];
    private String dizi_kampusler[];
    private String dizi_resimler[];
    private String dizi_gorevler[];
    private String dizi_idler[];

    LayoutInflater layoutInflater = null;


    public CustomKatilimcilarListeAdapter(Katilimcilar katilimcilar, String idler[], String isimler[], String kampusler[], String gorevler[], String resimler[])
    {
        this.dizi_idler = idler;
        this.dizi_isimler = isimler;
        this.dizi_kampusler = kampusler;
        this.dizi_gorevler = gorevler;
        this.dizi_resimler = resimler;

        layoutInflater = LayoutInflater.from(katilimcilar.getApplicationContext());
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

        TextView txt_isim = RowView.findViewById(R.id.custom_baslik);
        TextView txt_icerik = RowView.findViewById(R.id.custom_icerik);
        ImageView img = RowView.findViewById(R.id.custom_profil);

        txt_isim.setText(dizi_isimler[position]);
        txt_icerik.setText(dizi_kampusler[position] + " - " + dizi_gorevler[position]);

        Picasso.get()
                .load("http://www.kentkoleji.com.tr/upload/personel/" + dizi_resimler[position])
                .resize(350,350)
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.mipmap.ic_launcher_round)
                .into(img);


        return RowView;
    }
}
