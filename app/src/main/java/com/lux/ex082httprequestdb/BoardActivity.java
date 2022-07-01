package com.lux.ex082httprequestdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.lux.ex082httprequestdb.databinding.ActivityBoardBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    ActivityBoardBinding binding;
    ArrayList<Item> items=new ArrayList<>();
    BoardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_board);
        binding=ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new BoardAdapter(this,items);
        binding.recyclerview.setAdapter(adapter);

        //swipeRefreshLayout이 발동하는 것을 듣는 리스너
        binding.layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //서버 데이터를 다시 읽어오기
                loadData();

                //둥글게 도는 아이콘 사라지기
                binding.layoutRefresh.setRefreshing(false);
            }
        });

        //서버 DB의 데이터를 읽어와서 items 에 추가하는 기능 메소드 호출
        loadData();
    }

    //서버 DB의 데이터를 읽어와서 items 에 추가하는 기능 메소드
    void loadData(){
        //우선 테스트 목적으로 가상의 Item 객체 하나 추가
//        items.add(new Item(1,"aaa","kkk","2022"));
//        adapter.notifyDataSetChanged();

        items.clear();

        //서버에서 DB값을 echo 시켜주는 php 문서 실행
        new Thread(){
            @Override
            public void run() {
                String serverUrl="http://sens0104.dothome.co.kr/03AndroidHttpRequest/loadDB.php";

                try {
                    URL url=new URL(serverUrl);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);

                    StringBuffer buffer=new StringBuffer();
                    while (true){
                        String line=reader.readLine();
                        if (line==null) break;
                        buffer.append(line+"\n");
                    }

                    //우선은 잘 읽어왔는지 확인해보기 위해 다이얼로그로 글씨 보여주기
//                    runOnUiThread(()->{
//                        new AlertDialog.Builder(BoardActivity.this).setMessage(buffer.toString()).create().show();
//                    });

                    //서버에서 echo된 문자열 데이터에서 '&'문자를 기준으로 문자열을 분리
                    String[] rows=buffer.toString().split("&");

                    for(String row : rows){

                        //한 줄(row) 안에 ',' 구분자로 구분된 값들이 여러개(4개)
                        String[] datas=row.split(",");
                        if (datas.length!=4) continue;

                        int no=Integer.parseInt(datas[0]);
                        String title=datas[1];
                        String msg=datas[2];
                        String date=datas[3];

                        items.add(0,new Item(no, title, msg, date));
                    }//for
                    runOnUiThread(()->adapter.notifyDataSetChanged());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}