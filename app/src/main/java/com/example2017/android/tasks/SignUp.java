package com.example2017.android.tasks;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText mEmail,mPassword,mRepeatPassword,mUsername,mMobile;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth=FirebaseAuth.getInstance();

        init();
    }

    public void Register(View v){
        String email=mEmail.getText().toString().toLowerCase().trim();
        String password=mPassword.getText().toString().toLowerCase().trim();
        String repeatpassword=mRepeatPassword.getText().toString().toLowerCase().trim();
        final String username=mUsername.getText().toString().toLowerCase().trim();


        if (TextUtils.isEmpty(email) ||TextUtils.isEmpty(password)|| TextUtils.isEmpty(repeatpassword) || TextUtils.isEmpty(username)){

            Toast.makeText(SignUp.this, "من فضلك املأ الفراغات كامله", Toast.LENGTH_SHORT).show();
        }else {
            if (password.equals(repeatpassword)) {

                firebaseAuth.createUserWithEmailAndPassword(email,password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()){
                            final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            String userId= user.getUid();
                            DatabaseReference name= FirebaseDatabase.getInstance().getReference().child("username");
                            name.child(userId).setValue(username);
                            Toast.makeText(SignUp.this, "تم التسجيل بنجاح ", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(SignUp.this, "حدث خطا يرجي التاكد من الاتصال بالانترنت", Toast.LENGTH_SHORT).show();

                        }


                    }
                });




            }else {
                Toast.makeText(SignUp.this, "الرقم السري غير مطابق", Toast.LENGTH_SHORT).show();
            }
        }




    }



    public void init(){

        mEmail=(EditText)findViewById(R.id.editText_register_email);
        mPassword=(EditText)findViewById(R.id.editText_register_password);
        mRepeatPassword=(EditText)findViewById(R.id.editText_register_password2);
        mUsername=(EditText)findViewById(R.id.editText_username);


    }
}
