package com.example.tricyclehailigsytemfinalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
     //('first_name','middle_name','last_name','address','email','gender','password','image')
    EditText first_name,middle_name,last_name,addres,email,password,RePassword,user_name;
    RadioButton male_rb;
    String gender;
    Button browse,upload;
    ImageView img;
    Bitmap bitmap;
    String encodeImageString;
    private static final String url="http://10.0.2.2/tricycleHailingSystem/fileupload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        gender="male";
        img=(ImageView)findViewById(R.id.img);
        upload=(Button)findViewById(R.id.upload);
        browse=(Button)findViewById(R.id.browse);
        male_rb=(RadioButton) findViewById(R.id.male_rb);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Signup.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploaddatatodb();
            }
        });



    }
    public void backtoLogin(){
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }
//EditText first_name,middle_name,last_name,addres,email,password;
    private void uploaddatatodb() {

        if (male_rb.isChecked())
            gender = "male";
        else
            gender = "Female";

        gender = "male";
        first_name = (EditText) findViewById(R.id.first_name);
        middle_name = (EditText) findViewById(R.id.middle_name);
        last_name = (EditText) findViewById(R.id.last_name);
        addres = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        user_name= (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.editTextTextPassword);



        final String f_name = first_name.getText().toString().trim();
        final String m_name = middle_name.getText().toString().trim();
        final String mid_name = last_name.getText().toString().trim();
        final String address = addres.getText().toString().trim();
        final String emailA = email.getText().toString().trim();
        final String user_nameA = user_name.getText().toString().trim();
        final String passwordA = password.getText().toString().trim();

        ///if (password.equals(RePassword)) {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    first_name.setText("");
                    middle_name.setText("");
                    last_name.setText("");
                    addres.setText("");
                    email.setText("");
                    user_name.setText("");
                    password.setText("");

                    img.setImageResource(R.drawable.ic_launcher_foreground);
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    if(response.toString().trim().equals("Account Successfully Created"))
                    backtoLogin();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                  Map<String, String> map = new HashMap<String, String>();
                    map.put("t1", f_name);
                    map.put("t2", m_name);
                    map.put("t3", mid_name);
                    map.put("t4", address);
                    map.put("t5", emailA);
                    map.put("t6", gender);
                    map.put("t7", passwordA);
                    map.put("upload", encodeImageString);
                    map.put("user_name", user_nameA);
                    return map;

                }

            };


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);

    }

}
