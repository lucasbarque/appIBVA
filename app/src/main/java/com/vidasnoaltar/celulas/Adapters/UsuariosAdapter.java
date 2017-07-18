package com.vidasnoaltar.celulas.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;

import org.w3c.dom.Text;

import java.util.List;

public class UsuariosAdapter extends BaseAdapter {

    private final List<Usuario> usuarios;
    private final Activity activity;

    public UsuariosAdapter(List<Usuario> usuarios, Activity activity) {
        this.usuarios = usuarios;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int i) {
        return usuarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater().inflate(R.layout.lista_usuarios, viewGroup, false);
        Usuario usuario = usuarios.get(i);

        TextView nome      = (TextView) view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView descricao = (TextView) view.findViewById(R.id.lista_curso_personalizada_descricao);

        nome.setText(usuario.getNome());
        descricao.setText(usuario.getSobrenome());
        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
