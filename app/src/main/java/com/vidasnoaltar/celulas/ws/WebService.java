package com.vidasnoaltar.celulas.ws;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {

    public static final String BASE_URL = "http://www.vidasnoaltar.com/outros/sistema-celulas/api/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int TIMEOUT = 5000;


    private String sendRequest(URL url, String method, String jsonString) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Accept", "application/json");
        if (method.equalsIgnoreCase("GET")) {
            builder.get();
        } else if (method.equalsIgnoreCase("POST")) {
            builder.post(RequestBody.create(JSON, jsonString));
        } else {
            builder.delete();
        }
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.i(String.format("sendRequest(%s)", request.url().toString()), "Error closing InputStream");
        }

        return null;
    }

    public String save(Object objeto, String endPoint) {
        try {
            URL url = new URL(BASE_URL + endPoint);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return sendRequest(url, "POST", objectMapper.writeValueAsString(objeto));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String listAll(String endPoint) {
        try {
            URL url = new URL(BASE_URL + endPoint);
            return sendRequest(url, "GET", null);
        } catch (Exception e) {
            return null;
        }
    }

    public String listByCelula(String endPoint, int celulaId) {
        try {
            URL url = new URL(BASE_URL + endPoint + "?celula=" + celulaId);
            return sendRequest(url, "GET", null);
        } catch (Exception e) {
            return null;
        }
    }

    public String listaCelulaById(String endPoint, int celulaId) {
        try {
            URL url = new URL(BASE_URL + endPoint + "/" + celulaId);
            return sendRequest(url, "GET", null);
        } catch (Exception e) {
            return null;
        }
    }

    public String delete(int id, String endPoint) {
        try {
            URL url = new URL(BASE_URL + endPoint + "/" + id);
            return sendRequest(url, "DELETE", null);
        } catch (Exception e) {
            return null;
        }
    }
}
