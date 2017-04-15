package com.example.gautam.allinonelauncher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 30/12/16.
 */

public class NerdAdapter extends RecyclerView.Adapter<NerdAdapter.ViewHolder> {
    List<ApplicationInfo> applicationInfos,list_original=new ArrayList<>();String search_pattern;


    public NerdAdapter(List<ApplicationInfo> applicationInfos){
        this.applicationInfos=applicationInfos;
        list_original.addAll(this.applicationInfos);

    }
    @Override
    public NerdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listfrag,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NerdAdapter.ViewHolder holder, int position) {
        holder.textView.setText(applicationInfos.get(position).sourceDir);
        holder.imageView.setImageDrawable(applicationInfos.get(position).loadIcon(holder.pm));

    }

    @Override
    public int getItemCount() {
        return applicationInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PackageManager pm;
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageVeiw);
            textView=(TextView)itemView.findViewById(R.id.textView);
            pm=itemView.getContext().getPackageManager();
            cardView=(CardView)itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            Context context=view.getContext();
            Extractor extractor=new Extractor();
            try {
                String path=extractor.extractWithoutRoot(applicationInfos.get(pos));
                Toast.makeText(context,"Apk extracted from " +path , Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSearchPattern(String pattern) {
        Log.d("Search pattern","Starting");
        search_pattern = pattern.toLowerCase();
        filterListByPattern();
        this.notifyDataSetChanged();
    }

    private void filterListByPattern() {
        applicationInfos.clear();
        for (ApplicationInfo info : list_original) {
            boolean add = true;
            do {
                if (search_pattern == null || search_pattern.isEmpty()) {
                    break;// empty search pattern: add everything
                }
                if (info.packageName.toLowerCase().contains(search_pattern)) {
                    Log.d("Search pattern","Search found");
                    break;// search in package name

                }

                add = false;
            } while (false);
            Log.d("Search pattern","Searching");
            if (add) applicationInfos.add(info);
        }
    }

    public void filter(String text) {
        applicationInfos.clear();
        if(text.isEmpty()){
            applicationInfos.addAll(list_original);
            Log.d("Search pattern","Searching");
        } else{
            text = text.toLowerCase();
            for(ApplicationInfo item: list_original){
                if(item.sourceDir.toLowerCase().contains(text)){
                    applicationInfos.add(item);
                    Log.d("Search pattern","Searching 123 ");
                }
            }
        }
        this.notifyDataSetChanged();
    }
}
