package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import com.vidasnoaltar.celulas.Activities.FormProgramacaoActivity;
import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.ws.WebService;

public class SaveProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final FormProgramacaoActivity activity;
    private final Programacao programacao;
    private ProgressDialog alert;

    public SaveProgramacaoTask(FormProgramacaoActivity activity, Programacao programacao) {
        this.activity = activity;
        this.programacao = programacao;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Salvando programação...");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(programacao, "programacoes");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao salvar a programação!", Toast.LENGTH_LONG).show();
        }
        alert.dismiss();
        activity.finish();
    }
}
