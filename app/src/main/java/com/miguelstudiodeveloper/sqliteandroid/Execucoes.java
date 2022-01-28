package com.miguelstudiodeveloper.sqliteandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_InserirTabela;
import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_botoes;
import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_inserirDado;
import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_selectAll;
import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_selectEspecificAtributo;
import com.miguelstudiodeveloper.sqliteandroid.fragments.Fragment_updateDado;

import java.io.BufferedReader;
import java.util.Map;

public class Execucoes extends AppCompatActivity {

    //UI components
    private FrameLayout placeholder;
    private TextView textView, crud;

    //variaveis
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execucoes);

        //set UI components
        placeholder = findViewById(R.id.placeholder);
        textView = findViewById(R.id.txt_acao);
        crud = findViewById(R.id.txt_crud);

        Intent intent = getIntent();
        String what = intent.getStringExtra("what");
        String btn1 = intent.getStringExtra("btn1");

        if (intent.hasExtra("letter")){
            textView.setText(intent.getStringExtra("letter"));
        }

        if (intent.hasExtra("btn2")){
            String btn2 = intent.getStringExtra("btn2");
            change_placeholder(what, btn1, btn2);
        }else{
            change_placeholder(what, btn1, null);
        }


        bd = openOrCreateDatabase("banco_teste", MODE_PRIVATE, null);
    }

    //retorna para a activity inicio
    public void go_back(){
        finish();
    }

    //placeholder é o lugar onde 'hospedo' os fragments, assim essa função altera o fragment do placeholder
    public void change_placeholder(String whoCalls, String btn1, String btn2){
        placeholder.removeAllViews();
        FragmentManager fragmentManagerall = getSupportFragmentManager();
        FragmentTransaction ft2 = fragmentManagerall.beginTransaction();
        switch (whoCalls){
            case "botoes":
                if (btn2 != null){
                    Fragment_botoes botoes = new Fragment_botoes(btn1, btn2);
                    ft2.replace(R.id.placeholder, botoes);
                }else{
                    Fragment_botoes sem_dois = new Fragment_botoes(btn1);
                    ft2.replace(R.id.placeholder, sem_dois);
                }
                break;
            case "Inserir tabela":
                Fragment_InserirTabela fragmentInserirTabela = new Fragment_InserirTabela(bd);
                ft2.replace(R.id.placeholder, fragmentInserirTabela);
                break;
            case "Inserir elemento":
                Fragment_inserirDado fragment_inserirDado = new Fragment_inserirDado(bd);
                ft2.replace(R.id.placeholder, fragment_inserirDado);
                break;
            case "Select tabela":
                Fragment_selectAll fragment_selectAll = new Fragment_selectAll(bd);
                ft2.replace(R.id.placeholder, fragment_selectAll);
                break;
            case "Select elemento":
                Fragment_selectEspecificAtributo especificAtributo = new Fragment_selectEspecificAtributo(bd, false, false);
                ft2.replace(R.id.placeholder, especificAtributo);
                break;
            case "Alterar dado":
                Fragment_selectEspecificAtributo especificAtributos = new Fragment_selectEspecificAtributo(bd, true, false);
                ft2.replace(R.id.placeholder, especificAtributos);
                break;
            case "Deletar dado":
                Fragment_selectEspecificAtributo especificAtributoDElete = new Fragment_selectEspecificAtributo(bd, false, true);
                ft2.replace(R.id.placeholder, especificAtributoDElete);
                break;
        }
        ft2.commit();
    }

    //caso especial do changeplaceholder onde recebo um map com os atributos (update)
    public void change_placeholderWithMap(String whoCalls, String nome_tabela, Map<String, String> atributos){
        placeholder.removeAllViews();
        FragmentManager fragmentManagerall = getSupportFragmentManager();
        FragmentTransaction ft2 = fragmentManagerall.beginTransaction();
        switch (whoCalls){
            case "Alterar de fato":
                Fragment_updateDado fragment_updateDado = new Fragment_updateDado(bd, nome_tabela, atributos);
                ft2.replace(R.id.placeholder, fragment_updateDado);
                break;
        }
        ft2.commit();
    }
}