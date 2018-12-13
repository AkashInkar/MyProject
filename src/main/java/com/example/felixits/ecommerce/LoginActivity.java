package com.example.felixits.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.felixits.ecommerce.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText login_no,login_password;
    private Button login_btn;
    private ProgressDialog loadingbar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_no = findViewById(R.id.login_no);
        login_password = findViewById(R.id.log_passsword);
        login_btn = findViewById(R.id.login_btn);
        loadingbar=new ProgressDialog(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });

    }

    private void UserLogin()
    {
        String no = login_no.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        if (TextUtils.isEmpty(no) | TextUtils.isEmpty(password)){

            Toast.makeText(getApplicationContext(),"Please fill the all feilds",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("User login");
            loadingbar.setMessage("Please wait , while we are checking the credential.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(no,password);
        }

    }

    private void AllowAccessToAccount(final String no, final String password)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

            {

                if (dataSnapshot.child(parentDbName).child(no).exists())
                {
                    Users usersData=dataSnapshot.child(parentDbName).child(no).getValue(Users.class);
                    if (usersData.getPhone().equals(no))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(getApplicationContext(),"Logged in succssfull",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Log.d("hiu","akash");
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Account with this "+no +"number do not exits",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                   // Toast.makeText(getApplicationContext(),"You need to create a new account",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
