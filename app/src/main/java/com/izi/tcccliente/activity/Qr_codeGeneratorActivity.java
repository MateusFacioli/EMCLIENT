package com.izi.tcccliente.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.izi.tcccliente.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class Qr_codeGeneratorActivity extends AppCompatActivity {

    private Button scan;
    private ImageView qr_code;
    private TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_generator);
        scan=findViewById(R.id.open_class);
        qr_code=findViewById(R.id.qr_code);
        information=findViewById(R.id.texto_qrcode);
        CarrinhoActivity carrinhoActivity;
        carrinhoActivity= new CarrinhoActivity();
        String json= carrinhoActivity.getALL("teste","teste");
        information.setText(json);
        String text= information.getText().toString().trim();
        if(text!=null){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {

                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                qr_code.setImageBitmap(bitmap);
            }
            catch (WriterException e)
            {
                e.printStackTrace();
            }


        }



        //final Activity activity = this;

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent inicio = new Intent(Qr_codeGeneratorActivity.this, Activity_Recebido.class);
                startActivity(inicio);
                finish();
                /*
                String text= information.getText().toString().trim();
                if(text!=null){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {

                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        qr_code.setImageBitmap(bitmap);
                    }
                    catch (WriterException e)
                    {
                        e.printStackTrace();
                    }


                }
                */


            }
        });

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

}