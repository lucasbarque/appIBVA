package com.vidasnoaltar.celulas.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.vidasnoaltar.celulas.Dados.GrupoEvangelistico;

public class GrupoEvangelisticoConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public GrupoEvangelistico fromJson(JSONObject jsonObject) {
        GrupoEvangelistico obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), GrupoEvangelistico.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<GrupoEvangelistico> fromJson(JSONArray jsonArray) {
        List<GrupoEvangelistico> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            GrupoEvangelistico obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
