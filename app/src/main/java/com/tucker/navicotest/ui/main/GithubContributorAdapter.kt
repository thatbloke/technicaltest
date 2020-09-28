package com.tucker.navicotest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tucker.navicotest.R

/**
 * Adapter class for our recyclerview
 * Nothing too special in here, except that we're taking advantage of ListAdapter rather than directly implementing RecyclerView.Adapter
 * This means that if we update the list then it runs a diff operation for us automatically and then will notify of any changes automatically
 *
 * @property glide Passing in a Glide RequestManager object here - this is created in the fragment such that any requests for images
 * made by this object will then be tied to that fragment's lifecycle. i.e. if Fragment pauses or stops, any requests here will be paused/cancelled automatically
 */

class GithubContributorAdapter(private val glide: RequestManager):
    ListAdapter<GithubUser, GithubContributorAdapter.GithubContributorViewHolder>(GithubContributorDiffCallback()) {

    //passing in the RequestManager object created from the fragment
    //that means it has the correct context so that if the fragment were to exit, the requests will be auto-cancelled by Glide

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubContributorViewHolder {
        return GithubContributorViewHolder(
            glide,
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GithubContributorViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    /**
     * ViewHolder for the Recyclerview - nothing special here except that we pass each one the Glide RequestManager
     * that we passed in to the adapter
     */

    class GithubContributorViewHolder(private val glide: RequestManager, itemView: View): RecyclerView.ViewHolder(itemView) {

        private var avatarImageView: ImageView = itemView.findViewById(R.id.recycler_item_avatar)
        private var usernameView: TextView = itemView.findViewById(R.id.recycler_item_username)
        private var commitsView: TextView = itemView.findViewById(R.id.recycler_item_commits)

        //set the data to the view
        fun bindData(data: GithubUser) = with(itemView) {
            if (data.avatarURL != null) {
                glide.load(data.avatarURL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatarImageView)
            }

            usernameView.text = this.context.getString(R.string.recycler_item_username, data.username)
            commitsView.text = this.context.getString(R.string.recycler_item_commits, data.numberOfCommits)
        }

    }
}

/**
 * Diff callback class, required for the ListAdapter's diff comparisons
 * Kotlin makes it easy when you use a data class!
 */
class GithubContributorDiffCallback: DiffUtil.ItemCallback<GithubUser>() {
    override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
        return oldItem == newItem
    }
}