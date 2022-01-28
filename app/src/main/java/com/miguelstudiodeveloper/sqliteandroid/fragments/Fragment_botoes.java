package com.miguelstudiodeveloper.sqliteandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miguelstudiodeveloper.sqliteandroid.Execucoes;
import com.miguelstudiodeveloper.sqliteandroid.Inicio;
import com.miguelstudiodeveloper.sqliteandroid.R;

public class Fragment_botoes extends Fragment {

    //variaveis
    private String txt1, txt2;

    //UI components
    private Button btn1, btn2;

    public Fragment_botoes(String txt_btn1, String txt_btn2){
        this.txt1 = txt_btn1;
        this.txt2 = txt_btn2;
    }

    public Fragment_botoes(String txt_btn1){
        this.txt1 = txt_btn1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_botoes, container, false);

        btn1 = view.findViewById(R.id.btn_frag_1);
        btn1.setText(this.txt1);
        btn2 = view.findViewById(R.id.btn_frag_2);
        btn1.setOnClickListener( new Escutador("b1"));

        if (txt2 != null){

            btn2.setText(this.txt2);
            btn2.setOnClickListener( new Escutador("b2"));
        }else{
            btn2.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    private class Escutador implements View.OnClickListener {
        private String qual;
        public Escutador(String which) {
            this.qual = which;
        }

        @Override
        public void onClick(View view) {
            Execucoes activity = (Execucoes) getActivity();
            if (this.qual.equals("b1")){ //envio o que ta escrito no próprio botão
                activity.change_placeholder(btn1.getText().toString(), null, null);
            }else{
                activity.change_placeholder(btn2.getText().toString(), null, null);
            }
        }
    }
}
