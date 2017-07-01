package com.vidasnoaltar.celulas.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.vidasnoaltar.celulas.Dados.Aviso;

public class AvisoConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Aviso fromJson(JSONObject jsonObject) {
        Aviso aviso;
        try {
            aviso = mapper.readValue(jsonObject.toString(), Aviso.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return aviso;
    }

    public List<Aviso> fromJson(JSONArray jsonArray) {
        List<Aviso> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Aviso aviso = fromJson(objectJson);
            if (aviso != null) {
                list.add(aviso);
            }
        }
        return list;
    }
}
