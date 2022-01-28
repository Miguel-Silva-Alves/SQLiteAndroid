package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miguelstudiodeveloper.sqliteandroid.R;
import com.miguelstudiodeveloper.sqliteandroid.utils.Atributo;
import com.miguelstudiodeveloper.sqliteandroid.utils.BancoSQLite;

import java.util.ArrayList;
import java.util.Map;

public class Fragment_selectAll extends Fragment {

    //variaveis
    private SQLiteDatabase banco;

    //UI Components
    private Spinner spinner;
    private ScrollView scrollView;

    public Fragment_selectAll(SQLiteDatabase banco){
        this.banco = banco;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_all, container, false);

        //set UI components
        spinner = view.findViewById(R.id.select_all_spinner);
        scrollView = view.findViewById(R.id.scroll);

        ArrayList<String> arrTblNames = BancoSQLite.getNomeTabelas(this.banco);
        configAdapterSpinner(arrTblNames);

        if (!arrTblNames.isEmpty()){
            setaLayout(arrTblNames.get(0));
        }

        return view;
    }

    //insere um elemento no scrollview
    public void populaScroll(String texto){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.layout_linha, null, false);

        TextView textView = rowView.findViewById(R.id.lblTextoInserir);
        textView.setText(texto);

        LinearLayout layout = scrollView.findViewById(R.id.select_layout_vertical);
        layout.addView(rowView);
    }

    //consulta -> Select all de uma tabela
    public void setaLayout(String nome_tabela){
        ArrayList<Atributo> atributos = BancoSQLite.getAtributos(banco, nome_tabela);
        LinearLayout layout = scrollView.findViewById(R.id.select_layout_vertical);
        layout.removeAllViews();
        if (atributos != null){
            try {
                //ArrayList<ArrayList<Atributo>> dados_salvos = BancoSQLite.getDadosFromTabela(this.banco, nome_tabela, atributos);
                Map<String, Map<String, String>> mapDados = BancoSQLite.getDadosFromTabelaMap(this.banco, nome_tabela, atributos);
                for (String keyMaior : mapDados.keySet()){
                    StringBuilder builder = new StringBuilder("id-");
                    builder.append(keyMaior);
                    for (String key: mapDados.get(keyMaior).keySet()){
                        builder.append(" ");
                        builder.append(key);
                        builder.append("-");
                        builder.append(mapDados.get(keyMaior).get(key));
                    }

                    populaScroll(builder.toString());
                }
            }catch (Exception e){
                populaScroll(e.getMessage());
            }

        }else{
            populaScroll("Não há nenhum dado!");
        }
    }

    //configura o spinner com o nome das tabelas do banco de dados
    private void configAdapterSpinner(ArrayList<String> arrTblNames) {

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                getContext(), // contexto da activity
                R.layout.linha_registros, // o desenho de item já pronto
                R.id.text1, // o ID do TextView dentro do item
                arrTblNames); // o ArrayList com as strings a serem exibidas
        // configurar a lista para utilizar este objeto adaptador
        spinner.setAdapter( adaptador );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msupplier = spinner.getSelectedItem().toString();
                setaLayout(msupplier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
