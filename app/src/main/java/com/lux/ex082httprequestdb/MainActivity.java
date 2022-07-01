package com.lux.ex082httprequestdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;
import com.lux.ex082httprequestdb.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

      binding=ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      binding.btnSave.setOnClickListener(view -> saveData());
      binding.btnLoad.setOnClickListener(view -> loadData());
    }

    void saveData(){

        //소프트 키패드 닫기
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


        new Thread(){
            @Override
            public void run() {
                String title=binding.etTitle.getText().toString();
                String message=binding.etMsg.getText().toString();

                String serverUrl="http://sens0104.dothome.co.kr/03AndroidHttpRequest/insertDB.php";

                try {
                    URL url=new URL(serverUrl);

                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    //보낼 데이터 포맷
                    String data="title="+title +"&msg=" +message;
                    OutputStream os=connection.getOutputStream();
                    PrintWriter writer=new PrintWriter(os);
                    writer.print(data);
                    writer.flush();
                    writer.close();

                    //insertDB.php로부터 DB저장 결과를 응답받아 SnackBar 또는 Toast로 보여주기
                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);

                    StringBuffer buffer=new StringBuffer();
                    while (true){
                        String line=reader.readLine();
                        if (line==null) break;

                        buffer.append(line+"\n");
                    }
                    //snackBar or Toast로 보여주기
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(binding.getRoot(),buffer.toString(),Snackbar.LENGTH_SHORT).show();

                            binding.etTitle.setText("");
                            binding.etMsg.setText("");
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    void loadData(){
        //DB 데이터를 읽어서 보여주는 화면으로 이동 - 리사이클러뷰를 가진 액티비티
        startActivity(new Intent(this,BoardActivity.class));
    }
}