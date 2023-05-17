package com.example.demosdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

import java.io.File;
import java.net.ResponseCache;

public class ResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        TextView text_name, text_number, text_document, text_date_of_birth;
        ImageView imgChkBiometric, imgChkDetection, imgChkList, imgChkRevision, img_user;
        Button btnFinish = findViewById(R.id.btnFinish);

        text_name = findViewById(R.id.text_name);
        text_number = findViewById(R.id.text_number);
        text_document = findViewById(R.id.text_document);
        text_date_of_birth = findViewById(R.id.text_date_of_birth);
        imgChkBiometric = findViewById(R.id.imgChkBiometric);
        imgChkDetection = findViewById(R.id.imgChkDetection);
        imgChkList = findViewById(R.id.imgChkList);
        imgChkRevision = findViewById(R.id.imgChkRevision);
        img_user = findViewById(R.id.img_user);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResponseActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ResponseIV responseIV = (ResponseIV) extras.getSerializable("responseIV");
//
//            String fullname = responseIV.getFullname();
//            String birth = responseIV.getBirth();
//            String document_type = responseIV.getDocument_type();
//            String document_number = responseIV.getDocument_number();
//
//            text_name.setText(fullname);
//            text_number.setText(String.format("No. %s", document_number));
//            text_document.setText(String.format("Documento: %s", document_type));
//            text_date_of_birth.setText(String.format("Fecha nacimiento: %s", birth));
//            imgChkBiometric.setImageResource(responseIV.getFace_match() ? R.drawable.check_icon : R.drawable.facil_icon);
//            imgChkDetection.setImageResource(responseIV.getAlteration() ? R.drawable.check_icon : R.drawable.facil_icon);
//            imgChkList.setImageResource(responseIV.getWatch_list() ? R.drawable.check_icon : R.drawable.facil_icon);
//            imgChkRevision.setImageResource(responseIV.getTemplate() ? R.drawable.check_icon : R.drawable.facil_icon);
//
//            String pathToFileFront = responseIV.getSelfiImageUrlLocal();
//            File imgFileFront = new File (pathToFileFront);
//            if (imgFileFront.exists ( )) {
//                Bitmap myBitmap = BitmapFactory.decodeFile (imgFileFront.getAbsolutePath ( ));
//                img_user.setImageBitmap (myBitmap);
//            }

        }

    }
}