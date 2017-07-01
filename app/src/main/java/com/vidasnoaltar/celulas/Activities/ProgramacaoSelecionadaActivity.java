package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.Utils;

public class ProgramacaoSelecionadaActivity extends AppCompatActivity {

    private TextView textview_data;
    private TextView textview_telefone;
    private TextView textview_horario;
    private TextView textview_valor;
    private TextView textview_mapa;
    private Toolbar toolbar;
    private DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao_selecionada);
        getIntent().getIntExtra("id_prog", 0);

        toolbar = (Toolbar) findViewById(R.id.th_programacao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textview_data = (TextView) findViewById(R.id.textview_data);
        textview_telefone = (TextView) findViewById(R.id.textview_telefone);
        textview_horario = (TextView) findViewById(R.id.textview_horario);
        textview_valor = (TextView) findViewById(R.id.textview_valor);
        textview_mapa = (TextView) findViewById(R.id.textview_mapa);

        db = new DbHelper(this);

        try {
            Programacao programacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE ID = " + getIntent().getIntExtra("id_prog", 0)).get(0);
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            String dataSaida = null;
            try {
                Date dataEntrada = formatoEntrada.parse(programacao.getData());
                dataSaida = formatoSaida.format(dataEntrada);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            toolbar.setTitle(programacao.getNome());
            textview_data.setText(dataSaida);
            textview_telefone.setText(programacao.getTelefone());
            textview_horario.setText(programacao.getHorario());
            textview_valor.setText(programacao.getValor());
            textview_mapa.setText(programacao.getLocal());
        } catch (CursorIndexOutOfBoundsException e) {
            Utils.mostraMensagemDialog(this, "Erro ao abrir programacao", "Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
