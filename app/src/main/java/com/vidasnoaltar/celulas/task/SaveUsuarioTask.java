package com.vidasnoaltar.celulas.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import com.vidasnoaltar.celulas.Activities.FormUsuarioActivity;
import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.ws.WebService;

public class SaveUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final FormUsuarioActivity activity;
    private final Usuario usuario;
    private ProgressDialog alert;

    public SaveUsuarioTask(FormUsuarioActivity activity, Usuario usuario) {
        this.activity = activity;
        this.usuario = usuario;
    }


    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Salvando membro...");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(usuario, "usuarios");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao salvar o membro!", Toast.LENGTH_LONG).show();
        }
        alert.dismiss();
        activity.finish();
    }
}
