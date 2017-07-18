package com.vidasnoaltar.celulas.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProgramacoesAdapter extends BaseAdapter {

    private final List<Programacao> programacoes;
    private final Activity activity;

    public ProgramacoesAdapter(List<Programacao> programacoes, Activity activity) {
        this.programacoes = programacoes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return programacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return programacoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater().inflate(R.layout.lista_programacoes, viewGroup, false);
        Programacao programacao = programacoes.get(i);

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
        String dataSaida = null;
        try {
            Date dataEntrada = formatoEntrada.parse(programacao.getData());
            dataSaida = formatoSaida.format(dataEntrada);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView horario  = view.findViewById(R.id.horario);
        TextView data     = view.findViewById(R.id.data);
        TextView valor    = view.findViewById(R.id.valor);
        TextView nome     = view.findViewById(R.id.nome);
        TextView local    = view.findViewById(R.id.local);
        TextView telefone = view.findViewById(R.id.telefone);

        horario.setText(programacao.getHorario());
        data.setText(dataSaida);
        valor.setText("R$ " + programacao.getValor());
        nome.setText(programacao.getNome());
        local.setText(programacao.getLocal());
        telefone.setText(programacao.getTelefone());
        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
