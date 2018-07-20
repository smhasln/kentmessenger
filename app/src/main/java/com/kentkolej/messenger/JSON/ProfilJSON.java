package com.kentkolej.messenger.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ProfilJSON extends StringRequest {
    private static final String GIRIS_URL = "http://www.kentkoleji.com.tr/v2/json/chat/chat_profil.php";


    private Map<String, String> params;

    public ProfilJSON(String per_id, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("per_id", per_id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
