package com.example.demosdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        TextView textTerms = findViewById(R.id.txtTerms);
        textTerms.setText(getStringAttributes());

        Button btnContinue = findViewById(R.id.btnContinue);
        CheckBox chkTerms = findViewById (R.id.chkTermsEmision);
        chkTerms.setOnCheckedChangeListener ((buttonView, isChecked) -> {
            if (isChecked) {
                btnContinue.setEnabled (true);
            } else {
                btnContinue.setEnabled (false);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    public Spanned getStringAttributes() {
        StringBuilder termsString = new StringBuilder();
        String l7 = Locale.getDefault().getLanguage();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getResources().getAssets().open("textterms")));
            if (reader != null) {
                String str;
                while ((str = reader.readLine()) != null) {
                    termsString.append(str);
                }
                reader.close();
                return Html.fromHtml(termsString.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}