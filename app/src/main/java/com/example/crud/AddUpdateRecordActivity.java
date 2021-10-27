package com.example.crud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class AddUpdateRecordActivity extends AppCompatActivity {
    private ShapeableImageView profileIv;
    private TextInputEditText nameEdt,emailEdt,senhaEdt,idadeEdt,telefoneEdt,enderecoEdt;
    private FloatingActionButton saveBtn;

    private ActionBar actionBar;

    //Codigos de permissao para acesso a camera
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 101;
    //Codigos de permissao para acesso a galeria
    public static final int  IMAGE_PICK_CAMERA_CODE = 102;
    public static final int  IMAGE_PICK_GALLERY_CODE = 103;
    //Armazenamento das requisiçoes de permissao
    private String[] cameraPermissions;
    private  String[] storagePermissions;

    //Requisiçao da img e variaveis do banco
    private Uri imageUri;
    private String id,name,telefone,email,endereco,senha,idade,addedTime,updatedTime;
    private boolean isEditMode = false;

    //Conexao banco
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);
        //iniciar
        actionBar = getSupportActionBar();
        //titulo da pag
        actionBar.setTitle("Adicionar Registros");
        //botao de voltar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //img carregamento
        profileIv = findViewById(R.id.profileIV);
        ShapeAppearanceModel shapeAppearanceModel = profileIv.getShapeAppearanceModel().toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,125)
                .build();
        profileIv.setShapeAppearanceModel(shapeAppearanceModel);
        //campos de digitar
        nameEdt = findViewById(R.id.nameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        senhaEdt = findViewById(R.id.senhaEdt);
        idadeEdt = findViewById(R.id.idadeEdt);
        telefoneEdt = findViewById(R.id.telefoneEdt);
        enderecoEdt = findViewById(R.id.enderecoEdt);
        //botao
        saveBtn = findViewById(R.id.saveBtn);

        //pegando info do Argumentos passados para metodo Intent
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);


        //Mandando as informaçoes para as views

        if (isEditMode){


            actionBar.setTitle("Atualizar Informaçao");

            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            idade = intent.getStringExtra("IDADE");
            endereco = intent.getStringExtra("ENDERECO");
            email = intent.getStringExtra("EMAIL");
            telefone = intent.getStringExtra("TELEFONE");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            addedTime = intent.getStringExtra("ADDED_TIME");
            updatedTime = intent.getStringExtra("UPDATED_TIME");

            nameEdt.setText(name);
            idadeEdt.setText(idade);
            enderecoEdt.setText(endereco);
            emailEdt.setText(email);
            telefoneEdt.setText(telefone);

            if (imageUri.toString().equals("null")){
                profileIv.setImageResource(R.drawable.ic_person);

            }else{
                profileIv.setImageURI(imageUri);
            }

        }else {

            actionBar.setTitle("Adicionar Info");

        }



        //iniciando o banco
        dbHelper = new MyDbHelper(this);
        //permissoes
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(telefoneEdt,smf);
        telefoneEdt.addTextChangedListener(mtw);


        //Pegar Img da galeria
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();

            }
        });
        // Salvar os dados
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
    }

    private void inputData() {
        //pegando os dados do input
        name = ""+nameEdt.getText().toString().trim();
        email = ""+emailEdt.getText().toString().trim();
        senha = ""+senhaEdt.getText().toString().trim();
        idade = ""+idadeEdt.getText().toString().trim();
        telefone = ""+telefoneEdt.getText().toString().trim();
        endereco = ""+enderecoEdt.getText().toString().trim();

        if (isEditMode){
            //Atualizando o banco

            String timestamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+id,
                    ""+name,
                    ""+imageUri,
                    ""+email,
                    ""+senha,
                    ""+idade,
                    ""+telefone,
                    ""+endereco,
                    ""+addedTime,
                    ""+timestamp
            );

            Toast.makeText(this, "Atualizando ....", Toast.LENGTH_SHORT).show();

        } else {

            //salvando no banco
            String timestamp = ""+System.currentTimeMillis();
            long id = dbHelper.insertRecord(
                    ""+name,
                    ""+imageUri,
                    ""+email,
                    ""+senha,
                    ""+idade,
                    ""+telefone,
                    ""+endereco,
                    ""+timestamp,
                    ""+timestamp
            );

            Toast.makeText(this, "Cadastro Salvo e Responde pelo ID: "+id, Toast.LENGTH_SHORT).show();
        }

    }

    private void imagePickDialog() {
        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pegue uma Img :");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0){
                    if (!checkCameraPermissions()){
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }
                }else if (which == 1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();

    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void copyFileOrDirectory(String srcDir, String desDir){
        try {
            File src = new File(srcDir);
            File des = new File(desDir, src.getName());
            if (src.isDirectory()){
                String[] files = src.list();
                int filesLenght = files.length;
                for (String file : files){
                    String src1 = new File(src,file).getPath();
                    String dst1 = des.getPath();

                    copyFileOrDirectory(src1,dst1);
                }
            } else {
                copyFile(src, des);
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFile(File srcDir, File desDir) throws IOException {
        if (!desDir.getParentFile().exists()){
            desDir.mkdir();
        }
        if (!desDir.exists()){
            desDir.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileOutputStream(srcDir).getChannel();
            destination = new FileOutputStream(desDir).getChannel();
            destination.transferFrom(source, 0, source.size());

            imageUri = Uri.parse(desDir.getPath());
            Log.d("ImagePath","copyFile: "+imageUri);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if (source!=null){
                source.close();
            }
            if (destination!=null){
                destination.close();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //irá retornar ao menu anterior
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "PERMISSOES DE CAMERA E ARMAZENAMENTO SAO NECESSARIAS", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "PERMISSAO DE ARMAZENAMENTO E NECESSARIA", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){


                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){

                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    profileIv.setImageURI(resultUri);



                    copyFileOrDirectory(""+imageUri.getPath(),""+getDir("SQLiteRecordImages",MODE_PRIVATE));
                }
                else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}