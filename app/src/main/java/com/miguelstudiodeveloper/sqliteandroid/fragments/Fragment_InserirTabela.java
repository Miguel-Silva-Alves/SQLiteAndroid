package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miguelstudiodeveloper.sqliteandroid.Execucoes;
import com.miguelstudiodeveloper.sqliteandroid.R;
import com.miguelstudiodeveloper.sqliteandroid.utils.Atributo;
import com.miguelstudiodeveloper.sqliteandroid.utils.BancoSQLite;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

public class Fragment_InserirTabela extends Fragment {

    //UI components
    private EditText nome_tabela, nome_coluna, tipo_coluna;
    private Button btn_cadastrar;
    private ImageButton btn_mais_coluna;

    //variaveis
    private ArrayList<Atributo> atributos = new ArrayList<>();
    private SQLiteDatabase bd;

    public Fragment_InserirTabela(SQLiteDatabase banco){
        this.bd = banco;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inserir_tabela, container, false);

        //set UI components
        nome_tabela = view.findViewById(R.id.edit_nome_tabela);
        nome_coluna = view.findViewById(R.id.edit_nome_coluna);
        tipo_coluna = view.findViewById(R.id.edit_tipo);
        btn_cadastrar = view.findViewById(R.id.btn_cadastrar);
        btn_mais_coluna = view.findViewById(R.id.btn_mais);

        //set onclicklisteners
        btn_mais_coluna.setOnClickListener( new EscutadorMaisColuna());
        btn_cadastrar.setOnClickListener( new EscutadorCadastrar());

        return view;
    }

    /*
    ===================================================================
                            ESCUTADORES
    ===================================================================
     */

    private class EscutadorMaisColuna implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String sColuna = nome_coluna.getText().toString();
            String sTipo= tipo_coluna.getText().toString();
            if (sColuna.isEmpty() || sTipo.isEmpty()){
                Toast.makeText(getContext(), "Preencha todos os campos antes de cadastar!", Toast.LENGTH_SHORT).show();
                return;
            }
            Atributo novo = new Atributo(sColuna, sTipo.toUpperCase());
            atributos.add(novo);
            nome_coluna.setText("");
            tipo_coluna.setText("");
            btn_cadastrar.setEnabled(true);
            Toast.makeText(getContext(), "Atributo inserido com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    private class EscutadorCadastrar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String sTabela = nome_tabela.getText().toString();
            if (sTabela.isEmpty() || atributos.isEmpty()){
                Toast.makeText(getContext(), "Preecha todos os campos e cadastre um atributo!", Toast.LENGTH_SHORT).show();
                return;
            }
            BancoSQLite.createTable(bd, sTabela, atributos);
            Toast.makeText(getContext(), "Tabela Criada!", Toast.LENGTH_SHORT).show();
            Execucoes execucoes = (Execucoes) getActivity();
            execucoes.go_back();
        }
    }
}
