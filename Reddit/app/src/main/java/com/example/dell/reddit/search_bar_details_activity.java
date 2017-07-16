package com.example.dell.reddit;

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
import android.widget.Toast;

import java.util.ArrayList;

import Databases.MyHelper;

import static Databases.ContractClass.SearchHistoryTable.COMMENTS;
import static Databases.ContractClass.SearchHistoryTable.KIND;
import static Databases.ContractClass.SearchHistoryTable.TABLE_NAME;
import static Databases.ContractClass.SearchHistoryTable.TIMESTAMP;
import static Databases.ContractClass.SearchHistoryTable.TITLE;

public class search_bar_details_activity extends AppCompatActivity {

    ListView query_list;
    ArrayList<PostDetails> postDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar_details_activity);

        query_list = (ListView) findViewById(R.id.query_list);
        String query = getIntent().getExtras().getString("query");

        // Cursor
        MyHelper myHelper = new MyHelper(this);
        SQLiteDatabase database = myHelper.getWritableDatabase();
        postDetails = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, new String[]{TITLE, KIND, TIMESTAMP, COMMENTS}, TITLE+"=?", new String[]{query}, null, null, null);
        while (cursor.moveToNext()) {
            postDetails.add(new PostDetails(cursor.getString(cursor.getColumnIndex(KIND)), cursor.getString(cursor.getColumnIndex(TITLE)), cursor.getDouble(cursor.getColumnIndex(TIMESTAMP)), cursor.getInt(cursor.getColumnIndex(COMMENTS)), null));
        }

        QueryAdapter adapter = new QueryAdapter();
        query_list.setAdapter(adapter);

        Toast.makeText(this, postDetails.get(0).title, Toast.LENGTH_SHORT).show();
    }

    class QueryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return postDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return postDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            SearchHistoryViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row, parent, false);
                holder = new search_bar_details_activity.SearchHistoryViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (search_bar_details_activity.SearchHistoryViewHolder) row.getTag();
            }

            PostDetails temp = postDetails.get(position);

            holder.kind.setText(temp.kind);
            holder.comments.setText("" + temp.number_of_comments);
            holder.timestamp.setText("" + temp.created_utc_timestamp);
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

