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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.jam.R;
import hu.ait.jam.data.Match;
import hu.ait.jam.data.Profile;
import hu.ait.jam.touch.TouchHelperAdapter;

public class ProfileAdapter
        extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>
        implements TouchHelperAdapter {

    private Context context;
    private List<Profile> profileList;
    private List<String> profileKeys;
    private String uid;
    private int lastPosition = -1;
    private DatabaseReference profilesRef;

    public ProfileAdapter(Context context, String uid) {
        this.context = context;
        this.uid = uid;
        this.profileList = new ArrayList<Profile>();
        this.profileKeys = new ArrayList<String>();

        profilesRef = FirebaseDatabase.getInstance().getReference("posts");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile tmpProfile = profileList.get(position);
        holder.tvName.setText(tmpProfile.getName());
        holder.tvInstrument.setText(tmpProfile.getInstrument());
        holder.tvYears.setText(tmpProfile.getYears());
        holder.tvGenre.setText(tmpProfile.getGenre());
        holder.tvBio.setText(tmpProfile.getBio());
        holder.tvSearch.setText(tmpProfile.getGoals());

        if (!TextUtils.isEmpty(tmpProfile.getImageUrl())) {
            holder.ivProfile.setVisibility(View.VISIBLE);
            Glide.with(context).load(tmpProfile.getImageUrl()).into(holder.ivProfile);
        } else {
            holder.ivProfile.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        profileList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(profileList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(profileList, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }


    public String getOtherEmail(int position) {
        return profileList.get(position).getEmail();
    }

    public String getOtherName(int position) {
        return profileList.get(position).getName();
    }

    public String getOtherPhone(int position) {
        return profileList.get(position).getPhone();
    }


    public Profile getProfile(String email) {
        Profile profile = new Profile();

        for (int i = 0; i < getItemCount(); i++) {
            Profile user = profileList.get(i);
            String userEmail = user.getEmail();

            if (userEmail.equals(email)) {
                profile = profileList.get(i);
            }
        }

        return profile;
    }


    public ArrayList<Match> getMatches(String email) {
        ArrayList<Match> matches = new ArrayList<>();

        for (int i = 0; i < getItemCount(); i++) {
            Profile user = profileList.get(i);
            String userEmail = user.getEmail();

            if (userEmail.equals(email)) {
                HashMap<String, String> matchList = profileList.get(i).getMatches();

                if (matchList != null) {
                    for (Map.Entry<String, String> entry : matchList.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Match match = new Match(key, value);
                        matches.add(match);
                    }
                }
            }
        }

        return matches;
    }


    public void checkMatch(String currentEmail, String otherEmail,
                           String otherName, String otherPhone) {
        int currentIndex = -1;
        int otherIndex = -1;

        Profile currentUser = null;
        Profile otherUser = null;

        String currentName = "";
        String currentPhone = "";
        
        for (int i = 0; i < getItemCount(); i++) {
            if (profileList.get(i).getEmail().equals(currentEmail)) {
                currentIndex = i;
                currentUser = profileList.get(i);
                currentName = currentUser.getName();
                currentPhone = currentUser.getPhone();
            }

            if (profileList.get(i).getEmail().equals(otherEmail)) {
                otherIndex = i;
                otherUser = profileList.get(i);
            }
        }

        ArrayList<String> usersWhoSwiped = currentUser.getSwipedOnMe();

        if (usersWhoSwiped != null && usersWhoSwiped.contains(otherUser.getEmail())) {
            // it's a match
            HashMap<String, String> getMatches = currentUser.getMatches();

            if (getMatches != null) {
                getMatches.put(otherName, otherPhone);
                currentUser.setMatches(getMatches);
            } else {
                HashMap<String, String> matchList = new HashMap<>();
                matchList.put(otherName, otherPhone);
                currentUser.setMatches(matchList);
            }

            profilesRef.child(profileKeys.get(currentIndex)).setValue(currentUser);

            HashMap<String, String> otherMatches = otherUser.getMatches();

            if (otherMatches != null) {
                otherMatches.put(currentName, currentPhone);
                otherUser.setMatches(otherMatches);
            } else {
                HashMap<String, String> matchList = new HashMap<>();
                matchList.put(currentName, currentPhone);
                otherUser.setMatches(matchList);
            }

            profilesRef.child(profileKeys.get(otherIndex)).setValue(otherUser);

        } else {
            // not a match...yet...
            ArrayList<String> getSwiped = otherUser.getSwipedOnMe();

            if (getSwiped != null) {
                getSwiped.add(currentEmail);
                otherUser.setSwipedOnMe(getSwiped);
            } else {
                ArrayList<String> swipeList = new ArrayList<>();
                swipeList.add(currentEmail);
                otherUser.setSwipedOnMe(swipeList);
            }

            profilesRef.child(profileKeys.get(otherIndex)).setValue(otherUser);
        }
    }

    public void editProfile(String email) {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (profileList.get(i).getEmail().equals(email)) {
                removePost(i);
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvInstrument;
        public TextView tvYears;
        public TextView tvGenre;
        public TextView tvBio;
        public TextView tvSearch;
        public ImageView ivProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvInstrument = (TextView) itemView.findViewById(R.id.tvInstrument);
            tvYears = (TextView) itemView.findViewById(R.id.tvYears);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenre);
            tvBio = (TextView) itemView.findViewById(R.id.tvBio);
            tvSearch = (TextView) itemView.findViewById(R.id.tvSearch);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
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
        profileList.add(place);
        profileKeys.add(key);
        notifyDataSetChanged();
    }

    public void removePost(int index) {
        profilesRef.child(profileKeys.get(index)).removeValue();
        profileList.remove(index);
        profileKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePostByKey(String key) {
        int index = profileKeys.indexOf(key);
        if (index != -1) {
            profileList.remove(index);
            profileKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

}
