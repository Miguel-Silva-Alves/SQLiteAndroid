package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.miguelstudiodeveloper.sqliteandroid.Execucoes;
import com.miguelstudiodeveloper.sqliteandroid.R;
import com.miguelstudiodeveloper.sqliteandroid.utils.BancoSQLite;

import java.util.ArrayList;
import java.util.Map;


public class Fragment_updateDado extends Fragment {

    //variaveis
    private SQLiteDatabase banco;
    private String tabela;
    private Map<String, String> atributos;
    private int indice_atributo_atual = 0;
    private ArrayList<String> keys_atributos = new ArrayList<>();

    //UI components
    private TextView nometabela, lblId, placeAtributo;
    private EditText editText;
    private Button btn_next;

    public Fragment_updateDado(SQLiteDatabase banco, String tabela, Map<String, String> atributos){
        this.banco = banco;
        this.atributos = atributos;
        this.tabela = tabela;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_dado, container, false);

        //set UI components
        nometabela = view.findViewById(R.id.upDados_nomeTabela);
        lblId = view.findViewById(R.id.upDados_id);
        placeAtributo = view.findViewById(R.id.upDados_atributo);
        btn_next = view.findViewById(R.id.upDados_button);
        editText = view.findViewById(R.id.upDados_edit);

        //set onclick
        btn_next.setOnClickListener( new EscutadorProximo() );

        nometabela.setText(tabela);
        lblId.setText(atributos.get("id"));
        for(String key : atributos.keySet()){
            if (!key.equals("id")){
                keys_atributos.add(key);
            }
        }

        setRecadastramento();

        return view;
    }

    //função que atualiza os dados que poderão ser atualizados
    private void setRecadastramento() {
        String chave = keys_atributos.get(indice_atributo_atual);
        String valor = atributos.get(chave);
        editText.setText(valor);
        placeAtributo.setText(chave);
    }

    //avança para os próximos atributos e também executa o update
    private class EscutadorProximo implements View.OnClickListener {
        @SuppressLint({"NewApi", "ResourceAsColor"})
        @Override
        public void onClick(View view) {
            String nome_tabela = nometabela.getText().toString();
            String valor = editText.getText().toString();
            atributos.replace(keys_atributos.get(indice_atributo_atual), valor);
            if (indice_atributo_atual == keys_atributos.size()-1){
                BancoSQLite.update(banco, nome_tabela, atributos);
                Toast.makeText(getContext(), "Alteração concluída!", Toast.LENGTH_SHORT).show();
                Execucoes execucoes = (Execucoes) getActivity();
                execucoes.go_back();
                return;
            }
            if (indice_atributo_atual + 1 == keys_atributos.size()-1){
                //penúltimo atributo
                //setar o button para Salvar
                btn_next.setText("Salvar");
                btn_next.setBackgroundColor(R.color.green);
            }
            indice_atributo_atual += 1;
            setRecadastramento();

        }
    }
}
