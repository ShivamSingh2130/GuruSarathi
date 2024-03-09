package com.example.gurusarthi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ChatAlertOpt> mItems;
    private String from;
    ItemAdapterListener listener;

    public MyRecyclerViewAdapter(List<ChatAlertOpt> items,String mfrom,ItemAdapterListener mlistener) {
        mItems = items;
        from = mfrom;
        listener = mlistener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemName.setText(mItems.get(position).getTitle());
        holder.icon.setImageResource(mItems.get(position).getIcon());
        if (from=="chat"){
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.GONE);
        }else {
            holder.add.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.VISIBLE);
        }

        if (mItems.get(position).isAddable()){
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.VISIBLE);
        }else {
            holder.add.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.GONE);
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.onItemChanged(mItems.get(position));
                mItems.get(position).setAddable(true);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemChanged(mItems.get(position));
                mItems.get(position).setRemovable(true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemName;
        public ImageView icon,add,remove;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.item_name);
            icon = itemView.findViewById(R.id.icon);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}