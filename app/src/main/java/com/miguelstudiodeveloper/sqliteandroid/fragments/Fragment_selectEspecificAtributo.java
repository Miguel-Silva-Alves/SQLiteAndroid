package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.miguelstudiodeveloper.sqliteandroid.Execucoes;
import com.miguelstudiodeveloper.sqliteandroid.R;
import com.miguelstudiodeveloper.sqliteandroid.utils.Atributo;
import com.miguelstudiodeveloper.sqliteandroid.utils.BancoSQLite;

import java.util.ArrayList;
import java.util.Map;

public class Fragment_selectEspecificAtributo extends Fragment {

    //variaveis
    private SQLiteDatabase banco;
    private ArrayAdapter<String> adaptadorAtributos;
    Map<String, Map<String, String>> dados;
    private boolean isUpdate, isDelete;
    private Dialog dialog;


    //UI Components
    private Spinner spinner_tabela, spinner_atributo;
    private EditText valor_atributo;
    private ImageView btn_pesquisar;
    private ScrollView scrollView;

    public Fragment_selectEspecificAtributo(SQLiteDatabase banco, boolean isUpdate, boolean isDelete){
        this.banco = banco;
        this.isDelete = isDelete;
        this.isUpdate = isUpdate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_especific, container, false);

        //set UI components
        spinner_atributo = view.findViewById(R.id.sele_espe_spinner_atributos);
        spinner_tabela = view.findViewById(R.id.select_especific_spinner);
        btn_pesquisar = view.findViewById(R.id.sele_espe_search);
        valor_atributo = view.findViewById(R.id.select_espe_edit);
        scrollView = view.findViewById(R.id.select_especific_scroll);

        //set onclicklistener
        btn_pesquisar.setOnClickListener( new EscutadorPesquisar() );

        ArrayList<String> arrTblNames = BancoSQLite.getNomeTabelas(this.banco);
        configAdapterSpinner(arrTblNames);

        if (!arrTblNames.isEmpty()){
            configAdapterSpinnerAtributo(arrTblNames.get(0));
        }

        return view;
    }

    //configura o spinner com o nome das tabelas do banco de dados
    private void configAdapterSpinner(ArrayList<String> arrTblNames) {

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                getContext(), // contexto da activity
                R.layout.linha_registros, // o desenho de item já pronto
                R.id.text1, // o ID do TextView dentro do item
                arrTblNames); // o ArrayList com as strings a serem exibidas
        // configurar a lista para utilizar este objeto adaptador
        spinner_tabela.setAdapter( adaptador );

        spinner_tabela.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msupplier = spinner_tabela.getSelectedItem().toString();
                configAdapterSpinnerAtributo(msupplier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //configura o adapter do spinner de atributos dinamicamente
    private void configAdapterSpinnerAtributo(String msupplier) {
        valor_atributo.setText("");

        ArrayList<Atributo> atributoArrayList = BancoSQLite.getAtributos(banco, msupplier);
        ArrayList<String> atributos_nomes = new ArrayList<>();
        for(int i = 0; i<atributoArrayList.size();i++){
            atributos_nomes.add(atributoArrayList.get(i).getNome());
        }

        if (adaptadorAtributos == null){
            adaptadorAtributos = new ArrayAdapter<>(
                    getContext(), // contexto da activity
                    R.layout.linha_registros, // o desenho de item já pronto
                    R.id.text1, // o ID do TextView dentro do item
                    atributos_nomes); // o ArrayList com as strings a serem exibidas
            // configurar a lista para utilizar este objeto adaptador
            spinner_atributo.setAdapter(adaptadorAtributos);
        }else{
            adaptadorAtributos.clear();
            for(int i = 0; i<atributos_nomes.size();i++){
                adaptadorAtributos.add(atributos_nomes.get(i));
            }
            adaptadorAtributos.notifyDataSetChanged();
        }
    }

    //insere um elemento no scrollview
    public void populaScroll(String texto, String key){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.layout_linha, null, false);

        TextView textView = rowView.findViewById(R.id.lblTextoInserir);
        textView.setText(texto);

        if (isUpdate){
            textView.setLongClickable(true);
            textView.setOnLongClickListener( new EscutadorLongoUpdate(key) );
        }

        if (isDelete){
            textView.setLongClickable(true);
            textView.setOnLongClickListener( new EscutadorLongoDelete(key) );
        }

        LinearLayout layout = scrollView.findViewById(R.id.select_especific_layout);
        layout.addView(rowView);
    }

    //função que cria o diálogo para confirmação do delete
    public void CreateDialog(String nome_tabela, String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete, null);
        Button confirmar = viewInflated.findViewById(R.id.dialog_confirmar);
        Button cancelar = viewInflated.findViewById(R.id.dialog_cancelar);

        confirmar.setOnClickListener( new EscutadorConfirmarDelete(nome_tabela, id) );
        cancelar.setOnClickListener(new EscutadorCancelarDialog());
        builder.setView(viewInflated);

        builder.create();
        dialog = builder.show();
    }

    /*
    ========================================================
                         ESCUTADORES
    ========================================================

     */

    private class EscutadorPesquisar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String nome_tabela = spinner_tabela.getSelectedItem().toString();
            String atributo = spinner_atributo.getSelectedItem().toString();
            String pesquisa = valor_atributo.getText().toString();
            LinearLayout layout = scrollView.findViewById(R.id.select_especific_layout);
            layout.removeAllViews();
            ArrayList<Atributo> atributoArrayList = BancoSQLite.getAtributos(banco, nome_tabela);
            for(int o = 0; o<atributoArrayList.size(); o++){
                if(atributoArrayList.get(o).getNome().equals(atributo)){
                    atributoArrayList.get(o).setValor_temporario(pesquisa);
                }
            }
            //aqui permito que caso o usuário não saiba o atributo ele recebe o Select ALL
            //isso foi feito para que esse fragment seja reaproveitado no update e no delete

            if (!pesquisa.isEmpty()){
                dados = BancoSQLite.getDadosFromTabelaWithEspecificAtributoMap(banco, nome_tabela, atributoArrayList);
            }else{
                dados = BancoSQLite.getDadosFromTabelaMap(banco, nome_tabela, atributoArrayList);
            }

            //verificação básica que exclui caso não haja dados a serem exibidos
            if (dados.isEmpty()){
                populaScroll("Não há dados!", null);
                return;
            }

            for (String keyMaior : dados.keySet()){
                StringBuilder builder = new StringBuilder("id-");
                builder.append(keyMaior);
                for (String key: dados.get(keyMaior).keySet()){
                    builder.append(" ");
                    builder.append(key);
                    builder.append("-");
                    builder.append(dados.get(keyMaior).get(key));
                }

                populaScroll(builder.toString(), keyMaior);
            }
        }
    }

    private class EscutadorLongoUpdate implements View.OnLongClickListener {
        private String parChaveValor;
        public EscutadorLongoUpdate(String texto) {
            parChaveValor = texto;
        }

        @Override
        public boolean onLongClick(View view) {
            String nome_tabela = spinner_tabela.getSelectedItem().toString();
            Execucoes execucoes = (Execucoes) getActivity();
            Map<String, String> atributos = dados.get(parChaveValor);
            atributos.put("id", parChaveValor);
            execucoes.change_placeholderWithMap("Alterar de fato", nome_tabela, atributos);
            return false;
        }
    }

    private class EscutadorLongoDelete implements View.OnLongClickListener {
        private String parChaveValor;
        public EscutadorLongoDelete(String texto) {
            parChaveValor = texto;
        }

        @Override
        public boolean onLongClick(View view) {
            String nome_tabela = spinner_tabela.getSelectedItem().toString();
            CreateDialog(nome_tabela, parChaveValor);
            //Execucoes execucoes = (Execucoes) getActivity();
            //execucoes.change_placeholder("Deletar de fato", nome_tabela, parChaveValor);
            return false;
        }
    }

    private class EscutadorConfirmarDelete implements View.OnClickListener {
        private String nome_tabela, id;
        public EscutadorConfirmarDelete(String nome_tabela, String id) {
            this.nome_tabela = nome_tabela;
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            BancoSQLite.delete(banco, this.nome_tabela, this.id);
            Toast.makeText(getContext(), "Dado removido", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            Execucoes execucoes = (Execucoes) getActivity();
            execucoes.go_back();
        }
    }

    private class EscutadorCancelarDialog implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dialog.cancel();
        }
    }
}
