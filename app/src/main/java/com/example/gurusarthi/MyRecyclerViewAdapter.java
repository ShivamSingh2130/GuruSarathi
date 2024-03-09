package com.example.gurusarthi;

import static com.example.gurusarthi.SharedPref.saveIconList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ChatAlertOpt> mItems;
    private String from;
    ItemAdapterListener listener;
    FragmentActivity activity;

    public MyRecyclerViewAdapter(FragmentActivity mactivity, List<ChatAlertOpt> items, String mfrom, ItemAdapterListener mlistener) {
        mItems = items;
        from = mfrom;
        listener = mlistener;
        activity = mactivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (from.equals("chat")&&mItems.get(position).isAddable()){
            holder.addButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);

            holder.mItemName.setText(mItems.get(position).getTitle());
            holder.icon.setImageResource(mItems.get(position).getIcon());

        }else {
            if (mItems.get(position).isAddable()){
                holder.addButton.setVisibility(View.GONE);
                holder.removeButton.setVisibility(View.VISIBLE);
            }else {
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.GONE);
            }
            holder.mItemName.setText(mItems.get(position).getTitle());
            holder.icon.setImageResource(mItems.get(position).getIcon());
        }

        // Set click listeners for add and remove buttons
        holder.addButton.setOnClickListener(v -> {
            mItems.get(position).setAddable(true);
            notifyItemChanged(position);
            listener.onItemChanged(true,position);
        });
        holder.removeButton.setOnClickListener(v -> {
            mItems.get(position).setAddable(false);
            notifyItemChanged(position);
            listener.onItemChanged(false,position);
        });
        holder.listContainer.setOnClickListener(v -> {
            listener.onItemChanged(false,position);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemName;
        public LinearLayout listContainer;
        public ImageView icon,addButton,removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.item_name);
            icon = itemView.findViewById(R.id.icon);
            addButton = itemView.findViewById(R.id.add);
            removeButton = itemView.findViewById(R.id.remove);
            listContainer = itemView.findViewById(R.id.listContainer);
        }
    }

}