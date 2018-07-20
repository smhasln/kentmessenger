package com.kentkolej.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomMesajListeAdapter extends BaseAdapter {

    private ArrayList<String> dizi_isimler;
    private ArrayList<String> dizi_resim_kontrol;
    private ArrayList<String> dizi_mesajlar;
    private ArrayList<String> dizi_durumlar;
    private ArrayList<String> dizi_tarihler;
    private ArrayList<String> dizi_profil;

    LayoutInflater layoutInflater = null;


    public CustomMesajListeAdapter(Mesajlar mesajlar, ArrayList<String> mesaj, ArrayList<String> durumlar, ArrayList<String> isimler, ArrayList<String> resim_kontrol,  ArrayList<String> tarih,ArrayList<String> profiller)
    {
        this.dizi_isimler = isimler;
        this.dizi_resim_kontrol = resim_kontrol;
        this.dizi_mesajlar = mesaj;
        this.dizi_durumlar = durumlar;
        this.dizi_tarihler = tarih;
        this.dizi_profil = profiller;

        //notifyDataSetChanged();
        layoutInflater = LayoutInflater.from(mesajlar.getApplicationContext());
    }

    @Override
    public int getCount() {
        return dizi_mesajlar.size();
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
        View RowView = layoutInflater.inflate(R.layout.custom_mesaj_balon,null);

        TextView txt_gelen = RowView.findViewById(R.id.txt_gelen);
        TextView txt_giden = RowView.findViewById(R.id.txt_giden);

        TextView txt_gelen_isim = RowView.findViewById(R.id.gelen_isim);
        TextView txt_giden_isim = RowView.findViewById(R.id.giden_isim);

        TextView txt_gelen_zaman = RowView.findViewById(R.id.gelen_zaman);
        TextView txt_giden_zaman = RowView.findViewById(R.id.giden_zaman);

        ImageView img_gelen = RowView.findViewById(R.id.img_gelen);
        ImageView img_giden = RowView.findViewById(R.id.img_giden);

        ImageView img_gelen_resim = RowView.findViewById(R.id.img_gelen_resim);
        ImageView img_giden_resim = RowView.findViewById(R.id.img_giden_resim);

        //ONDAN BANA YAZI
        if (dizi_durumlar.get(position).equals("1") && dizi_resim_kontrol.get(position).equals("0"))
        {
            txt_gelen.setText(dizi_mesajlar.get(position));
            txt_gelen_isim.setText(dizi_isimler.get(position));

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/upload/personel/"+dizi_profil.get(position))
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_gelen);

            txt_gelen_zaman.setText(dizi_tarihler.get(position));

            txt_giden.setVisibility(View.GONE);
            img_giden.setVisibility(View.GONE);

            txt_giden_isim.setVisibility(View.GONE);
            txt_giden_zaman.setVisibility(View.GONE);

            img_giden_resim.setVisibility(View.GONE);
            img_gelen_resim.setVisibility(View.GONE);

        }

        //BENDEN ONA YAZI
        else if (dizi_durumlar.get(position).equals("0") && dizi_resim_kontrol.get(position).equals("0"))
        {
            txt_giden.setText(dizi_mesajlar.get(position));
            txt_giden_isim.setText(dizi_isimler.get(position));

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/upload/personel/"+dizi_profil.get(position))
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_giden);

            txt_giden_zaman.setText(dizi_tarihler.get(position));

            txt_gelen.setVisibility(View.GONE);
            img_gelen.setVisibility(View.GONE);

            txt_gelen_isim.setVisibility(View.GONE);
            txt_gelen_zaman.setVisibility(View.GONE);

            img_giden_resim.setVisibility(View.GONE);
            img_gelen_resim.setVisibility(View.GONE);
        }

        //ONDAN BANA RESİM
        else if (dizi_durumlar.get(position).equals("1") && dizi_resim_kontrol.get(position).equals("1"))
        {
            txt_gelen_isim.setText(dizi_isimler.get(position));
            txt_gelen_zaman.setText(dizi_tarihler.get(position));

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/upload/personel/"+dizi_profil.get(position))
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_gelen);

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/v2/json/chat/upload/"+dizi_mesajlar.get(position)+".jpg")
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_gelen_resim);

            img_gelen_resim.setVisibility(View.VISIBLE);

            txt_gelen.setVisibility(View.GONE);

            img_giden.setVisibility(View.GONE);
            txt_giden_isim.setVisibility(View.GONE);
            txt_giden_zaman.setVisibility(View.GONE);
            txt_giden.setVisibility(View.GONE);
            img_giden_resim.setVisibility(View.GONE);

        }

        //BENDEN ONA RESİM
        else if (dizi_durumlar.get(position).equals("0") && dizi_resim_kontrol.get(position).equals("1"))
        {
            txt_giden_isim.setText(dizi_isimler.get(position));
            img_giden_resim.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/upload/personel/"+dizi_profil.get(position))
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_giden);

            Picasso.get()
                    .load("http://www.kentkoleji.com.tr/v2/json/chat/upload/"+dizi_mesajlar.get(position)+".jpg")
                    .error(R.mipmap.ic_launcher_round)
                    .resize(250,250)
                    .into(img_giden_resim);

            txt_giden_zaman.setText(dizi_tarihler.get(position));

            txt_giden.setVisibility(View.GONE);
            img_gelen.setVisibility(View.GONE);
            txt_gelen_isim.setVisibility(View.GONE);
            txt_gelen_zaman.setVisibility(View.GONE);
            img_gelen_resim.setVisibility(View.GONE);
            txt_gelen.setVisibility(View.GONE);
        }
        return RowView;
    }
}
