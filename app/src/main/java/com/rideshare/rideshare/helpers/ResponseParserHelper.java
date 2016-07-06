package com.rideshare.rideshare.helpers;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ResponseParserHelper {

    public static String TIMEOUT_ERROR = "Erro de tempo de resposta";
    public static String NO_CONNECTION_ERROR = "Falha na conexão com o servidor";
    public static String CONNECTION_ERROR = "Problema de conexão";

    public static void handleError(VolleyError error, Context context) {
        if (error instanceof TimeoutError) {
            Toast.makeText(context, TIMEOUT_ERROR, Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(context, NO_CONNECTION_ERROR, Toast.LENGTH_LONG).show();
        } else {
            NetworkResponse networkResponse = error.networkResponse;
            String stringError = new String(networkResponse.data);

            parseErrorJSON(stringError, context);
        }
    }

    public static void parseErrorJSON(String stringError, Context context) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> errorMessage = null;
        try {
            errorMessage = mapper.readValue(stringError, new TypeReference<Map<String,String>>() { });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.get("error")))
            Toast.makeText(context, errorMessage.get("error"), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, CONNECTION_ERROR, Toast.LENGTH_LONG).show();
    }

}
