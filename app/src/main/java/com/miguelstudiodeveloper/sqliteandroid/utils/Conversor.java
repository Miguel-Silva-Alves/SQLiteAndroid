package com.miguelstudiodeveloper.sqliteandroid.utils;

import java.util.ArrayList;

public class Conversor {

    //converte uma lista de strings em um array de atributos
    public static ArrayList<Atributo> convertStringstoArrayAtributos(String[] string){
        ArrayList<Atributo> arrayList = new ArrayList<>();
        for(int i = 0; i<string.length; i++){
            String[] splicitado = string[i].split(" ");
            Atributo retorno;
            if (splicitado[0].isEmpty() || splicitado[0].equals("")){
                retorno = new Atributo(splicitado[1], splicitado[2]);
            }else {
                retorno = new Atributo(splicitado[0], splicitado[1]);
            }

            arrayList.add(retorno);
        }
        return arrayList;
    }
}
