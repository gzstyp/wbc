package com.fwtai.wbc;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fwtai.tool.IRequest;
import com.fwtai.tool.ToolHttp;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(this);
        findViewById(R.id.webSocket).setOnClickListener(this);
        show = findViewById(R.id.show);
    }

    @Override
    public void onClick(final View v){
        switch(v.getId()){
            case R.id.tv:
                run();
                break;
            case R.id.webSocket:
                webSocket();
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    void run(){
        new Runnable(){

            @Override
            public void run(){
                ToolHttp.getInstance().requestGet("http://api.fwtai.com/storage/getListData",new IRequest(){
                    @Override
                    public void onSuccess(final String data){
                        Toast.makeText(MainActivity.this,"连接成功:" + data,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(final IOException exception){
                        Toast.makeText(MainActivity.this,"连接失败:" + exception.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.run();
    }

    public void webSocket(){

        new Runnable(){
            @Override
            public void run(){
                try{
                    URI uri = new URI("ws://192.168.3.108:82/imserver/30");
                    WebSocketClient webSocketClient = new WebSocketClient(uri){

                        @Override
                        public void onOpen(ServerHandshake serverHandshake){
                            show.setText("链接成功。。。");
                        }

                        @Override
                        public void onMessage(String s){
                            show.setText("收到消息:" + s);
                        }

                        @Override
                        public void onClose(int i,String s,boolean b){
                            show.setText("退出链接！");
                        }

                        @Override
                        public void onError(Exception e){
                            show.setText("链接错误:" + e.getMessage());
                            e.printStackTrace();
                        }
                    };
                    webSocketClient.connect();
                    show.setText("链接打开。。。");
                }catch(URISyntaxException e){
                    e.printStackTrace();
                }
            }
        }.run();
    }
}