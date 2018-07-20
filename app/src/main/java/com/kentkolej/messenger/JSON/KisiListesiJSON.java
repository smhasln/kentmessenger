package com.kentkolej.messenger.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class KisiListesiJSON extends StringRequest {
    private static final String GIRIS_URL = "http://www.kentkoleji.com.tr/v2/json/chat/chat_kisi_liste.php";


    private Map<String, String> params;

    public KisiListesiJSON(String per_id,String aranan ,Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("per_id", per_id);
        params.put("aranan", aranan);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
