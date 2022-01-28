package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.miguelstudiodeveloper.sqliteandroid.Execucoes;
import com.miguelstudiodeveloper.sqliteandroid.R;
import com.miguelstudiodeveloper.sqliteandroid.utils.Atributo;
import com.miguelstudiodeveloper.sqliteandroid.utils.BancoSQLite;
import com.miguelstudiodeveloper.sqliteandroid.utils.Conversor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_inserirDado extends Fragment {

    //variaveis
    private SQLiteDatabase banco;
    private ArrayList<String> justNames;
    private ArrayAdapter<String> adaptadorAtributos;
    private ArrayList<Atributo> atributosAtuais;
    private int indice_atributo_atual = 1;

    //UI components
    private Spinner spinner, spinner_atributo;
    private TextView testagem, textViewAtributo, nomeAtributo;
    private EditText valor_atributo;
    private ConstraintLayout layout;
    private Button change_atributo;


    public Fragment_inserirDado(SQLiteDatabase banco){
        this.banco = banco;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inserir_dado, container, false);

        //set UI components
        spinner = view.findViewById(R.id.spinner_tabelas);
        testagem = view.findViewById(R.id.testagem);
        spinner_atributo = view.findViewById(R.id.spinner_atributo);
        textViewAtributo = view.findViewById(R.id.textViewAtributo);
        layout = view.findViewById(R.id.layout_interno);
        nomeAtributo = view.findViewById(R.id.nome_atributo);
        valor_atributo = view.findViewById(R.id.valor_atributo);
        change_atributo = view.findViewById(R.id.button_change_atributo);

        change_atributo.setOnClickListener( new EscutadorChange());


        ArrayList<String> arrTblNames = BancoSQLite.getNomeTabelas(this.banco);
        //seta os nomes das sequencias
        configAdapter(arrTblNames);

        if (!arrTblNames.isEmpty()){
            atributosAtuais = BancoSQLite.getAtributos(banco, arrTblNames.get(0));
            setaCadastramento();
        }

        return view;
    }

    //atualiza os campos para preenchimento dos dados por parte do usuário
    private void setaCadastramento() {
        valor_atributo.setText("");
        nomeAtributo.setText(atributosAtuais.get(indice_atributo_atual).getNome().toUpperCase());
    }


    //configura o spinner com o nome das tabelas
    private void configAdapter(ArrayList<String> keys){

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                getContext(), // contexto da activity
                R.layout.linha_registros, // o desenho de item já pronto
                R.id.text1, // o ID do TextView dentro do item
                keys); // o ArrayList com as strings a serem exibidas
        // configurar a lista para utilizar este objeto adaptador
        spinner.setAdapter( adaptador );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msupplier = spinner.getSelectedItem().toString();
                //        SELECT sql FROM sqlite_master WHERE tbl_name = 'Pets';

                atributosAtuais = BancoSQLite.getAtributos(banco, msupplier);
                indice_atributo_atual = 1;
                setaCadastramento();
                if (atributosAtuais != null){
                    justNames = new ArrayList<>();
                    for(int u = 0; u<atributosAtuais.size(); u++){
                        justNames.add(atributosAtuais.get(u).getNome());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /*
    ==================================
        ESCUTADORES
    ==================================
     */

    private class EscutadorChange implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (atributosAtuais != null){
                if(indice_atributo_atual == atributosAtuais.size()-1){
                    //salvar o valor do ultimo atributo
                    String valor = valor_atributo.getText().toString();
                    atributosAtuais.get(indice_atributo_atual).setValor_temporario(valor);
                    //salvar no banco
                    String nome_tabela = spinner.getSelectedItem().toString();
                    BancoSQLite.insertElemento(banco, nome_tabela, atributosAtuais);
                    Execucoes tela = (Execucoes) getActivity();
                    tela.go_back();

                    return;
                }
                if (indice_atributo_atual + 1 == atributosAtuais.size()-1){
                    //penúltimo atributo
                    //setar o button para Salvar
                    change_atributo.setText("Salvar");
                }
                //salvar o valor no atributo o valor temporário
                String valor = valor_atributo.getText().toString();
                atributosAtuais.get(indice_atributo_atual).setValor_temporario(valor);

                //incrementar mais um no indice_atributo_atual e atualizar o valor dos bags
                indice_atributo_atual += 1;
                setaCadastramento();
            }
            //cadastrar();
        }
    }

}
