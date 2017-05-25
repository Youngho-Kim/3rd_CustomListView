package com.veryworks.android.customlistview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DATA_KEY = "position";
    public static final String DATA_RES_ID = "resid";
    public static final String DATA_TITLE = "title";

    ArrayList<Data> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. 데이터
        datas = Loader.getData(this);
        // 2. 아답터
        CustomAdapter adapter = new CustomAdapter(datas, this);
        // 3. 연결
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                Data data = datas.get(position);

                intent.putExtra(DATA_KEY, position);
                intent.putExtra(DATA_RES_ID, data.resId);
                intent.putExtra(DATA_TITLE, data.title);

                startActivity(intent);
            }
        });
    }
}

class CustomAdapter extends BaseAdapter {
    // 1. 데이터
    ArrayList<Data> datas;
    // 2. 인플레이터
    LayoutInflater inflater;

    public CustomAdapter(ArrayList<Data> datas, Context context) {
        this.datas = datas;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. 컨버터뷰를 체크해서 null 일경우만 item layout 을 생성해준다
        Holder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        // 2. 내 아이템에 해당하는 데이터를 세팅해준다.
        Data data = datas.get(position);
//        ((TextView) convertView.findViewById(R.id.txtNo)).setText(data.no + "");
//        ((TextView) convertView.findViewById(R.id.txtTitle)).setText(data.title);
        holder.setNo(data.no);
        holder.setTitle(data.title);
        holder.setImage(data.resId);

        return convertView;
    }

    class Holder {
        TextView no;
        TextView title;
        ImageView image;

        public Holder(View view) {
            no = (TextView) view.findViewById(R.id.txtNo);
            title = (TextView) view.findViewById(R.id.txtTitle);
            image = (ImageView) view.findViewById(R.id.detail);
            // 1. 이미지뷰에 onclick listener 를 달고 상세페이지로 이동시킨다.
            // 2. 타이틀 텍스트뷰에 onclick listener 를 달고 Toast 로 내용을 출력한다.
        }

        public void setNo(int no){
            this.no.setText(no + "");
        }

        public void setTitle(String title){
            this.title.setText(title);
        }

        public void setImage(int resId){
            this.image.setImageResource(resId);
        }

    }
}


class Loader {
    public static ArrayList<Data> getData(Context context){
        ArrayList<Data> result = new ArrayList<>();
        for(int i=1 ; i<=10 ; i++){
            Data data = new Data();
            data.no = i;
            data.title = "자동차";

            data.setImage("car"+i, context);

            result.add(data);
        }
        return result;
    }
}

class Data {
    public int no;
    public String title;
    public String image;
    public int resId;

    public void setImage(String str, Context context){
        image = str;
        // 문자열로 리소스 아이디 가져오기
        resId = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());
    }
}