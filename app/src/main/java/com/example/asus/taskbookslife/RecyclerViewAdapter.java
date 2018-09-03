package com.example.asus.taskbookslife;

/**
 * Created by WILANDA on 16/8/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<ClassBuku> mData ;


    public RecyclerViewAdapter(Context mContext, List<ClassBuku> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.itembooks,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.judul.setText(mData.get(position).getName());

        Picasso.with(this.mContext).load(this.mData.get(position).getCover()).
               noFade().into(holder.gambar);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,BukuActivity.class);

                // passing data to the book activity
                intent.putExtra("name",mData.get(position).getName());

                intent.putExtra("guid",mData.get(position).getGuid());

            intent.putExtra("pdf",mData.get(position).getPdf());
               intent.putExtra("id",mData.get(position).get_id());
               intent.putExtra("writer",mData.get(position).getWriter());
               intent.putExtra("cover",mData.get(position).getCover());
                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView judul;
        ImageView gambar;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            gambar= (ImageView) itemView.findViewById(R.id.gambar);
            judul = (TextView) itemView.findViewById(R.id.judul);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }


}
