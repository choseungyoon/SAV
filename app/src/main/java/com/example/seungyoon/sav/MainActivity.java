package com.example.seungyoon.sav;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import com.example.seungyoon.sav.naverTalk.NaverTalkActivity;

public class MainActivity extends Activity {

    private Button voiceBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        voiceBtn = (Button)findViewById(R.id.btn_voice);



        voiceBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NaverTalkActivity.class);

                startActivity(intent);

            }

        });

    }
}
