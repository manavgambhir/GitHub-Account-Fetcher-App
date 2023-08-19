package com.example.apicalljson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class UserAdapter(val gitHubUsers: ArrayList<Users>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(gitHubUsers[position])
    }

    override fun getItemCount(): Int = gitHubUsers.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(users: Users){
            itemView.tvLogin.text = users.login
            itemView.tvID.text = users.id.toString()
            itemView.tvScore.text = users.score.toString()
            itemView.tvUrl.text = users.html_url
            Picasso.get().load(users.avatar_url).into(itemView.imgProfile)
        }
    }
}