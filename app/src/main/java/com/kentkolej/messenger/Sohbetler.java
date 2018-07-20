package com.kentkolej.messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kentkolej.messenger.JSON.KisiListesiJSON;
import com.kentkolej.messenger.JSON.SohbetlerJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sohbetler extends Fragment {

    ListView liste;

    private String dizi_id[];
    private String dizi_ad[];
    private String dizi_resim[];
    private String dizi_son[];
    private String dizi_tarih[];

    private String per_id;

    private Runnable runnable;
    private Handler tekrarla = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sohbetler, container, false);


        // OTOMATİK OTURUM AÇMA İŞLEMLERİ İÇİN DATA ÇEK.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        per_id = preferences.getString("k_id", "N/A");

        liste = rootView.findViewById(R.id.sohbet_listesi);

        tekrarla.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                doldur();

                tekrarla.postDelayed(runnable,12000);

            }
        }, 10000);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent git = new Intent(getActivity(),Mesajlar.class);
                git.putExtra("chat_id",dizi_id[position]);
                git.putExtra("chat_isim",dizi_ad[position]);
                startActivity(git);

            }
        });

        doldur();

        return rootView;
    }


    void doldur()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    Log.i("yaz",response);
                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray cek_id = jsonresponse.getJSONArray("chat_id");
                    JSONArray cek_isim = jsonresponse.getJSONArray("chat_ad");
                    JSONArray cek_son = jsonresponse.getJSONArray("chat_son");
                    JSONArray cek_tarih = jsonresponse.getJSONArray("chat_sontarih");
                    JSONArray cek_resim = jsonresponse.getJSONArray("chat_resim");

                    dizi_id = new String[cek_id.length()];
                    dizi_ad = new String[cek_isim.length()];
                    dizi_resim = new String[cek_resim.length()];
                    dizi_son = new String[cek_son.length()];
                    dizi_tarih = new String[cek_tarih.length()];

                    for (int i = 0; i < cek_id.length(); i++) {
                        dizi_id[i] = cek_id.get(i).toString();
                        dizi_ad[i] = cek_isim.get(i).toString();
                        dizi_resim[i] = cek_resim.get(i).toString();
                        dizi_son[i] = cek_son.get(i).toString();
                        dizi_tarih[i] = cek_tarih.get(i).toString();
                    }

                    liste.setAdapter(new CustomSohbetListeAdapter(Sohbetler.this,dizi_id,dizi_ad,dizi_resim,dizi_son,dizi_tarih));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        Log.i("yaz",per_id);

        SohbetlerJSON loginrequest = new SohbetlerJSON(per_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loginrequest);


    }
}
