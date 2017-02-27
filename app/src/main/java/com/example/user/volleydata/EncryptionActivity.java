package com.example.user.volleydata;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptionActivity extends AppCompatActivity {

    EditText enterName, enterContact, enterEmail;
    TextView name, contact, email;
    Button saveData, retrieveData;

    public static  final  String MYPREFERENCES="MyPrefs";
    public static final String Name="nameKey";
    public static final String Phone="phoneKey";
    public static final String Email="emailKey";


    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        enterName= (EditText) findViewById(R.id.editText);
        enterContact= (EditText) findViewById(R.id.editText2);
        enterEmail= (EditText) findViewById(R.id.editText3);
        name= (TextView) findViewById(R.id.viewName);
        contact= (TextView) findViewById(R.id.viewContact);
        email= (TextView) findViewById(R.id.viewEmail);
        saveData= (Button) findViewById(R.id.saveButton);
        retrieveData= (Button) findViewById(R.id.RetrieveButton);

        sharedPreferences=getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        //only name field is encrypted

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n= enterName.getText().toString();
                Encrypt nameEncrypt=Encrypt.RSA(n);
                String ph= enterContact.getText().toString();
                String e= enterEmail.getText().toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString(Name, nameEncrypt.getEncodedString());
                editor.putString(Phone, ph);
                editor.putString(Email, e);
                editor.commit();
                Toast.makeText(EncryptionActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
            }

        });
        retrieveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName=sharedPreferences.getString(Name, "");
                name.setText(uName.toString());
            }
        });
    }
}
