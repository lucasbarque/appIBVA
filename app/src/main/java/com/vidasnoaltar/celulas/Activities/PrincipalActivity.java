package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAviso;
    private Button btnProgramacao;
    private Button btnAniversariante;
    private Button btnGe;
    private Button btnCelula;
    private Button btnUsuario;
    private TextView txtAviso;
    private TextView txtProgramacao;
    private TextView txtAniversariante;
    private TextView txtGe;
    private TextView txtCelula;
    private TextView txtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnAviso = (Button) findViewById(R.id.btnAviso);
        btnProgramacao = (Button) findViewById(R.id.btnProgramacao);
        btnAniversariante = (Button) findViewById(R.id.btnAniversariante);
        btnGe = (Button) findViewById(R.id.btnGe);
        btnCelula = (Button) findViewById(R.id.btnCelula);
        btnUsuario = (Button) findViewById(R.id.btnUsuario);

        txtAviso = (TextView) findViewById(R.id.txtAviso);
        txtProgramacao = (TextView) findViewById(R.id.txtProgramacao);
        txtAniversariante = (TextView) findViewById(R.id.txtAniversariante);
        txtGe = (TextView) findViewById(R.id.txtGe);
        txtCelula = (TextView) findViewById(R.id.txtCelula);
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);

        btnAviso.setOnClickListener(this);
        btnProgramacao.setOnClickListener(this);
        btnAniversariante.setOnClickListener(this);
        btnGe.setOnClickListener(this);
        btnCelula.setOnClickListener(this);
        btnUsuario.setOnClickListener(this);
        txtAviso.setOnClickListener(this);
        txtProgramacao.setOnClickListener(this);
        txtAniversariante.setOnClickListener(this);
        txtGe.setOnClickListener(this);
        txtCelula.setOnClickListener(this);
        txtUsuario.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnAviso || v == txtAviso) {
            Intent intent = new Intent(PrincipalActivity.this, AvisoActivity.class);
            startActivity(intent);
        } else if (v == btnProgramacao || v == txtProgramacao) {
            Intent iProgramacao = new Intent(PrincipalActivity.this, ProgramacaoActivity.class);
            startActivity(iProgramacao);
        } else if (v == btnAniversariante || v == txtAniversariante) {
            Intent intent = new Intent(PrincipalActivity.this, AniversariantesActivity.class);
            startActivity(intent);
        } else if (v == btnGe || v == txtGe) {
            Intent intent = new Intent(PrincipalActivity.this, GEActivity.class);
            startActivity(intent);
        } else if (v == btnCelula || v == txtCelula) {
            Intent intent = new Intent(PrincipalActivity.this, CelulaActivity.class);
            startActivity(intent);
        } else if (v == btnUsuario || v == txtUsuario) {
            Utils.showMessageConfirm(PrincipalActivity.this, "ATENÇÃO", "Deseja realmente sair?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                    DbHelper db = new DbHelper(PrincipalActivity.this);
                    db.alterar("DELETE FROM TB_LOGIN;");
                    db.close();
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}