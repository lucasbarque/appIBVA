package com.vidasnoaltar.celulas.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.vidasnoaltar.celulas.Dados.Programacao;

public class ProgramacaoConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Programacao fromJson(JSONObject jsonObject) {
        Programacao obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Programacao.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Programacao> fromJson(JSONArray jsonArray) {
        List<Programacao> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Programacao obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
