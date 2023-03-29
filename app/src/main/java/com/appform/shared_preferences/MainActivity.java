package com.appform.shared_preferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nombre, cedula, telefono;
    Button guardar, consultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.name);
        cedula = findViewById(R.id.nit);
        telefono = findViewById(R.id.tel);
        guardar = findViewById(R.id.save);
        consultar = findViewById(R.id.select);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {select(view);}
        });
    }

    public void save(View view){
        adminBd admin = new adminBd(this, "BaseDatos", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String name = nombre.getText().toString();
        String nit = cedula.getText().toString();
        String tel = telefono.getText().toString();
        if(!name.isEmpty() && !nit.isEmpty() && !tel.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("nombre", name);
            registro.put("cedula", nit);
            registro.put("telefono", tel);
            BaseDatos.insert("usuario", null, registro);
            BaseDatos.close();
            nombre.setText("");
            cedula.setText("");
            telefono.setText("");
            Toast.makeText(this, "Registro Guardado Con Exito", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Todos Los Datos Son Obligatorios", Toast.LENGTH_LONG).show();
        }
    }

    public void select(View view){
        adminBd admin = new adminBd(this, "BaseDatos", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String nit = cedula.getText().toString();
        if (!nit.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("select nombre, telefono from usuario where cedula="+nit, null);
            if(fila.moveToFirst()){
                nombre.setText(fila.getString(0));
                telefono.setText((fila.getString(1)));
                BaseDatos.close();
            }
            else{
                Toast.makeText(this, "Usuario No Encontrado", Toast.LENGTH_LONG).show();
            }
        }
    }
}