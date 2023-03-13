package com.example.crudoperation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.crudoperation.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
Uri uri;
int maxId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseDatabase.getInstance().getReference().child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId= (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("name",binding.etName.getText().toString());
                map.put("email",binding.etEmail.getText().toString());
                map.put("pass",binding.etPassword.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("data").setValue(map);
                FirebaseStorage.getInstance().getReference().child("MyData").child(String.valueOf(maxId+1)).putFile(uri);
                Toast.makeText(MainActivity.this, "send data", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("data").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       Map<String,Object> getMap= (Map) snapshot.getValue();
                       if (snapshot.exists()){
                           String name=(String) getMap.get("name");
                           String email=(String) getMap.get("email");
                           String pass=(String) getMap.get("pass");
                           binding.tvName.setText(name);
                           binding.tvEmail.setText(email);
                           binding.tvPass.setText(pass);
                           //Toast.makeText(MainActivity.this, "data Fetch", Toast.LENGTH_SHORT).show();
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap updateHash=new HashMap();
                updateHash.put("name",binding.etName.getText().toString());
                updateHash.put("email",binding.etEmail.getText().toString());
                updateHash.put("pass",binding.etPassword.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("data").updateChildren(updateHash);



            }
        });
        binding.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("data").removeValue();
                binding.tvName.setText(null);
                binding.tvEmail.setText(null);
                binding.tvPass.setText(null);

            }
        });
    }


    }
