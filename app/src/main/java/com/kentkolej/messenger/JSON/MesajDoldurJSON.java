package com.kentkolej.messenger.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class MesajDoldurJSON extends StringRequest {
    private static final String GIRIS_URL = "http://www.kentkoleji.com.tr/v2/json/chat/chat_mesaj_liste.php";


    private Map<String, String> params;

    public MesajDoldurJSON(String per_id, String chat_id, String count, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("per_id", per_id);
        params.put("chat_id", chat_id);
        params.put("count", count);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
