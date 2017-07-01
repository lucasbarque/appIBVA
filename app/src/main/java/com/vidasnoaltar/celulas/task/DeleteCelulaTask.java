package com.vidasnoaltar.celulas.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import com.vidasnoaltar.celulas.Activities.LoginActivity;
import com.vidasnoaltar.celulas.Dados.Celula;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class DeleteCelulaTask extends AsyncTask<String, Object, Boolean> {
    private final Celula celula;
    private static final String QTDE = "qtde";
    private LoginActivity activity;

    public DeleteCelulaTask(Celula celula) {
        this.celula = celula;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(celula.getId(), "celulas");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(QTDE) > 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover o celula", Toast.LENGTH_LONG).show();
        }
        else {
            DbHelper dao = new DbHelper(activity);
            dao.alterar("DELETE FROM TB_CELULAS WHERE ID = " + celula.getId());
            dao.close();
        }
    }
}
