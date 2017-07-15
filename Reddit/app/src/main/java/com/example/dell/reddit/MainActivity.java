package com.example.dell.reddit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import Databases.MyHelper;

import static Databases.ContractClass.SearchHistoryTable.COMMENTS;
import static Databases.ContractClass.SearchHistoryTable.KIND;
import static Databases.ContractClass.SearchHistoryTable.TABLE_NAME;
import static Databases.ContractClass.SearchHistoryTable.TIMESTAMP;
import static Databases.ContractClass.SearchHistoryTable.TITLE;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter adapter;
    int i;
    public static ArrayList<PostDetails> postDetailsArrayList;
    public static boolean loading;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyHelper helper = new MyHelper(this);
//        loading = true;
//        SQLiteDatabase database = helper.getWritableDatabase();
//        database.delete(TABLE_NAME,null,null);
        postDetailsArrayList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(0);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));





//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
//            {
//                if(dy > 0) //check for scroll down
//                {
//                    LinearLayoutManager mLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
//                    visibleItemCount = mLayoutManager.getChildCount();
//                    totalItemCount = mLayoutManager.getItemCount();
//                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//                    if(recyclerView.getAdapter(). == recyclerView.getAdapter().getItemCount())
//                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
//                        {
//                            //Do pagination.. i.e. fetch new data
//                            MyAsyncTask myAsyncTask = new MyAsyncTask();
//                            myAsyncTask.execute(1);
//                            adapter.Refresh();
//                        }
//                    }
//                }
//
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    public void search_history_page(MenuItem menuItem) {
        Intent intent = new Intent(MainActivity.this,SearchHistoryActivity.class);
        startActivity(intent);
    }

    class MyAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Utils.EstablishUrlConnection(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            adapter = new MyAdapter(MainActivity.this, null, recyclerView);
            recyclerView.setAdapter(adapter);
            MyHelper helper = new MyHelper(MainActivity.this);
            SQLiteDatabase database = helper.getWritableDatabase();
            int n = Utils.postDetailsArrayList.size();
            ArrayList<PostDetails> postDetails = new ArrayList<>();
            postDetails = Utils.postDetailsArrayList;
            for(int i=0;i<n;i++) {
                ContentValues contentValues = new ContentValues();
                PostDetails temp = postDetailsArrayList.get(i);
                contentValues.put(KIND,temp.kind);
                contentValues.put(TITLE,temp.title);
                contentValues.put(TIMESTAMP,temp.created_utc_timestamp);
                contentValues.put(COMMENTS,temp.number_of_comments);
                database.insert(TABLE_NAME,null,contentValues);
            }
            database.close();
        }
    }
}
