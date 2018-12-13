package com.example.felixits.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button acc_btn;
    private EditText edtname,edtno,edtpassword;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        acc_btn = findViewById(R.id.acc_btn);
        edtname=findViewById(R.id.edtname);
        edtno=findViewById(R.id.edtno);
        edtpassword=findViewById(R.id.edtpasssword);
        loadingbar=new ProgressDialog(this);
    }

    public void create(View view) {
        CreateAccount();
    }

    private void CreateAccount() {
        String name = edtname.getText().toString().trim();
        String no = edtno.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();
        if (TextUtils.isEmpty(name) |TextUtils.isEmpty(no) | TextUtils.isEmpty(password)){

                Toast.makeText(getApplicationContext(),"Please fill the all feilds",Toast.LENGTH_SHORT).show();
            }
        else
            {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait , while we are checking the credential.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            ValidatePhoneno(name,no,password);
        }

    }

    private void ValidatePhoneno(final String name, final String no, final String password) {
    final DatabaseReference Rootref;
    Rootref= FirebaseDatabase.getInstance().getReference();
    Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!(dataSnapshot.child("Users").child(no).exists()))
            {
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("no",no);
                userdataMap.put("password",password);
                userdataMap.put("name",name);

                Rootref.child("Users").child(no).updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"congratulations your account succssfull creadted",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(getApplicationContext(),"Network Error : please try again after some time ...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(),"This"+no+"already exits",Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
                Toast.makeText(getApplicationContext(),"plz try againg another phone no",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
