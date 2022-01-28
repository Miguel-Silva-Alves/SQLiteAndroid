package com.miguelstudiodeveloper.sqliteandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Inicio extends AppCompatActivity {

    //components UI
    private TextView letterC, letterR, letterU, letterD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //set UI components
        letterC = findViewById(R.id.letterC);
        letterR = findViewById(R.id.letterR);
        letterU = findViewById(R.id.letterU);
        letterD = findViewById(R.id.letterD);


        letterC.setOnClickListener( new EscutadorUnico("C"));
        letterR.setOnClickListener( new EscutadorUnico("R"));
        letterU.setOnClickListener( new EscutadorUnico("U"));
        letterD.setOnClickListener( new EscutadorUnico("D"));
    }

    private class EscutadorUnico implements View.OnClickListener {
        private String letter;
        public EscutadorUnico(String letter) {
            this.letter = letter;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Execucoes.class);
            intent.putExtra("what", "botoes");
            switch (this.letter){
                case "C":
                    intent.putExtra("btn1", "Inserir tabela");
                    intent.putExtra("btn2", "Inserir elemento");
                    intent.putExtra("letter", "Create");
                    break;
                case "R":
                    intent.putExtra("btn1", "Select tabela");
                    intent.putExtra("btn2", "Select elemento");
                    intent.putExtra("letter", "Read");
                    break;
                case "U":
                    intent.putExtra("btn1", "Alterar dado");
                    intent.putExtra("letter", "Update");
                    break;
                case "D":
                    intent.putExtra("btn1", "Deletar dado");
                    intent.putExtra("letter", "Delete");
                    break;
            }
            startActivity(intent);
        }
    }
}