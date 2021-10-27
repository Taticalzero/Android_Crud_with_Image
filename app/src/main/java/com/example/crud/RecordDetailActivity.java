package com.example.crud;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Calendar;
import java.util.Locale;

public class RecordDetailActivity extends AppCompatActivity {

    private ShapeableImageView profileIv;
    private TextView nameShowTv,idadeShowTv,emailShowTv,enderecoShowTv,telefoneShowTv,addTimeTv,updatedTimeTv;
    private ActionBar actionBar;

    private MyDbHelper dbHelper;

    private String recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detalhes do Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        dbHelper = new MyDbHelper(this);


        profileIv = findViewById(R.id.profileIv);
        ShapeAppearanceModel shapeAppearanceModel = profileIv.getShapeAppearanceModel().toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,125)
                .build();
        profileIv.setShapeAppearanceModel(shapeAppearanceModel);
        nameShowTv = findViewById(R.id.nameShowTv);
        idadeShowTv = findViewById(R.id.idadeShowTv);
        emailShowTv = findViewById(R.id.emailShowTv);
        enderecoShowTv = findViewById(R.id.enderecoShowTv);
        telefoneShowTv = findViewById(R.id.telefoneShowTv);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(telefoneShowTv,smf);
        telefoneShowTv.addTextChangedListener(mtw);
        addTimeTv = findViewById(R.id.addTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);

        showRecordDetails();

    }

    private void showRecordDetails() {

        String selectQuery = " SELECT * FROM " + ConstBanco.TABLE_NAME + " WHERE " + ConstBanco.C_ID +" =\"" + recordID+"\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range")String id = ""+ cursor.getInt(cursor.getColumnIndex(ConstBanco.C_ID));
                @SuppressLint("Range")String image = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_IMAGE));
                @SuppressLint("Range")String name = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_NAME));
                @SuppressLint("Range")String idade = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_IDADE));
                @SuppressLint("Range")String email = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_EMAIL));
                @SuppressLint("Range")String endereco = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_ENDERECO));
                @SuppressLint("Range")String telefone = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_TELEFONE));
                @SuppressLint("Range")String timestampAdded = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_ADDED_TIMESTAMP));
                @SuppressLint("Range")String timestampUpdated = ""+ cursor.getString(cursor.getColumnIndex(ConstBanco.C_UPDATED_TIMESTAMP));


                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar1);

                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampUpdated));
                String timeUpdated = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar2);



                nameShowTv.setText(name);
                idadeShowTv.setText(idade);
                emailShowTv.setText(email);
                enderecoShowTv.setText(endereco);
                telefoneShowTv.setText(telefone);
                addTimeTv.setText(timeAdded);
                updatedTimeTv.setText(timeUpdated);


                if (image.equals("null")){
                    profileIv.setImageResource(R.drawable.ic_person);

                }else{
                    profileIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }

        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}