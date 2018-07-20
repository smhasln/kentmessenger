package com.kentkolej.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomSohbetListeAdapter extends BaseAdapter {


    private String dizi_id[];
    private String dizi_ad[];
    private String dizi_resim[];
    private String dizi_son[];
    private String dizi_tarih[];

    LayoutInflater layoutInflater = null;


    public CustomSohbetListeAdapter(Sohbetler sohbetler, String idler[], String isimler[], String resimler[], String son[], String tarihler[])
    {
        this.dizi_id = idler;
        this.dizi_ad = isimler;
        this.dizi_resim = resimler;
        this.dizi_son = son;
        this.dizi_tarih = tarihler;

        layoutInflater = LayoutInflater.from(sohbetler.getContext());
    }

    @Override
    public int getCount() {
        return dizi_id.length;
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
        View RowView = layoutInflater.inflate(R.layout.custom_sohbet_liste,null);

        TextView txt_isim = RowView.findViewById(R.id.custom_sohbet_baslik);
        TextView txt_son = RowView.findViewById(R.id.custom_sohbet_icerik);
        TextView txt_tarih = RowView.findViewById(R.id.custom_sohbet_tarih);
        ImageView img = RowView.findViewById(R.id.custom_sohbet_img);

        txt_isim.setText(dizi_ad[position]);
        txt_son.setText(dizi_son[position]);
        txt_tarih.setText(dizi_tarih[position]);

        Picasso.get()
                .load("http://www.kentkoleji.com.tr/upload/personel/" + dizi_resim[position])
                .resize(350,350)
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .error(R.mipmap.ic_launcher_round)
                .into(img);


        return RowView;
    }
}
