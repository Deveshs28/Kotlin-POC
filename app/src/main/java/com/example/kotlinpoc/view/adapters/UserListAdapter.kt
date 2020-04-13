package com.example.kotlinpoc.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinpoc.R
import com.example.kotlinpoc.data.model.UserResultModel
import com.example.kotlinpoc.databinding.ListItemBinding
import com.example.kotlinpoc.interfaces.RecyclerClickListener

class UserListAdapter(
    private var userList: ArrayList<UserResultModel>,
    private val itemClickListener: RecyclerClickListener
) :
    RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        textViewName.text = result.name.first
        Glide.with(holder.binding.ivItem.context)
            .load(userList[position].picture?.medium)
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(holder.binding.ivItem)

        holder.bindItems(userList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onClick(position)
        })
    }

    fun updateList(result: ArrayList<UserResultModel>) {
        this.userList = result
        notifyDataSetChanged()
    }

    //the class is holing the list view
    class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var result: UserResultModel
        fun bindItems(result: UserResultModel) {
            this.result = result
            binding.dataModel = result
        }
    }
}