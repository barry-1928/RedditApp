package com.example.dell.reddit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Databases.MyHelper;

import static Databases.ContractClass.SearchHistoryTable.COMMENTS;
import static Databases.ContractClass.SearchHistoryTable.KIND;
import static Databases.ContractClass.SearchHistoryTable.TABLE_NAME;
import static Databases.ContractClass.SearchHistoryTable.TIMESTAMP;
import static Databases.ContractClass.SearchHistoryTable.TITLE;

public class SearchHistoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<PostDetails> postDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        listView = (ListView) findViewById(R.id.list_view);
        MyHelper myHelper = new MyHelper(this);
        SQLiteDatabase database = myHelper.getWritableDatabase();
        postDetailsArrayList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME,new String[] {TITLE,KIND,TIMESTAMP,COMMENTS},null,null,null,null,null);
        if(cursor!=null) {
            while(cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String kind = cursor.getString(cursor.getColumnIndex(KIND));
                Double timestamp = cursor.getDouble(cursor.getColumnIndex(TIMESTAMP));
                int comments = cursor.getInt(cursor.getColumnIndex(COMMENTS));
                postDetailsArrayList.add(new PostDetails(kind,title,timestamp,comments,null));
            }
        }

        Adapter adapter = new Adapter(this);
        listView.setAdapter(adapter);
    }

    class Adapter extends BaseAdapter {


        Context context;

        Adapter(Context context) {

            this.context = context;

        }

        @Override
        public int getCount() {
            return postDetailsArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return postDetailsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            SearchHistoryViewHolder holder = null;

            if(row == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row,parent,false);
                holder = new SearchHistoryViewHolder(row);
                row.setTag(holder);
            }
            else {
                holder = (SearchHistoryViewHolder) row.getTag();
            }

            PostDetails temp = postDetailsArrayList.get(position);

            holder.kind.setText(temp.kind);
            holder.comments.setText(""+temp.number_of_comments);
            holder.timestamp.setText(""+temp.created_utc_timestamp);
            holder.title.setText(temp.title);
            return row;
        }
    }

    class SearchHistoryViewHolder {

        TextView kind;
        TextView title;
        TextView timestamp;
        TextView comments;


        SearchHistoryViewHolder(View view) {
            kind = (TextView) view.findViewById(R.id.row_kind);
            title = (TextView) view.findViewById(R.id.row_title);
            timestamp = (TextView) view.findViewById(R.id.row_timestamp);
            comments = (TextView) view.findViewById(R.id.row_comments);
        }
    }
}
