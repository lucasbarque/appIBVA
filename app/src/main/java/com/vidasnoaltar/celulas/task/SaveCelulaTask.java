package com.vidasnoaltar.celulas.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import com.vidasnoaltar.celulas.Activities.CelulaActivity;
import com.vidasnoaltar.celulas.Dados.Celula;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class SaveCelulaTask extends AsyncTask<String, Object, Long> {
    private final CelulaActivity activity;
    private final Celula celula;

    private static final String ID = "id";

    public SaveCelulaTask(CelulaActivity activity, Celula celula) {
        this.activity = activity;
        this.celula = celula;
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(celula, "celulas");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar a célula!", Toast.LENGTH_LONG).show();
        } else {
            DbHelper dao = new DbHelper(activity);
            dao.atualizarCelula(celula);
            dao.close();
        }
        activity.finish();
    }
}
