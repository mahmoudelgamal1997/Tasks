package com.example2017.android.tasks.SignUP;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example2017.android.tasks.Mandob_Map;
import com.example2017.android.tasks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    EditText mEmail,mPassword,mRepeatPassword,mUsername,mMobile;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    Uri uri;
    int gallery_intent=2;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth=FirebaseAuth.getInstance();

        init();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,gallery_intent);
            }
        });

    }

    public void Register(View v){
        String email=mEmail.getText().toString().toLowerCase().trim();
        String password=mPassword.getText().toString().toLowerCase().trim();
        String repeatpassword=mRepeatPassword.getText().toString().toLowerCase().trim();
        final String username=mUsername.getText().toString().toLowerCase().trim();


        if ( TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatpassword) || TextUtils.isEmpty(username) || uri == null){
            if (uri==null)
            {Toast.makeText(SignUp.this, "قم باختيار صوره", Toast.LENGTH_SHORT).show();}
            else
            {Toast.makeText(SignUp.this, "من فضلك املأ الفراغات كامله", Toast.LENGTH_SHORT).show();}
        }else {
            if (password.equals(repeatpassword)) {

                firebaseAuth.createUserWithEmailAndPassword(email,password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            DatabaseReference name = FirebaseDatabase.getInstance().getReference().child("username");
                            name.child(userId).child("name").setValue(username);
                            uploadProfileImage(storageReference, uri, name);
                            Toast.makeText(SignUp.this, "تم التسجيل بنجاح ", Toast.LENGTH_SHORT).show();

                            Intent i=new Intent(SignUp.this,Mandob_Map.class);
                            startActivity(i);
                            finish();

                        }else{
                            Toast.makeText(SignUp.this, task.getException().getMessage()
                                    , Toast.LENGTH_SHORT).show();

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
        imageView=(ImageView)findViewById(R.id.user_profile_image);
        storageReference= FirebaseStorage.getInstance().getReference();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap resized=null;
        if(resultCode != RESULT_CANCELED) {

            if (requestCode == gallery_intent && resultCode == RESULT_OK) {
                uri = data.getData();
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                resized = Bitmap.createScaledBitmap(bitmap, 100, 120, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(resized);

        }
    }

    void uploadProfileImage(StorageReference s, Uri imageUri, final DatabaseReference databaseReference){
        StorageReference filepath=s.child("ProfileImage").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri down=taskSnapshot.getDownloadUrl();
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.child(firebaseUser.getUid().toString()).child("ProfileImage").setValue(down.toString());

            }
        });
    }
}
