package com.kentkolej.messenger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kentkolej.messenger.JSON.GirisJSON;
import com.kentkolej.messenger.JSON.KisiListesiJSON;
import com.kentkolej.messenger.JSON.YeniKisiSohbet;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kisiler extends Fragment {

    ListView liste;

    private  ArrayList<String> dizi_isimler = new ArrayList<>();
    private  ArrayList<String> dizi_kampusler = new ArrayList<>();
    private  ArrayList<String> dizi_gorevler = new ArrayList<>();
    private  ArrayList<String> dizi_resimler = new ArrayList<>();
    private  ArrayList<String> dizi_idler = new ArrayList<>();
    private  ArrayList<String> dizi_chat_idler = new ArrayList<>();

    String aranan = "10";
    String per_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kisiler, container, false);

        // OTOMATİK OTURUM AÇMA İŞLEMLERİ İÇİN DATA ÇEK.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String kadi = preferences.getString("kadi", "N/A");
        String sifre = preferences.getString("sifre", "N/A");
        per_id = preferences.getString("k_id", "N/A");

        // DAHA ÖNCE OTURUM AÇMIŞ MI KONTROL ET.
        if (kadi.equals("N/A") || sifre.equals("N/A") || per_id.equals("N/A"))
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setTitle("Uyarı");
            builder1.setMessage("Kent Admin veya e-Kent uygulamasında kullandığınız bilgiler ile oturum açmalısınız.");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Tamam",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { startActivity(new Intent(getContext(),Giris.class));
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else
        {
           doldur();
        }
        final EditText kisi_ara = rootView.findViewById(R.id.kisi_ara_ilk);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        kisi_ara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (kisi_ara.getText().toString().trim().length() == 0)
                    {
                        aranan = "10";
                        doldur();
                    }else {
                        dizi_idler.clear();
                        dizi_isimler.clear();
                        dizi_kampusler.clear();
                        dizi_gorevler.clear();
                        dizi_resimler.clear();
                        dizi_chat_idler.clear();
                        aranan = kisi_ara.getText().toString();
                        doldur();
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        liste = rootView.findViewById(R.id.kisi_liste);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (dizi_chat_idler.get(position).equals("0"))
                {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle(dizi_isimler.get(position));
                    builder1.setMessage("ile konuşma başlatmak istediğinize emin misiniz?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Evet",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    konusma_olustur(dizi_idler.get(position), dizi_isimler.get(position));

                                }
                            });
                    builder1.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else
                {
                    Intent git = new Intent(getContext(),Mesajlar.class);
                    git.putExtra("chat_isim",dizi_isimler.get(position));
                    git.putExtra("chat_id",dizi_chat_idler.get(position));
                    startActivity(git);
                }
            }
        });


        return rootView;
    }


    void konusma_olustur(final String kimle, final String chat_isim)
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    String yeni_chat_id = jsonresponse.getString("chat_id");

                    doldur();

                    Intent git = new Intent(getContext(),Mesajlar.class);
                    git.putExtra("chat_isim",chat_isim);
                    git.putExtra("chat_id",yeni_chat_id);
                    startActivity(git);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        YeniKisiSohbet loginrequest = new YeniKisiSohbet(per_id,kimle,responselistener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loginrequest);
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

                    JSONArray cek_id = jsonresponse.getJSONArray("idler");
                    JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                    JSONArray cek_kampus = jsonresponse.getJSONArray("kampusler");
                    JSONArray cek_gorev = jsonresponse.getJSONArray("gorevler");
                    JSONArray cek_resim = jsonresponse.getJSONArray("resimler");
                    JSONArray cek_chat_id = jsonresponse.getJSONArray("chat_id");


                    for (int i = 0; i < cek_id.length(); i++) {

                        dizi_idler.add(cek_id.get(i).toString());
                        dizi_isimler.add(cek_isim.get(i).toString());
                        dizi_kampusler.add(cek_kampus.get(i).toString());
                        dizi_gorevler.add(cek_gorev.get(i).toString());
                        dizi_resimler.add(cek_resim.get(i).toString());
                        dizi_chat_idler.add(cek_chat_id.get(i).toString());
                    }

                    liste.setAdapter(new CustomListeAdapter(Kisiler.this,dizi_idler,dizi_isimler,dizi_kampusler,dizi_gorevler,dizi_resimler));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        KisiListesiJSON loginrequest = new KisiListesiJSON(per_id,aranan,responselistener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loginrequest);
    }
}