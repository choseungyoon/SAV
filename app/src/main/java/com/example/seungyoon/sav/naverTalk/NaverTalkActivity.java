package com.example.seungyoon.sav.naverTalk;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.example.seungyoon.sav.ListViewAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.seungyoon.sav.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechConfig;
import com.example.seungyoon.sav.R;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NaverTalkActivity extends Activity {

    private static final String CLIENT_ID = "JTvsvnwJm207m96VyhTD"; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private Button btnStart;
    private String mResult;

    private AudioWriterPCM writer;

    private boolean isRunning;

    public static ListViewAdapter listviewadapter;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                txtResult.setText(mResult);
                mResult = mResult.toUpperCase();
                mResult = mResult.trim();
                mResult = mResult.replaceAll(" ","");
                mResult = mResult.replaceAll("\\p{Z}","");
                mResult = mResult.replaceAll("(^\\p{Z}+|\\p{Z}+$)","");
                //Toast.makeText(getApplicationContext(),mResult.toUpperCase(),Toast.LENGTH_SHORT);

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage(mResult).setCancelable(false).setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final PackageManager pm = getPackageManager();
                                List<ApplicationInfo> list = pm.getInstalledApplications(0);
                                ArrayList<String> app_plist = new ArrayList<String>();
                                ArrayList<String> app_nlist = new ArrayList<String>();
                                for (ApplicationInfo applicationInfo : list) {
                                    String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름
                                    String init_name = name;
                                    name = name.toUpperCase();
                                    name = name.trim();
                                    name = name.replaceAll(" ","");
                                    name = name.replaceAll("\\p{Z}","");
                                    name = name.replaceAll("(^\\p{Z}+|\\p{Z}+$)","");
                                    if(name.contains(mResult)) {
                                        String pName = applicationInfo.packageName;
                                        app_nlist.add(init_name);
                                        app_plist.add(pName);
                                    }
                                    //final String pName = applicationInfo.packageName;
            /*
            try {
                PackageInfo packageInfo = pm.getPackageInfo(pName, PackageManager.PERMISSION_DENIED);
                String[] requestedPermissions = packageInfo.requestedPermissions;
                if(requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        Log.d("test", requestedPermissions[i]);
                    }
                }
            }catch (Exception e){

            }
            */
                                    //Intent i = pm.getLaunchIntentForPackage(pName);
                                    //startActivity(i);
                                    //break;
                                    // 'YES'
                                }
                                int list_size = app_nlist.size();
                                switch (list_size){
                                    case 0:
                                        AlertDialog.Builder alert_confirm2 = new AlertDialog.Builder(NaverTalkActivity.this);
                                        alert_confirm2.setMessage("입력한 App이 없습니다. Google Store로 연결할까요?").setCancelable(false).setPositiveButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q="+mResult)));
                                                    }
                                                }).setNegativeButton("아니오",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // 'No'
                                                        return;
                                                    }
                                                });
                                        AlertDialog alert = alert_confirm2.create();
                                        alert.show();
                                        break;
                                    case 1:
                                        startActivity(pm.getLaunchIntentForPackage(app_plist.get(0)));
                                        break;
                                    default:
                                        Activity activity = NaverTalkActivity.this;
                                        listviewadapter = new ListViewAdapter(activity);
                                        for(int i = 0 ; i < list_size ; i++){
                                            listviewadapter.setName(app_nlist.get(i),app_plist.get(i));
                                        }
                                        // 뷰 호출
                                        View view = activity.getLayoutInflater().inflate(R.layout.list_view, null);
                                        // 해당 뷰에 리스트뷰 호출
                                        ListView listview = (ListView)view.findViewById(R.id.listview);
                                        // 리스트뷰에 어뎁터 설정
                                        listview.setAdapter(listviewadapter);

                                        // 다이얼로그 생성
                                        AlertDialog.Builder listViewDialog = new AlertDialog.Builder(activity);
                                        // 리스트뷰 설정된 레이아웃
                                        listViewDialog.setView(view);
                                        // 확인버튼
                                        listViewDialog.setPositiveButton("확인", null);
                                        // 타이틀
                                        //listViewDialog.setTitle("Application List");
                                        // 다이얼로그 보기
                                        listViewDialog.show();
                                        break;
                                }
                                return;
                            }
                        }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart = (Button) findViewById(R.id.btn_voice);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID, SPEECH_CONFIG);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setText(R.string.str_listening);
                    isRunning = true;

                    naverRecognizer.recognize();
                } else {
                    // This flow is occurred by pushing start button again
                    // when SpeechRecognizer is running.
                    // Because it means that a user wants to cancel speech
                    // recognition commonly, so call stop().
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // initialize() must be called on resume time.
        naverRecognizer.getSpeechRecognizer().initialize();

        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release() must be called on pause time.
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        isRunning = false;
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<NaverTalkActivity> mActivity;

        RecognitionHandler(NaverTalkActivity activity) {
            mActivity = new WeakReference<NaverTalkActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NaverTalkActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}