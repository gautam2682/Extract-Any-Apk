package com.example.gautam.allinonelauncher;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import  android.content.pm.ApplicationInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gautam on 29/12/16.
 */

public class NerdLauncherFragment extends Fragment {
    List<ApplicationInfo> applicationInfo;
     static  NerdAdapter nerdAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.nerd_launcher_fragment,container,false);

        try {
            setupAdapter(view);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);

        return view;
    }

    private void setupAdapter(View view) throws PackageManager.NameNotFoundException {
        Intent startupintent=new Intent(Intent.ACTION_MAIN);
        startupintent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm=getActivity().getPackageManager();
        final List<ResolveInfo> activities=pm.queryIntentActivities(startupintent,0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {

                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),b.loadLabel(pm).toString());
            }
        });
        Log.d("TAG", String.valueOf(activities.size()));
        applicationInfo=getActivity().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        RecyclerView recycler=(RecyclerView)view.findViewById(R.id.recyler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        nerdAdapter=new NerdAdapter(applicationInfo);

        recycler.setAdapter(nerdAdapter);

        Toast.makeText(getActivity(),"Total apk "+applicationInfo.size(),Toast.LENGTH_LONG).show();

    }



}
