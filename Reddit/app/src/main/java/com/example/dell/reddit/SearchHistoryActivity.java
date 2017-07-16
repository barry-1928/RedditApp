package com.example.dell.reddit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
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
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        listView = (ListView) findViewById(R.id.list_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search_history);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        MyHelper myHelper = new MyHelper(this);
        SQLiteDatabase database = myHelper.getWritableDatabase();
        postDetailsArrayList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, new String[]{TITLE, KIND, TIMESTAMP, COMMENTS}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String kind = cursor.getString(cursor.getColumnIndex(KIND));
                Double timestamp = cursor.getDouble(cursor.getColumnIndex(TIMESTAMP));
                int comments = cursor.getInt(cursor.getColumnIndex(COMMENTS));
                postDetailsArrayList.add(new PostDetails(kind, title, timestamp, comments, null));
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

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row, parent, false);
                holder = new SearchHistoryViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (SearchHistoryViewHolder) row.getTag();
            }

            PostDetails temp = postDetailsArrayList.get(position);

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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_history, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setBackgroundColor(Color.parseColor("#ffffff"));

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    loadHistory(query,menu);
                    return true;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
//                    loadHistory(newText);
//                    return true;
                    return false;
                }
            });
        }
        return true;
    }

    public void loadHistory(String query,Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Intent intent = new Intent(this,search_bar_details_activity.class);
            intent.putExtra("query",query);
            startActivity(intent);
            // SearchView
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        }

    }
}


