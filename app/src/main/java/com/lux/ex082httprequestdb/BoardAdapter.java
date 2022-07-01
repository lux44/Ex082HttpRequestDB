package com.lux.ex082httprequestdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lux.ex082httprequestdb.databinding.RecyclerItemBinding;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.VH> {

    Context context;
    ArrayList<Item> items;

    public BoardAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.recycler_item,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item=items.get(position); //현재번째 아이템 얻어오기

        holder.binding.tvNo.setText(item.no+"");
        holder.binding.tvTitle.setText(item.title);
        holder.binding.tvMsg.setText(item.msg);
        holder.binding.tvDate.setText(item.date);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        //recycler_item.xml과 연결된 binding 클래스
        RecyclerItemBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            //만들어진 cardView(=itemView)와 연결
            binding=RecyclerItemBinding.bind(itemView);

        }
    }
}
