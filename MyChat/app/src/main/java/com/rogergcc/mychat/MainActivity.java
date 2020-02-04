package com.rogergcc.mychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    EditText edtmensaje;
    TextView txtmensaje;
    TextView txtmensajerecibido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket = App.getSocket();
        mSocket.on("recibirmensaje", recibirmensaje);
        mSocket.connect();
        edtmensaje = findViewById(R.id.edtmensaje);
        txtmensaje = findViewById(R.id.txtmimensaje);
        txtmensajerecibido=findViewById(R.id.txtmensaje);
    }

    public void enviar(View view) {
        mSocket.emit("enviarmensaje", edtmensaje.getText().toString(), new Ack() {
            @Override
            public void call(Object... args) {
                String res = (String) args[0];
                if (res.equals("OK")) {
                    Log.i("mimensaje", "Se envio correctamente");
                } else
                    Log.i("mimensaje", "Hubo error en el envio");
            }
        });
        txtmensaje.setText(edtmensaje.getText().toString());
    }
    private Emitter.Listener recibirmensaje = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
// Se crea un JSONObjet
            JSONObject paramsRequest = (JSONObject) args[0];
            try {
                final String mensaje = paramsRequest.getString("mensaje");
// Se crea un intent enviando el email
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtmensajerecibido.setText(mensaje);
                    }
                });
            } catch (JSONException e) {
                Log.e("JSONException", e.toString());
            }
        }
    };

}
