package com.example.gurusarthi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatOptionsDialog extends BottomSheetDialogFragment {

  private String from ;
    private  List<ChatAlertOpt> items = new ArrayList<>();
    public static ChatOptionsDialog newInstance(String mfrom) {
        ChatOptionsDialog fragment = new ChatOptionsDialog();
        Bundle args = new Bundle();
        args.putString("mfrom", mfrom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the bottom sheet
        View view = inflater.inflate(R.layout.activity_dialog, container, false);
        if (getArguments() != null) {
             from = getArguments().getParcelable("mfrom");
            // Do something with the item
        }
        // Create a list of items

        items.add(new ChatAlertOpt("Duster",R.drawable.chat, false, false));
        items.add(new ChatAlertOpt("Duster",R.drawable.chat, false, false));
        items.add(new ChatAlertOpt("Duster",R.drawable.chat, false, false));
        items.add(new ChatAlertOpt("Duster",R.drawable.chat, false, false));

        // Find the RecyclerView in the layout
        RecyclerView recyclerView = view.findViewById(R.id.optionsListView);

        // Create an adapter for the list of items
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(items,from, new ItemAdapterListener() {
            @Override
            public void onItemChanged(ChatAlertOpt mitems) {
                saveItemsToSharedPreferences(getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE));
            }
        });

        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(adapter);

        // Use a LinearLayoutManager to display the items in a vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
    public List<ChatAlertOpt> getItemsFromSharedPreferences(SharedPreferences sharedPreferences) {
        // 1. Create a SharedPreferences object
        String json = sharedPreferences.getString("items", null);

        // 2. Retrieve the saved string
        if (json == null) {
            return new ArrayList<>();
        }

        // 3. Parse the JSONArray from the retrieved string
        List<ChatAlertOpt> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                int icon = jsonObject.getInt("icon");
                boolean isAddable = jsonObject.getBoolean("isAddable");
                boolean isRemovable = jsonObject.getBoolean("isRemovable");
                ChatAlertOpt item = new ChatAlertOpt(title, icon, isAddable, isRemovable);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }
    public void saveItemsToSharedPreferences(SharedPreferences sharedPreferences) {
        // Convert the list to a JSONArray
        JSONArray jsonArray = new JSONArray();
        for (ChatAlertOpt item : items) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", item.getTitle());
                jsonObject.put("icon", item.getIcon());
                jsonObject.put("isAddable", item.isAddable());
                jsonObject.put("isRemovable", item.isRemovable());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        // Save the JSONArray to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("items", jsonArray.toString());
        editor.apply();
    }
}