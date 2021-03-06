package com.kentkolej.messenger.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class YeniKisiSohbet extends StringRequest {
    private static final String GIRIS_URL = "http://www.kentkoleji.com.tr/v2/json/chat/chat_konusma_olustur.php";


    private Map<String, String> params;

    public YeniKisiSohbet(String per_id, String kimle, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("per_id", per_id);
        params.put("kim_id", kimle);
        params.put("tur", "0");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
