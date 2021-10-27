package com.example.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.HolderRecord> {

    private Context context;
    private ArrayList<ModelRecord> recordList;

    MyDbHelper dbHelper;

    public AdapterRecord(Context context, ArrayList<ModelRecord> recordList) {
        this.context = context;
        this.recordList = recordList;

        dbHelper = new MyDbHelper(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_record,parent,false);

        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, @SuppressLint("RecyclerView") int position) {


        ModelRecord model = recordList.get(position);
        String id = model.getId();
        String name = model.getName();
        String image = model.getImage();
        String idade = model.getIdade();
        String endereco = model.getEndereco();
        String email = model.getEmail();
        String telefone = model.getTelefone();
        String addedTime = model.getAddedTime();
        String updatedTime = model.getUpdatedTime();


        holder.nameTv.setText(name);
        holder.idadeTv.setText(idade);
        holder.enderecoTv.setText(endereco);
        holder.emailTv.setText(email);
        holder.telefoneTv.setText(telefone);

        if (image.equals("null")){
            holder.profileIv.setImageResource(R.drawable.ic_person);

        }else{
            holder.profileIv.setImageURI(Uri.parse(image));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordDetailActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);

            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDialog(
                        ""+position,
                        ""+id,
                        ""+name,
                        ""+idade,
                        ""+endereco,
                        ""+email,
                        ""+telefone,
                        ""+image,
                        ""+addedTime,
                        ""+updatedTime
                );

            }
        });

        Log.d("ImagePath", "onBindViewHolder: "+image);

    }

    private void showMoreDialog(String position, final String id, final String name, final String idade,
                                final String endereco, final String email, final String telefone, final String image,
                                final String addedTime, final String updatedTime) {

        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0){
                    //editar

                    //update
                    Intent intent = new Intent(context, AddUpdateRecordActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("NAME",name);
                    intent.putExtra("IDADE",idade);
                    intent.putExtra("ENDERECO",endereco);
                    intent.putExtra("EMAIL",email);
                    intent.putExtra("TELEFONE",telefone);
                    intent.putExtra("IMAGE",image);
                    intent.putExtra("ADDED_TIME",addedTime);
                    intent.putExtra("UPDATED_TIME",updatedTime);
                    intent.putExtra("isEditMode", true);
                    context.startActivity(intent);

                }
                else if (which == 1){
                    //deletar se for clickado no menu de edicao
                    dbHelper.deleteData(id);
                    //refresh para o menu
                    ((MainActivity)context).onResume();
                }
            }
        });

        builder.create().show();

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class HolderRecord extends RecyclerView.ViewHolder{

        ShapeableImageView profileIv;
        TextView nameTv,telefoneTv,emailTv,idadeTv,enderecoTv;
        ImageButton moreBtn;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            ShapeAppearanceModel shapeAppearanceModel = profileIv.getShapeAppearanceModel().toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED,125)
                    .build();
            nameTv = itemView.findViewById(R.id.nameTv);
            idadeTv = itemView.findViewById(R.id.idadeTv);
            enderecoTv = itemView.findViewById(R.id.enderecoTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            telefoneTv = itemView.findViewById(R.id.telefoneTv);
            SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
            MaskTextWatcher mtw = new MaskTextWatcher(telefoneTv,smf);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }
    }
}
