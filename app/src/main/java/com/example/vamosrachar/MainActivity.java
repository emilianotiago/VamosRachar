package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText eValor, eQtde;
    TextView resultado;
    FloatingActionButton compartilhar, ouvir;
    TextToSpeech ttsPlayer;
    int qtde;
    double valor;
    //double resultado;
    String resFormatado = "0,00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eValor = (EditText) findViewById(R.id.editTextNumber2);
        eValor.addTextChangedListener(this);
        eQtde = (EditText)findViewById(R.id.editTextNumber);
        eQtde.addTextChangedListener(this);
        resultado = (TextView) findViewById(R.id.textView11);
        compartilhar = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        compartilhar.setOnClickListener(this);
        ouvir = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        ouvir.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this,this);
            }else{
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //Log.v("PDM", eQtde.getText().toString());
        //Log.v("PDM", eValor.getText().toString());
        try{
            double valor = Double.parseDouble(eValor.getText().toString());
            double qtde = Double.parseDouble(eQtde.getText().toString());
            DecimalFormat df = new DecimalFormat("#.00");
            resultado.setText("R$ " + df.format(valor/qtde) );

        }catch (Exception e){
            resultado.setText("0,00");
        }

    }

    @Override
    public void onClick(View v) {
        if(v == compartilhar){
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "A conta diviida por pessoa deu " + resultado.getText().toString());
            startActivity(intent);
        }

        if(v == ouvir){
            if(ttsPlayer != null){
                ttsPlayer.speak("O valor a ser pago é de " + resultado.getText().toString() + " por pessoa ",
                        TextToSpeech.QUEUE_FLUSH, null,"ID1");
            }

        }

    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            Toast.makeText(this,"TTS ativado...",
                    Toast.LENGTH_LONG).show();

        }else  if(initStatus == TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS habilitado...",
                    Toast.LENGTH_LONG).show();
        }


    }
}