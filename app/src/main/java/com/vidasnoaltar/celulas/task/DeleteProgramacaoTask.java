package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import com.vidasnoaltar.celulas.Activities.ProgramacaoActivity;
import com.vidasnoaltar.celulas.Adapters.ProgramacoesAdapter;
import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class DeleteProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final Programacao programacao;
    private ProgramacaoActivity activity;
    private ProgressDialog alert;
    private List<Programacao> listaProgramacao;
    private ListView listview_programacoes;
    private ImageView imageview_lista_vazia;

    public DeleteProgramacaoTask(Programacao programacao, ProgramacaoActivity activity) {
        this.programacao = programacao;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Excluindo programação...");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            request.delete(programacao.getId(), "programacoes");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        alert.dismiss();
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover a programação", Toast.LENGTH_LONG).show();
        } else {
            DbHelper db = new DbHelper(activity);
            try {
                listview_programacoes = (ListView) activity.findViewById(R.id.listview_programacoes);

                db.alterar("DELETE FROM TB_PROGRAMACOES WHERE ID = " + programacao.getId());
                listaProgramacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE PROGRAMACOES_CELULA_ID = " + Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));
                ProgramacoesAdapter adapter = new ProgramacoesAdapter(listaProgramacao, activity);
                listview_programacoes.setAdapter(adapter);
                Toast.makeText(activity, "Programação excluída com sucesso", Toast.LENGTH_LONG).show();

            } catch (CursorIndexOutOfBoundsException e) {
                System.out.println("Tabela programações vazia!");
                imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
                imageview_lista_vazia.setVisibility(View.VISIBLE);
            }
        }
    }
}
