package com.example.roomdb.fragments.add.list.ListAdapter

import android.graphics.BitmapFactory
import android.media.Image
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.R
import com.example.roomdb.fragments.add.list.ListFragment.ListFragmentDirections
import com.example.roomdb.model.User

class ListAdapter(): RecyclerView.Adapter<ListAdapter.mYViewHolder>(){

    private var userList = emptyList<User>();

    class mYViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mYViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false);
        return mYViewHolder(view);

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: mYViewHolder, position: Int) {
        var item = userList[position];
        val bitmap = item.image?.let { BitmapFactory.decodeByteArray(item.image, 0, it.size) };

        holder.itemView.findViewById<TextView>(R.id.id_text).text = item.id.toString();
        holder.itemView.findViewById<ImageView>(R.id.showProfile).setImageBitmap(bitmap);
        holder.itemView.findViewById<TextView>(R.id.firstNameTxt).text = item.firstName;
        holder.itemView.findViewById<TextView>(R.id.Location).text = item.lastName;
        holder.itemView.findViewById<TextView>(R.id.ageTxt).text = item.age.toString();

//

        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item);
            holder.itemView.findNavController().navigate(action);
        }


    }

    fun setData(user: List<User>) {
        this.userList = user;
        notifyDataSetChanged();
    }

}

