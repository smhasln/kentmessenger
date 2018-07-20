package com.kentkolej.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kentkolej.messenger.JSON.GirisJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Giris extends AppCompatActivity {

    String kadi;
    String sifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        final Button giris = findViewById(R.id.btn_giris_yap);
        final EditText txt_kadi = findViewById(R.id.txt_kadi);
        final EditText txt_sifre = findViewById(R.id.txt_sifre);


        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kadi = txt_kadi.getText().toString().trim();
                sifre = txt_sifre.getText().toString().trim();

                if (kadi.isEmpty() || sifre.isEmpty())
                {
                    Alerter.create(Giris.this)
                            .setTitle("Giriş Başarısız")
                            .setText("Kullanıcı adı veya şifre boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {

                    giris_yap();
                }

            }
        });

    }


    void giris_yap()
    {

        final ProgressDialog progress = ProgressDialog.show(Giris.this, "Oturum Açma", "Giriş Yapılıyor...", true);

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");

                    if (hata == 0)
                    {

                        progress.dismiss();

                        String cek_id = jsonresponse.getString("per_id");

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("kadi", kadi);
                        editor.putString("sifre", sifre);
                        editor.putString("k_id",cek_id);

                        editor.apply();

                        startActivity(new Intent(Giris.this, AnaMenu.class));

                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(Giris.this)
                                .setTitle("Giriş Başarısız")
                                .setText("Kullanıcı adı veya şifre hatalı!")
                                .setIcon(R.drawable.ic_cancel_black_24dp)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(android.R.color.holo_red_light)
                                .show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        GirisJSON loginrequest = new GirisJSON(kadi,sifre,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Giris.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }


}