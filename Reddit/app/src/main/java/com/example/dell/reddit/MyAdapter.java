package com.example.dell.reddit;

/**
 * Created by dell on 15-07-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import Databases.MyHelper;

import static Databases.ContractClass.SearchHistoryTable.COMMENTS;
import static Databases.ContractClass.SearchHistoryTable.KIND;
import static Databases.ContractClass.SearchHistoryTable.TABLE_NAME;
import static Databases.ContractClass.SearchHistoryTable.TIMESTAMP;
import static Databases.ContractClass.SearchHistoryTable.TITLE;
import static com.example.dell.reddit.Utils.postDetailsArrayList;


class MyAdapter extends android.support.v7.widget.RecyclerView.Adapter<MyAdapter.Holder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<PostDetails> postDetailsArrayListRecyclerView;
    RecyclerView recyclerView;
    MyAdapter(Context context, Cursor cursor, RecyclerView recyclerView) {
        postDetailsArrayListRecyclerView = new ArrayList<>();
        postDetailsArrayListRecyclerView = MainActivity.postDetailsArrayList;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    void Refresh() {
        postDetailsArrayListRecyclerView = MainActivity.postDetailsArrayList;
        MyAdapter.this.notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_row,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Picasso.with(context).load(postDetailsArrayListRecyclerView.get(position).thumbnail).into(holder.thumbnail_imageview);
        holder.kind_textview.setText(postDetailsArrayListRecyclerView.get(position).kind);
        holder.title_textview.setText(postDetailsArrayListRecyclerView.get(position).title);
        holder.comments_textview.setText(""+ postDetailsArrayListRecyclerView.get(position).number_of_comments);
        holder.created_utc_timestamp_textview.setText(""+ postDetailsArrayListRecyclerView.get(position).created_utc_timestamp);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //Log.d("xyz",details.get(details_size-1-position).url+" "+details.get(details_size-1-position).name);
        if(position == MainActivity.postDetailsArrayList.size()-1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // DO your work here
                    // get the data
                    Utils.EstablishUrlConnection(1);
                }
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        Log.d("xyz",""+postDetailsArrayListRecyclerView.size());
        return postDetailsArrayListRecyclerView.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView kind_textview;
        TextView title_textview;
        TextView comments_textview;
        TextView created_utc_timestamp_textview;
        ImageView thumbnail_imageview;

        Holder(View itemView) {
            super(itemView);
            kind_textview = (TextView) itemView.findViewById(R.id.kind);
            comments_textview = (TextView) itemView.findViewById(R.id.number_of_comments);
            created_utc_timestamp_textview = (TextView) itemView.findViewById(R.id.created_utc_timestamp);
            title_textview = (TextView) itemView.findViewById(R.id.post_title);
            thumbnail_imageview = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

//    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Utils.EstablishUrlConnection();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Refresh();
//            MyHelper helper = new MyHelper(context);
//            SQLiteDatabase database = helper.getWritableDatabase();
//            int n = Utils.postDetailsArrayList.size();
//            ArrayList<PostDetails> postDetails = new ArrayList<>();
//            postDetails = Utils.postDetailsArrayList;
//            for(int i=0;i<n;i++) {
//                ContentValues contentValues = new ContentValues();
//                PostDetails temp = postDetailsArrayList.get(i);
//                contentValues.put(KIND,temp.kind);
//                contentValues.put(TITLE,temp.title);
//                contentValues.put(TIMESTAMP,temp.created_utc_timestamp);
//                contentValues.put(COMMENTS,temp.number_of_comments);
//                database.insert(TABLE_NAME,null,contentValues);
//            }
//            database.close();
//        }
//    }
}
