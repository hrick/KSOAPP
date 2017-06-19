package com.example.logonrm.ksouapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    String METHOD_NAME = "Soma";
    String SOAP_ACTION = "";
    String NAMESPACE= "http://henrique.com.br/";
    String SOAP_URL = "http://10.3.1.37:8080/Calculadora/Calculadora";
    EditText etNumero1, etNumero2;
    Button btSomar;
    TextView tvResultado;
    SoapObject request;
    SoapPrimitive calcular;
    int numero1;
    int numero2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNumero1 = (EditText) findViewById(R.id.etNumero1);
        etNumero2 = (EditText) findViewById(R.id.etNumero2);
        tvResultado = (TextView) findViewById(R.id.tvResultado);
        btSomar = (Button) findViewById(R.id.btSomar);
    }

    public void somar(View view) {
        numero1 = Integer.parseInt(etNumero1.getText().toString());
        numero2 = Integer.parseInt(etNumero2.getText().toString());
        CalculadoraAsync calculadoraAsync = new CalculadoraAsync();
        calculadoraAsync.execute(numero1,numero2);
    }
    public class CalculadoraAsync extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("numero1", params[0]);
            request.addProperty("numero2", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            try {
                httpTransport.call(SOAP_ACTION, envelope);
                calcular = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvResultado.setText(calcular.toString());
            Toast.makeText(getApplicationContext(), "Resultado: " + calcular.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
