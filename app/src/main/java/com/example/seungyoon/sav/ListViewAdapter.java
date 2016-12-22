package com.example.seungyoon.sav;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by cspclab on 2016-12-05.
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<String> nlist;
    private ArrayList<String> plist;
    private Activity activity;

    // 생성할 클래스
    public ListViewAdapter(Activity activity){
        this.activity = activity;
        nlist = new ArrayList<String>();
        plist = new ArrayList<String>();
    }

    // 리스트에 값을 추가할 메소드
    public void setName(String name, String pName)
    {
        nlist.add(name);
        plist.add(pName);
    }
    @Override
    public int getCount() {
        // 리스트뷰 갯수 리턴
        return nlist.size();
    }

    @Override
    public Object getItem(int position) {
        // 리스트 값 리턴
        return nlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder    holder  = null;
        final int pos = position;
        TextView name;

        // 최초 뷰 생성
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_row, parent, false);
            name    = (TextView) convertView.findViewById(R.id.name_text);

            holder = new ListViewHolder();
            holder.name = name;

            // list values save
            convertView.setTag(holder);
            // 텍스트 보이기
            name.setVisibility(View.VISIBLE);
        }
        else
        {
            // list values get
            holder = (ListViewHolder) convertView.getTag();
            name = holder.name;
        }

        // 리스트 이름 보이기
        name.setText(nlist.get(pos));

        final PackageManager pm = activity.getPackageManager();

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(pm.getLaunchIntentForPackage(plist.get(pos)));
                //Toast.makeText(activity.getApplicationContext(), "선택한 이름:" + nlist.get(pos), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    // list values class
    private class ListViewHolder {
        TextView name;
    }
}
