package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import com.vidasnoaltar.celulas.Activities.CelulaActivity;
import com.vidasnoaltar.celulas.Dados.Celula;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.CelulaConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class ListaCelulaTask extends AsyncTask<String, Object, Boolean> {
    private final CelulaActivity activity;
    private int celulaId;
    private ProgressDialog alert;
    private DbHelper db;
    private TextView nome;
    private TextView lider;
    private TextView dia;
    private TextView horario;
    private TextView local;
    private TextView semana;
    private TextView versiculo;

    public ListaCelulaTask(CelulaActivity activity, int celulaId) {
        this.activity = activity;
        this.celulaId = celulaId;
        db = new DbHelper(activity);
        nome = (TextView) activity.findViewById(R.id.nome);
        lider = (TextView) activity.findViewById(R.id.lider);
        dia = (TextView) activity.findViewById(R.id.dia);
        horario = (TextView) activity.findViewById(R.id.horario);
        local = (TextView) activity.findViewById(R.id.local);
        semana = (TextView) activity.findViewById(R.id.semana);
        versiculo = (TextView) activity.findViewById(R.id.versiculo);
    }

    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_CELULAS WHERE ID =" + celulaId) <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando os dados!");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listaCelulaById("celulas", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Celula> celulas = new CelulaConverter().fromJson(jsonArray);
            if (celulas != null && !celulas.isEmpty()) {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_CELULAS");
                for (int i = 0; i < celulas.size(); i++) {
                    db.atualizarCelula(celulas.get(i));
                }
                db.close();
            } else {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_CELULAS");
                System.out.println("O objeto acabou ficando vazio!");
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    alert.dismiss();
                } catch (NullPointerException e) {
                    System.out.println("Alert está nulo");
                }
            }
        });
        try {
            int celulaid = Integer.parseInt(db.consulta("SELECT * FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            Celula celula = db.listaCelula("SELECT * FROM TB_CELULAS WHERE ID =" + celulaid).get(0);

            nome.setText(celula.getNome());
            lider.setText(celula.getLider());
            dia.setText(celula.converteDiaCelula());
            horario.setText(celula.getHorario());
            local.setText(celula.getLocal());
            semana.setText(celula.converteDiaJejum() + " - " + celula.getPeriodo());
            versiculo.setText("\"" + celula.getVersiculo() + "\"");
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Sem célula para listar");
        }
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro de conexão", Toast.LENGTH_LONG).show();
        }
    }
}
