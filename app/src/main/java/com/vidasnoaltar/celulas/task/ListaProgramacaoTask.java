package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import com.vidasnoaltar.celulas.Activities.ProgramacaoActivity;
import com.vidasnoaltar.celulas.Activities.ProgramacaoSelecionadaActivity;
import com.vidasnoaltar.celulas.Adapters.ProgramacoesAdapter;
import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.ProgramacaoConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class ListaProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final ProgramacaoActivity activity;
    private ProgressDialog alert;
    private int celulaId;
    private DbHelper db;
    private ListView listview_programacoes;
    private ImageView imageview_lista_vazia;

    public ListaProgramacaoTask(ProgramacaoActivity activity, int celulaId) {
        this.celulaId = celulaId;
        this.activity = activity;
        db = new DbHelper(activity);
        listview_programacoes = (ListView) activity.findViewById(R.id.listview_programacoes);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
    }

    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_PROGRAMACOES") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos buscando as programações...");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listByCelula("programacoes", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Programacao> programacoes = new ProgramacaoConverter().fromJson(jsonArray);
            if (programacoes != null && !programacoes.isEmpty()) {
                DbHelper dao = new DbHelper(activity);
                db.alterar("DELETE FROM TB_PROGRAMACOES;");
                for (int i = 0; i < programacoes.size(); i++) {
                    dao.atualizarProgramacao(programacoes.get(i));
                }
                dao.close();
            } else {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_PROGRAMACOES;");
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
                    System.out.println("Alert esta nulo");
                }
            }
        });
        try {
            int celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            final List<Programacao> listaProgramacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE PROGRAMACOES_CELULA_ID = " + celulaid);
//            ArrayAdapter<Programacao> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaProgramacao);
            ProgramacoesAdapter adapter = new ProgramacoesAdapter(listaProgramacao, activity);
            listview_programacoes.setAdapter(adapter);
            //Vamos chamar a outra tela apenas quando implementarmos a imagem da programação
            /*listview_programacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, ProgramacaoSelecionadaActivity.class);
                    intent.putExtra("id_prog", listaProgramacao.get(position).getId());
                    activity.startActivity(intent);
                }
            });*/
            if (listaProgramacao.size() > 0) {
                imageview_lista_vazia.setVisibility(View.GONE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            listview_programacoes.setAdapter(null); //Limpando Lista
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao obter as programações", Toast.LENGTH_LONG).show();
        }
    }
}
