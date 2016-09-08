package com.gii.insreport;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timur_hnimdvi on 04-Sep-16.
 */
public class Directories {

    private static String TAG = "Directories.java";
    boolean loaded = false;
    //ArrayList<Directory> dirs = new ArrayList<>();
    Map<String,Directory> map = new HashMap<>();

    public Directories() {
        Query queryRef = InsReport.ref.child("dirs/");
        queryRef.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e(TAG, "onDataChange: got directories, processing...");
                        int i  =0;
                        //directoryItems.clear();
                        //map = snapshot.getValue(map.getClass());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Log.e(TAG, "next directory: " + postSnapshot.getKey());
                            Directory newDir = new Directory();
                            for (DataSnapshot dataSnapshot : postSnapshot.getChildren()) {
                                //Log.e(TAG, "directory details: " +
                                //dataSnapshot.getKey() + "/" + dataSnapshot.getValue() );
                                DirectoryItem newDirItem = new DirectoryItem();
                                Map<String,String> anItem = new HashMap<String, String>();
                                anItem = dataSnapshot.getValue(anItem.getClass());
                                newDirItem.name = anItem.get("name");
                                newDirItem.id = dataSnapshot.getKey();
                                newDirItem.status = anItem.get("status");
                                newDir.items.add(newDirItem);
                            }
                            //newDir.itemMap = postSnapshot.getValue(newDir.itemMap.getClass());
                            //newDir.mapToArrayList();
                            newDir.name = postSnapshot.getKey();
                            Collections.sort(newDir.items, new Comparator<DirectoryItem>() {
                                @Override
                                public int compare(DirectoryItem o, DirectoryItem t1) {
                                    return (o.name.compareTo(t1.name));
                                }
                            });
                            map.put(postSnapshot.getKey(),newDir);
                        }

                        Log.e(TAG,"Got new directories: " + map.size());
                        if (map.get("DCT_CAUSE_ID") != null) {
                            Log.e(TAG, "onDataChange: SAMPLE:" + map.get("DCT_CAUSE_ID").name);
                            //ArrayList<DirectoryItem> directoryItems = InsReport.directories.map.get("DCT_CAUSE_ID").items;
                            for (DirectoryItem dct_cause_id : map.get("DCT_CAUSE_ID").items) {
                                Log.e(TAG, "dct_cause_id: " + dct_cause_id.id + "/" + dct_cause_id.name);
                            }
                        }
                        loaded = true;
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read failed 133: " + firebaseError.getMessage());
                    }
                });
    }

}
