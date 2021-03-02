package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bukcoja.seung.receipt_inmyhand.ListVO.ListVO;
import com.bukcoja.seung.receipt_inmyhand.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListVO> list=new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_custom_menu, parent, false);
        }

        final TextView groupname = (TextView) convertView.findViewById(R.id.group) ;

        ListVO listViewItem = list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        groupname.setText(listViewItem.getGroupName());

        //리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, groupname.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_custom_menu, parent, false);
         }

        final TextView groupname = (TextView) convertView.findViewById(R.id.group) ;

        ListVO listViewItem = list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        groupname.setText(listViewItem.getGroupName());

        //리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View view) {
                Toast.makeText(context, groupname.getText().toString(), Toast.LENGTH_SHORT).show();
               }
          });

        return convertView;

    }
    public void addVO(int groupid,String groupname){
        ListVO item=new ListVO();

        item.setGroup(groupid);
        item.setGroupName(groupname);

        list.add(item);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


}
