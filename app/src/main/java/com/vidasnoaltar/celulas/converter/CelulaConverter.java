package com.vidasnoaltar.celulas.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.vidasnoaltar.celulas.Dados.Celula;

public class CelulaConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Celula fromJson(JSONObject jsonObject) {
        Celula obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Celula.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Celula> fromJson(JSONArray jsonArray) {
        List<Celula> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Celula obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
