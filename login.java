package com.example.tricyclehailigsytemfinalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

   
    String[] items= {"Passenger","Driver"};
    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adapterItems;
    EditText userEditText;
    EditText passwordEditText;
    String item;
    String url;
    String userName,id_num;
    static String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        item="Passenger";
        userEditText=(EditText)findViewById(R.id.etUserName);
        passwordEditText=(EditText)findViewById(R.id.etPassword);

        //Drop Down
        adapterItems= new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteText=findViewById(R.id.auto_complete_text);
        autoCompleteText.setAdapter(adapterItems);
        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 item = adapterView.getItemAtPosition(i).toString().trim();
                //Toast.makeText(getApplicationContext(),"Item"+item,Toast.LENGTH_LONG).show();

            }
        });
    }

    public void buLogin(View view) {
        if(userEditText.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Email and Password First!",Toast.LENGTH_LONG).show();
        }
        else {
            if (item.equals("Passenger")) {
                 url = "http://10.0.2.2/tricycleHailingSystem/login_passenger.php?user_name=" + userEditText.getText().toString() + "&password=" + passwordEditText.getText().toString();
            } else if (item.equals("Driver")) {
                url = "http://10.0.2.2/tricycleHailingSystem/login_driver.php?user_name=" + userEditText.getText().toString() + "&password=" + passwordEditText.getText().toString();
            } 
            new MyAsyncTaskgetNews().execute(url);
        }
    }
    public void buRegister(View view) {
        if(item.equals("Passenger")) {
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
        }
        else if(item.equals("Driver")){
            Intent intent = new Intent(this, Signup_driver.class);
            startActivity(intent);
        }
       
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static String getUser() {
        return user;
    }

    public void ShowLoginPage(){

        if (item.equals("Passenger")) {
            Intent intent=new Intent(this,passenger_ui.class);
            intent.putExtra("user_names",userName);
            startActivity(intent);
            finish();
        } else if (item.equals("Driver")) {
            Intent intent=new Intent(this,booking_list.class);
            intent.putExtra("types","driver");
            intent.putExtra("id",id_num);
            startActivity(intent);
            finish();
        }
       
    }

    // get news from server
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {

            // TODO Auto-generated method stub

            try {

                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

                //Toast.makeText(getApplicationContext(),params.toString(),Toast.LENGTH_LONG).show();
            }catch (Exception ex){
                // Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();

            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            boolean showWrong=true;
            try {
                JSONArray json=new JSONArray(progress[0]);
                JSONObject user= json.getJSONObject(0);

                userName=user.getString("user_name").trim();
                id_num=user.getString("id").trim();
                if(json.length()>0){
                    ShowLoginPage();
                    showWrong=false;
                }
             } catch (Exception ex) {
            }
            if(showWrong)
            Toast.makeText(getApplicationContext(),"Wrong Password Or User",Toast.LENGTH_LONG).show();

        }

        protected void onPostExecute(String  result2){

        }

    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }


}
