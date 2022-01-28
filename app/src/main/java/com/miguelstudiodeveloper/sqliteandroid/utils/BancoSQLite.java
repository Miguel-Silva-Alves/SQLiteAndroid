package com.miguelstudiodeveloper.sqliteandroid.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BancoSQLite {

    //retorna o nome das tabelas a partir de um banco de dados
    @SuppressLint("Range")
    public static ArrayList<String> getNomeTabelas(SQLiteDatabase banco){
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = banco.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('android_metadata', 'sqlite_sequence', 'room_master_table')" , null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }
        return arrTblNames;
    }

    //retorna um array com objetos ATRIBUTO que possui o nome e o tipo do atributo
    public static ArrayList<Atributo> getAtributos(SQLiteDatabase banco, String nome_tabela){
        Cursor c = banco.rawQuery("SELECT sql FROM sqlite_master WHERE tbl_name = '"+nome_tabela+"'", null);
        if (c.moveToFirst()) {
            @SuppressLint("Range") String s = String.valueOf(c.getString(c.getPosition()));
            try {
                String[] valores = s.split(nome_tabela);
                if(valores.length>1){
                    String m = valores[1].substring(1, valores[1].length()-1);
                    String[] lista = m.split(",");

                    return Conversor.convertStringstoArrayAtributos(lista);

                }
            }catch (Exception e){
                //testagem.setText(e.getMessage());
                return null;
            }
        }
        return null;
    }

    //Seleciona todos os dados de uma tabela e retorna um map
    @SuppressLint("Range")
    public static Map<String, Map<String, String>> getDadosFromTabelaMap(SQLiteDatabase banco, String nome_tabela, ArrayList<Atributo> atributos) {
        //agrupa todos os atributos
        Log.v("Banco", "Iniciando com " + nome_tabela);
        StringBuilder builder = new StringBuilder("SELECT ");
        for (int x = 0; x<atributos.size(); x++){

            builder.append(atributos.get(x).getNome());

            if (x < atributos.size()-1){
                builder.append(", ");
            }
        }
        builder.append(" FROM ");
        builder.append(nome_tabela);

        // consulta para gerar o Cursor
        Log.v("Banco", builder.toString());
        Cursor cursor = banco.rawQuery( builder.toString(), null );

        Map<String, Map<String, String>> map_dados = new HashMap<>();
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {

                String[] nome_colunas = cursor.getColumnNames();
                Map<String, String> components = new HashMap<>();
                String id = null;
                for (int x=0; x<nome_colunas.length; x++){
                    if (nome_colunas[x].equals("id")){
                        id = cursor.getString( cursor.getColumnIndex(nome_colunas[x]) );
                    }else{
                        components.put(nome_colunas[x], cursor.getString( cursor.getColumnIndex(nome_colunas[x]) ));
                    }
                }
                if (id != null){
                    map_dados.put(id, components);
                }

                cursor.moveToNext();
            }
        }
        return map_dados;
    }

    //Select valores que correspondem à um determinado valor de um atributo
    @SuppressLint("Range")
    public static Map<String, Map<String, String>> getDadosFromTabelaWithEspecificAtributoMap(SQLiteDatabase banco, String nome_tabela, ArrayList<Atributo> atributos) {
        //agrupa todos os atributos
        Log.v("Banco", "Iniciando com " + nome_tabela);
        StringBuilder builder = new StringBuilder("SELECT ");
        String valor_where = null, argumento = null;
        for (int x = 0; x<atributos.size(); x++){

            builder.append(atributos.get(x).getNome());
            if (atributos.get(x).getValor_temporario() != null){
                valor_where = atributos.get(x).getNome();
                argumento = atributos.get(x).getValor_temporario();
            }

            if (x < atributos.size()-1){
                builder.append(", ");
            }
        }
        builder.append(" FROM ");
        builder.append(nome_tabela);
        Cursor cursor;
        if (valor_where != null){
            builder.append(" WHERE ");
            builder.append(valor_where);
            builder.append(" = ?");
            cursor = banco.rawQuery( builder.toString(), new String[] {argumento} );
        }else{
            cursor = banco.rawQuery( builder.toString(), null );
        }

        // consulta para gerar o Cursor
        Log.v("Banco", builder.toString());
        Map<String, Map<String, String>> map_dados = new HashMap<>();
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                Log.v("Banco", "Entradas!");

                String[] nome_colunas = cursor.getColumnNames();
                Map<String, String> components = new HashMap<>();
                String id = null;
                for (int x=0; x<nome_colunas.length; x++){
                    if (nome_colunas[x].equals("id")){
                        id = cursor.getString( cursor.getColumnIndex(nome_colunas[x]) );
                    }else{
                        components.put(nome_colunas[x], cursor.getString( cursor.getColumnIndex(nome_colunas[x]) ));
                    }
                }
                if (id != null){
                    map_dados.put(id, components);
                }
                cursor.moveToNext();
            }
        }
        return map_dados;
    }

    //cria uma tabela no banco
    public static void createTable(SQLiteDatabase banco, String nome_tabela, ArrayList<Atributo> atributos){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(nome_tabela);
        builder.append("( id INTEGER PRIMARY KEY AUTOINCREMENT");
        for (int x = 0; x < atributos.size(); x++){
            builder.append(", ");
            builder.append(atributos.get(x).getNome());
            builder.append(" ");
            builder.append(atributos.get(x).getTipo());
        }
        builder.append(")");
        banco.execSQL(builder.toString());
    }

    //insere um elemento na tabela
    public static void insertElemento(SQLiteDatabase banco, String nome_tabela, ArrayList<Atributo> atributos){
        ContentValues values = new ContentValues();
        for(int i = 0; i < atributos.size(); i++){
            if (!atributos.get(i).getNome().equals("id")){
                values.put(atributos.get(i).getNome(), atributos.get(i).getValor_temporario());
            }
        }
        banco.insert(nome_tabela, null, values);
    }

    //atualiza um dado da tabela
    public static void update(SQLiteDatabase banco, String nome_tabela, Map<String, String> valores){
        ContentValues values = new ContentValues();
        for(String key : valores.keySet()){
            if (!valores.get(key).equals("id")){
                values.put(key, valores.get(key));
            }
        }
        banco.update(nome_tabela, values, "id = ?", new String[]{valores.get("id")});
    }

    //deleta um dado da tabela
    public static void delete(SQLiteDatabase banco, String nome_tabela, String id){
        banco.delete(nome_tabela, "id = ?", new String[] { id });
    }
}
