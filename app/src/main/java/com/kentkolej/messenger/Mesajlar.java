package com.kentkolej.messenger;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kentkolej.messenger.JSON.BildirimGonderJSON;
import com.kentkolej.messenger.JSON.MesajDoldurJSON;
import com.kentkolej.messenger.JSON.MesajGonderJSON;
import com.kentkolej.messenger.JSON.SohbetlerJSON;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;

public class Mesajlar extends AppCompatActivity {

    private ArrayList<Integer> emoji = new ArrayList<>();

    private ArrayList<String> dizi_isimler;
    private ArrayList<String> dizi_resim_kontrol;
    private ArrayList<String> dizi_mesajlar;
    private ArrayList<String> dizi_durumlar;
    private ArrayList<String> dizi_tarihler;
    private ArrayList<String> dizi_profiller;

    private RecyclerView recyclerView;
    private ListView liste;

    private EmojiAdapter emojiAdapter;
    private CustomMesajListeAdapter adapter;

    private String chat_isim;
    private String chat_id;
    private String per_id;

    private Runnable runnable;
    private Handler tekrarla = new Handler();

    String resim_mi = "0";
    String giden_mesaj;

    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajlar);

        // OTOMATİK OTURUM AÇMA İŞLEMLERİ İÇİN DATA ÇEK.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        per_id = preferences.getString("k_id", "N/A");

        Intent git = getIntent();
        chat_id = git.getStringExtra("chat_id");
        chat_isim = git.getStringExtra("chat_isim");

        dizi_isimler = new ArrayList<>();
        dizi_resim_kontrol = new ArrayList<>();
        dizi_mesajlar = new ArrayList<>();
        dizi_durumlar = new ArrayList<>();
        dizi_tarihler = new ArrayList<>();
        dizi_profiller = new ArrayList<>();

        recyclerView = findViewById(R.id.emojiler);
        liste = findViewById(R.id.mesaj_liste);

        final FloatingActionButton foto_sec = findViewById(R.id.btn_foto_gonder);
        final FloatingActionButton gonder = findViewById(R.id.btn_msj_gonder);
        final EditText txt_mesaj = findViewById(R.id.txt_ilet_mesaj);
        TextView txt_title = findViewById(R.id.txt_mesajlar_title);
        ImageButton katilimcilar = findViewById(R.id.img_katilimcilar);
        txt_title.setText(chat_isim + " ile");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mesaj_olustur();

        emoji.add(R.drawable.emoji1);
        emoji.add(R.drawable.emoji2);
        emoji.add(R.drawable.emoji3);
        emoji.add(R.drawable.emoji4);
        emoji.add(R.drawable.emoji5);
        emoji.add(R.drawable.emoji6);
        emoji.add(R.drawable.emoji7);
        emoji.add(R.drawable.emoji8);
        emoji.add(R.drawable.emoji9);
        emoji.add(R.drawable.emoji10);
        emoji.add(R.drawable.emoji11);
        emoji.add(R.drawable.emoji12);
        emoji.add(R.drawable.emoji13);
        emoji.add(R.drawable.emoji14);
        emoji.add(R.drawable.emoji15);
        emoji.add(R.drawable.emoji16);
        emoji.add(R.drawable.emoji17);
        emoji.add(R.drawable.emoji18);
        emoji.add(R.drawable.emoji19);
        emoji.add(R.drawable.emoji20);
        emoji.add(R.drawable.emoji21);
        emoji.add(R.drawable.emoji23);
        emoji.add(R.drawable.emoji24);
        emoji.add(R.drawable.emoji25);
        emoji.add(R.drawable.emoji26);
        emoji.add(R.drawable.emoji27);
        emoji.add(R.drawable.emoji28);
        emoji.add(R.drawable.emoji29);
        emoji.add(R.drawable.emoji30);
        emoji.add(R.drawable.emoji31);
        emoji.add(R.drawable.emoji32);
        emoji.add(R.drawable.emoji33);
        emoji.add(R.drawable.emoji34);
        emoji.add(R.drawable.emoji35);
        emoji.add(R.drawable.emoji36);
        emoji.add(R.drawable.emoji37);

        emojiAdapter = new EmojiAdapter(Mesajlar.this, emoji);
        recyclerView.setAdapter(emojiAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(Mesajlar.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);


        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dizi_resim_kontrol.get(position).equals("1"))
                {
                    Intent zoom = new Intent(Mesajlar.this,FotoZoom.class);
                    zoom.putExtra("foto_url",dizi_mesajlar.get(position).toString());
                    startActivity(zoom);
                }
            }
        });

        katilimcilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent katilim_git = new Intent(Mesajlar.this,Katilimcilar.class);
                katilim_git.putExtra("chat_id",chat_id);
                startActivity(katilim_git);
            }
        });

        foto_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Mesajlar.this);
                builder1.setTitle("Fotoğraf Gönder");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Galeri",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showFileChooser();
                            }
                        });
                builder1.setNegativeButton("Kamera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RxImagePicker.with(Mesajlar.this).requestImage(Sources.CAMERA).subscribe(new Consumer<Uri>() {
                                    @Override
                                    public void accept(@NonNull Uri uri) throws Exception {

                                        final InputStream imageStream = getContentResolver().openInputStream(uri);
                                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                        Bitmap btmp = Bitmap.createScaledBitmap(selectedImage, 750, 750, false);
                                        giden_mesaj = encodeImage(btmp);

                                        resim_mi = "1";

                                        sor(btmp);

                                    }
                                });
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        gonder.setVisibility(View.INVISIBLE);
        txt_mesaj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt_mesaj.getText().length() > 0)
                {
                    gonder.setVisibility(View.VISIBLE);
                }
                else
                {
                    gonder.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tekrarla.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                mesaj_olustur();

                tekrarla.postDelayed(runnable,6000);

            }
        }, 5000);

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                giden_mesaj = txt_mesaj.getText().toString().trim();

                mesaj_gonder();

                txt_mesaj.setText("");

            }});
    }

    void sor(Bitmap bitmap)
    {
        new MaterialStyledDialog.Builder(this)
                .setDescription("Resmi göndermek istediğine emin misin?")
                .setHeaderDrawable(new BitmapDrawable(getResources(),bitmap))
                .withDialogAnimation(true)
                .setPositiveText("Gönder")
                .setCancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        resim_mi = "1";
                        mesaj_gonder();
                    }
                })
                .setNegativeText("Vazgeç")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        resim_mi = "0";
                        giden_mesaj = "";
                    }
                })
                .show();
    }

    void bildirim_tetikle()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonresponse = new JSONObject(response);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
        BildirimGonderJSON loginrequest = new BildirimGonderJSON(per_id,chat_id,giden_mesaj,resim_mi,responselistener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginrequest);
    }

    void mesaj_gonder()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer cek_hata = jsonresponse.getInt("hata");
                    if (cek_hata == 0)
                    {
                        bildirim_tetikle();
                        mesaj_olustur();
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Log.i("yaz",giden_mesaj);
        MesajGonderJSON loginrequest = new MesajGonderJSON(per_id,chat_id,giden_mesaj,resim_mi,responselistener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginrequest);
    }

    private String encodeImage(Bitmap bm)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,80,baos);

        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView

                Bitmap btmp = Bitmap.createScaledBitmap(bitmap, 750, 750, false);
                giden_mesaj = encodeImage(btmp);

                resim_mi = "1";
                sor(btmp);

                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void mesaj_olustur()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonresponse = new JSONObject(response);
                    Integer cek_hata = jsonresponse.getInt("hata");

                    Log.i("yaz",response);
                    if (cek_hata == 0){
                        JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                        JSONArray cek_mesaj = jsonresponse.getJSONArray("mesajlar");
                        JSONArray cek_durum = jsonresponse.getJSONArray("durumlar");
                        JSONArray cek_resim_kontrol = jsonresponse.getJSONArray("resim_kontrol");
                        JSONArray cek_tarih = jsonresponse.getJSONArray("tarih");
                        JSONArray cek_profil = jsonresponse.getJSONArray("profil");

                        for (int i = 0; i < cek_durum.length(); i++) {

                            dizi_isimler.add(cek_isim.get(i).toString());
                            dizi_mesajlar.add(cek_mesaj.get(i).toString());
                            dizi_durumlar.add(cek_durum.get(i).toString());
                            dizi_resim_kontrol.add(cek_resim_kontrol.get(i).toString());
                            dizi_tarihler.add(cek_tarih.get(i).toString());
                            dizi_profiller.add(cek_profil.get(i).toString());
                        }

                        adapter = new CustomMesajListeAdapter(Mesajlar.this,dizi_mesajlar,dizi_durumlar,dizi_isimler,dizi_resim_kontrol,dizi_tarihler,dizi_profiller);
                        liste.setAdapter(adapter);

                        liste.post(new Runnable(){
                            public void run() {
                                liste.setSelection(liste.getCount() - 1);}});

                        resim_mi = "0";
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
        MesajDoldurJSON loginrequest = new MesajDoldurJSON(per_id,chat_id,String.valueOf(dizi_mesajlar.size()),responselistener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        tekrarla.removeCallbacks(runnable);

    }
}
