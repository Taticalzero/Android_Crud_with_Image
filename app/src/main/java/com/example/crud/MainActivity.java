package com.example.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton addRecordBtn;
    private RecyclerView recordRv;

    private MyDbHelper dbHelper;

    ActionBar actionBar;

    //Parametros de Ordenaçao
    String orderByNewest = ConstBanco.C_ADDED_TIMESTAMP + " DESC ";
    String orderByOldest = ConstBanco.C_ADDED_TIMESTAMP + " ASC ";
    String orderByTitleAsc = ConstBanco.C_NAME + " ASC ";
    String orderByTitleDesc = ConstBanco.C_NAME + " DESC ";

    //atualizando os dados salvos

    String currentOrderByStatus = orderByNewest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Todos os Registros");

        addRecordBtn = findViewById(R.id.addRecordBtn);
        recordRv = findViewById(R.id.recordRv);

        dbHelper = new MyDbHelper(this);

        loadRecords(orderByNewest);

        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddUpdateRecordActivity.class);
                intent.putExtra("isEditMode",false);
                startActivity(intent);
            }
        });
    }

    private void loadRecords(String orderBy) {
        currentOrderByStatus = orderBy;
        AdapterRecord adapterRecord = new AdapterRecord(MainActivity.this,
                dbHelper.getAllRecords(orderBy));

        recordRv.setAdapter(adapterRecord);


        actionBar.setSubtitle("TOTAL: "+dbHelper.getRecordsCount());

    }
    private void searchRecords(String query){
        AdapterRecord adapterRecord = new AdapterRecord(MainActivity.this,
                dbHelper.searchAllRecords(query));

        recordRv.setAdapter(adapterRecord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(currentOrderByStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecords(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort){
            sortOptionDialog();
        }
        else if (id == R.id.action_delete_all){
            dbHelper.deleteAllData();
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortOptionDialog() {
        String [] options = {"TITULO ASCENDENTE","TITULO DESCENDENTE","MAIS NOVO","MAIS ANTIGO"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(which == 0){
                            //caso ascendente
                            loadRecords(orderByTitleAsc);
                        }
                        else if (which == 1){
                            //caso descendente
                            loadRecords(orderByTitleDesc);
                        }
                        else if (which == 2){
                            //caso novo
                            loadRecords(orderByNewest);
                        }
                        else if (which == 3){
                            //caso antigo
                            loadRecords(orderByOldest);
                        }
                    }
                })
                .create().show();


    }
}