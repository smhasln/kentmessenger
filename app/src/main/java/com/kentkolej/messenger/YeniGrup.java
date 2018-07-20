package com.kentkolej.messenger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kentkolej.messenger.JSON.GirisJSON;
import com.kentkolej.messenger.JSON.GrupOlusturJSON;
import com.kentkolej.messenger.JSON.KisiAraJSON;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class YeniGrup extends AppCompatActivity {

    String per_id;
    String gidecek_idler = "";

    private ListView liste;

    private ArrayList<String> ust_resimler = new ArrayList<>();

    private  ArrayList<String> dizi_isimler = new ArrayList<>();
    private  ArrayList<String> dizi_kampusler = new ArrayList<>();
    private  ArrayList<String> dizi_gorevler = new ArrayList<>();
    private  ArrayList<String> dizi_resimler = new ArrayList<>();
    private  ArrayList<String> dizi_idler = new ArrayList<>();
    private  ArrayList<String> dizi_chat_idler = new ArrayList<>();

    private String aranan = "10";

    private RecyclerView eklenen_liste;
    private EditText txt_adi;

    CustomGrupListeAdapter grupListeAdapter;
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_grup);

        // OTOMATİK OTURUM AÇMA İŞLEMLERİ İÇİN DATA ÇEK.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        per_id = preferences.getString("k_id", "N/A");

        txt_adi = findViewById(R.id.txt_grup_konu);
        eklenen_liste = findViewById(R.id.eklenenler_liste);
        liste = findViewById(R.id.yeni_grup_liste);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        doldur();

        final EditText txt_ara = findViewById(R.id.txt_kisi_ara);
        final TextView txt_sayi = findViewById(R.id.txt_grup_sayi);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_grup_tamam);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ust_resimler.size() == 2 || txt_adi.getText().toString().trim().length() < 3)
                {
                    new MaterialStyledDialog.Builder(YeniGrup.this)
                            .setDescription("Grup adı minimum 3 karakter, grup katılımcıları minimum 3 kişi olmalı. ")
                            .setHeaderDrawable(R.drawable.arkaplan)
                            .withDialogAnimation(true)
                            .setCancelable(true)
                            .setNegativeText("Anladım")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            })
                            .show();
                }
                else
                {
                    new MaterialStyledDialog.Builder(YeniGrup.this)
                            .setDescription("Grubu oluşturmak istediğine emin misin?")
                            .setHeaderDrawable(R.drawable.arkaplan)
                            .withDialogAnimation(true)
                            .setPositiveText("Evet")
                            .setCancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    bitir();
                                }
                            })
                            .setNegativeText("Vazgeç")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                }
                            })
                            .show();
                }


            }
        });

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(YeniGrup.this);
                builder1.setTitle(dizi_isimler.get(position));
                builder1.setMessage("Seçilen kişi grup listesine eklenecek");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ProgressDialog progress = ProgressDialog.show(YeniGrup.this, "İşleniyor", "Lütfen bekleyin.", true);

                                gidecek_idler = gidecek_idler + dizi_idler.get(position) + ",";
                                Log.i("yaz",gidecek_idler);

                                ust_resimler.add("http://www.kentkoleji.com.tr/upload/personel/" + dizi_resimler.get(position));

                                adapter = new MyRecyclerViewAdapter(YeniGrup.this, ust_resimler);
                                eklenen_liste.setAdapter(adapter);
                                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(YeniGrup.this, LinearLayoutManager.HORIZONTAL, false);
                                eklenen_liste.setLayoutManager(horizontalLayoutManagaer);
                                adapter.notifyDataSetChanged();

                                dizi_idler.remove(position);
                                dizi_isimler.remove(position);
                                dizi_kampusler.remove(position);
                                dizi_gorevler.remove(position);
                                dizi_resimler.remove(position);
                                dizi_chat_idler.remove(position);

                                grupListeAdapter.notifyDataSetChanged();

                                txt_sayi.setText("Eklenenler (" + ust_resimler.size() + ")");

                                progress.dismiss();

                                aranan = "";
                                txt_ara.setText("");


                            }
                        });
                builder1.setNegativeButton("Vazgeç",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });


        txt_ara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt_ara.getText().toString().isEmpty())
                {

                }
                else
                {

                    dizi_idler.clear();
                    dizi_isimler.clear();
                    dizi_kampusler.clear();
                    dizi_gorevler.clear();
                    dizi_resimler.clear();
                    dizi_chat_idler.clear();
                    aranan = txt_ara.getText().toString();

                    doldur();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void bitir()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer cek_hata = jsonresponse.getInt("hata");

                    Log.i("yaz",response);
                    if (cek_hata == 0)
                    {
                        new MaterialStyledDialog.Builder(YeniGrup.this)
                            .setDescription("Grup oluşturuldu!")
                            .setHeaderDrawable(R.drawable.arkaplan)
                            .withDialogAnimation(true)
                            .setPositiveText("Tamam")
                            .setCancelable(false)
                            .show();

                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        GrupOlusturJSON loginrequest = new GrupOlusturJSON(txt_adi.getText().toString().trim(),gidecek_idler,per_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(YeniGrup.this);
        queue.add(loginrequest);
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
                    JSONArray cek_chat_id = jsonresponse.getJSONArray("chat_id");

                    for (int i = 0; i < cek_id.length(); i++) {
                        dizi_idler.add(cek_id.get(i).toString());
                        dizi_isimler.add(cek_isim.get(i).toString());
                        dizi_kampusler.add(cek_kampus.get(i).toString());
                        dizi_gorevler.add(cek_gorev.get(i).toString());
                        dizi_resimler.add(cek_resim.get(i).toString());
                        dizi_chat_idler.add(cek_chat_id.get(i).toString());
                    }

                    grupListeAdapter = new CustomGrupListeAdapter(YeniGrup.this,dizi_idler,dizi_isimler,dizi_kampusler,dizi_gorevler,dizi_resimler);
                    liste.setAdapter(grupListeAdapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        KisiAraJSON loginrequest = new KisiAraJSON(aranan,per_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(YeniGrup.this);
        queue.add(loginrequest);

    }
}
