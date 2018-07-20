package com.kentkolej.messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kentkolej.messenger.JSON.KatilimciListesiJSON;
import com.kentkolej.messenger.JSON.KisiListesiJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.dom.DOMLocator;

public class Katilimcilar extends AppCompatActivity {

    private ListView liste;

    private String dizi_isimler[];
    private String dizi_kampusler[];
    private String dizi_resimler[];
    private String dizi_gorevler[];
    private String dizi_idler[];

    private String per_id;
    private String chat_id;

    private TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katilimcilar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        per_id = preferences.getString("k_id", "N/A");

        Intent katilim_git = getIntent();
        chat_id = katilim_git.getStringExtra("chat_id");

        liste = findViewById(R.id.katilimci_listesi);
        txt_title = findViewById(R.id.txt_katilimci_title);

        doldur();
    }


    void doldur()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray cek_id = jsonresponse.getJSONArray("idler");
                    JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                    JSONArray cek_kampus = jsonresponse.getJSONArray("kampusler");
                    JSONArray cek_gorev = jsonresponse.getJSONArray("gorevler");
                    JSONArray cek_resim = jsonresponse.getJSONArray("resimler");

                    dizi_idler = new String[cek_id.length()];
                    dizi_isimler = new String[cek_isim.length()];
                    dizi_kampusler = new String[cek_kampus.length()];
                    dizi_gorevler = new String[cek_gorev.length()];
                    dizi_resimler = new String[cek_resim.length()];

                    for (int i = 0; i < dizi_idler.length; i++) {
                        dizi_idler[i] = cek_id.get(i).toString();
                        dizi_isimler[i] = cek_isim.get(i).toString();
                        dizi_kampusler[i] = cek_kampus.get(i).toString();
                        dizi_gorevler[i] = cek_gorev.get(i).toString();
                        dizi_resimler[i] = cek_resim.get(i).toString();
                    }

                    txt_title.setText("Katilimci Listesi (" + dizi_idler.length + ")");

                    liste.setAdapter(new CustomKatilimcilarListeAdapter(Katilimcilar.this,dizi_idler,dizi_isimler,dizi_kampusler,dizi_gorevler,dizi_resimler));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        KatilimciListesiJSON loginrequest = new KatilimciListesiJSON(per_id,chat_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginrequest);
    }
}
