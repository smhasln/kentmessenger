package com.kentkolej.messenger;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kentkolej.messenger.JSON.GirisJSON;
import com.kentkolej.messenger.JSON.ProfilJSON;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

public class Profilim extends Fragment {

    private String per_id;

    private TextView txt_isim;
    private TextView txt_gorev;
    private TextView txt_kadi;
    private TextView txt_sifre;

    ImageView resim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profilim, container, false);

        // OTOMATİK OTURUM AÇMA İŞLEMLERİ İÇİN DATA ÇEK.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        per_id = preferences.getString("k_id", "N/A");

        txt_isim = rootView.findViewById(R.id.txt_profil_isim);
        txt_gorev = rootView.findViewById(R.id.txt_profil_gorev);

        resim = rootView.findViewById(R.id.img_profil_resim);

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    String cek_adsoyad = jsonresponse.getString("adsoyad");
                    String cek_gorev = jsonresponse.getString("gorev");
                    String cek_resim = jsonresponse.getString("resim");

                    txt_isim.setText(cek_adsoyad);
                    txt_gorev.setText(cek_gorev);

                    Picasso.get()
                            .load("http://www.kentkoleji.com.tr/upload/personel/" + cek_resim)
                            .resize(350,350)
                            .placeholder(R.drawable.ic_file_download_black_24dp)
                            .error(R.mipmap.ic_launcher_round)
                            .into(resim);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        ProfilJSON loginrequest = new ProfilJSON(per_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loginrequest);

        return rootView;
    }
}
