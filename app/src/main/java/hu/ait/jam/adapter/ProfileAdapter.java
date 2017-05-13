package hu.ait.jam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.jam.R;
import hu.ait.jam.data.Profile;
import hu.ait.jam.touch.TouchHelperAdapter;

public class ProfileAdapter
        extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>
        implements TouchHelperAdapter {

    private Context context;
    private List<Profile> postList;
    private List<String> postKeys;
    private String uId;
    private int lastPosition = -1;
    private DatabaseReference postsRef;

    public ProfileAdapter(Context context, String uId) {
        this.context = context;
        this.uId = uId;
        this.postList = new ArrayList<Profile>();
        this.postKeys = new ArrayList<String>();

        postsRef = FirebaseDatabase.getInstance().getReference("posts");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile tmpPost = postList.get(position);
        holder.tvName.setText(tmpPost.getName());
        holder.tvInstrument.setText(tmpPost.getInstrument());
        holder.tvYears.setText(tmpPost.getYears());
        holder.tvGenre.setText(tmpPost.getGenre());
        holder.tvBio.setText(tmpPost.getBio());
        holder.tvSearch.setText(tmpPost.getSearch());

        if (!TextUtils.isEmpty(tmpPost.getImageUrl())) {
            holder.ivPost.setVisibility(View.VISIBLE);
            Glide.with(context).load(tmpPost.getImageUrl()).into(holder.ivPost);
        } else {
            holder.ivPost.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        postList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(postList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(postList, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvInstrument;
        public TextView tvYears;
        public TextView tvGenre;
        public TextView tvBio;
        public TextView tvSearch;
        public ImageView ivPost;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvInstrument = (TextView) itemView.findViewById(R.id.tvInstrument);
            tvYears = (TextView) itemView.findViewById(R.id.tvYears);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenre);
            tvBio = (TextView) itemView.findViewById(R.id.tvBio);
            tvSearch = (TextView) itemView.findViewById(R.id.tvSearch);
            ivPost = (ImageView) itemView.findViewById(R.id.ivPost);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public void addPost(Profile place, String key) {
        postList.add(place);
        postKeys.add(key);
        notifyDataSetChanged();
    }

    public void removePost(int index) {
        postsRef.child(postKeys.get(index)).removeValue();
        postList.remove(index);
        postKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePostByKey(String key) {
        int index = postKeys.indexOf(key);
        if (index != -1) {
            postList.remove(index);
            postKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

}
