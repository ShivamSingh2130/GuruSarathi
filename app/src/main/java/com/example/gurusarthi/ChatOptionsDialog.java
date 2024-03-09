package com.example.gurusarthi;

import static com.example.gurusarthi.SharedPref.getIconList;
import static com.example.gurusarthi.SharedPref.saveIconList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatOptionsDialog extends BottomSheetDialogFragment {

  private String from ;
  private ChatIconSelectedListner optionsListener ;
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
             from = getArguments().getString("mfrom");
            // Do something with the item
        }
        if (getActivity() instanceof ChatIconSelectedListner) {
            optionsListener = (ChatIconSelectedListner) getActivity();
        } else {
            throw new ClassCastException(getActivity().toString() + " must implement MyFragmentListener");
        }
        // Create a list of items
        items = getIconList(getActivity());
        if (items.isEmpty()){
            items.add(new ChatAlertOpt("Duster1",R.drawable.chat, false));
            items.add(new ChatAlertOpt("Duster2",R.drawable.chat, false));
            items.add(new ChatAlertOpt("Duster3",R.drawable.chat, false));
            items.add(new ChatAlertOpt("Duster4",R.drawable.chat, false));
            saveIconList(getActivity(),items);
        }else {
           items = getIconList(getActivity());
        }


        // Find the RecyclerView in the layout
        RecyclerView recyclerView = view.findViewById(R.id.optionsListView);
        EditText location = view.findViewById(R.id.address);
        location.setVisibility((from.equals("chat")) ? View.VISIBLE : View.GONE);

        // Create an adapter for the list of items
        List<ChatAlertOpt> filteredList ;
        if (from.equals("chat")){

            filteredList = items.stream().filter(chatAlertOpt -> chatAlertOpt.isAddable())
                    .collect(Collectors.toList());

        }else {
            filteredList = items;
        }
        MyRecyclerViewAdapter madapter = new MyRecyclerViewAdapter(getActivity(),filteredList,from, new ItemAdapterListener() {
            @Override
            public void onItemChanged(Boolean flag,int pos) {
                if (from.equals("chat")){
                    if (optionsListener != null) {
                        if (location.toString()==""){
                            Toast.makeText(getActivity(), "Enter The Location", Toast.LENGTH_SHORT).show();

                        }else {

                            optionsListener.onOptionsSelected(filteredList.get(pos),location.getText().toString());
                            dismiss();
                        }
                    }

                } else {
                    filteredList.get(pos).setAddable(flag);
                    saveIconList(getActivity(),filteredList);
                }

               items = filteredList;

            }

        });
        Log.i("items",filteredList.toString());
        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(madapter);
        madapter.notifyDataSetChanged();

        // Use a LinearLayoutManager to display the items in a vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}