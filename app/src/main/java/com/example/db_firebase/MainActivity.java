package com.example.db_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.db_firebase.entidades.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Crear lista visual
    private ListView ListaProductos;
    //Crear elementos para cargar listview
    private ArrayAdapter<Producto> adapter;
    private ArrayList<Producto> productosArrayList;
    private Producto product ;


    private FirebaseDatabase database;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListaProductos = findViewById(R.id.ListaProductos);

        productosArrayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,productosArrayList);

        conectarFirebase();
        Producto p = new Producto("10001","Fanta","Coca-cola Company",1500,3);
        insertarProducto(p);
        cargarProductos();
    }

    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        if(reference != null) {
            Toast.makeText(this,"Conectado a Firebase", Toast.LENGTH_LONG).show();
        }
    }

    public void insertarProducto(Producto producto){
        reference.child("Productos").push().setValue(producto, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this,"Producto Agregado", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void cargarProductos(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // limpiar Las Listas
                productosArrayList.clear();
                adapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    product = data.getValue(Producto.class);
                    productosArrayList.add(product);
                }
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,productosArrayList);
                ListaProductos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
