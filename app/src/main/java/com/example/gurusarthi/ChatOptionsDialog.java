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

        // Create a list of items
        items = getIconList(getActivity());
        if (items.isEmpty()){
            items = new ArrayList<>();
            items.add(new ChatAlertOpt("Tea",R.drawable.tea, false));
            items.add(new ChatAlertOpt("Duster",R.drawable.duster, false));
            items.add(new ChatAlertOpt("Files",R.drawable.files, false));
            items.add(new ChatAlertOpt("Red pen",R.drawable.red_pen, false));
            items.add(new ChatAlertOpt("Laptop Charger",R.drawable.laptop_charger, false));
            items.add(new ChatAlertOpt("Laptop",R.drawable.laptop, false));
            items.add(new ChatAlertOpt("Ac Remote",R.drawable.ac_remote, false));
            items.add(new ChatAlertOpt("Projector Remote",R.drawable.projector_remote, false));
            items.add(new ChatAlertOpt("Thread",R.drawable.threds, false));
            items.add(new ChatAlertOpt("Mini hdmi",R.drawable.mini_hdmi, false));
            items.add(new ChatAlertOpt("Water Bootle",R.drawable.bootle, false));
            items.add(new ChatAlertOpt("chawk",R.drawable.chawk, false));
            items.add(new ChatAlertOpt("HDMI",R.drawable.hdmi, false));
            items.add(new ChatAlertOpt("Blue Pen",R.drawable.blue_pen, false));
            items.add(new ChatAlertOpt("Attendance Sheet",R.drawable.attendance_sheet, false));
            items.add(new ChatAlertOpt("Question Paper",R.drawable.question_paper, false));

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
            if (getActivity() instanceof ChatIconSelectedListner) {
                optionsListener = (ChatIconSelectedListner) getActivity();
            } else {
                throw new ClassCastException(getActivity().toString() + " must implement MyFragmentListener");
            }
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