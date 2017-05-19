package hu.ait.jam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hu.ait.jam.R;
import hu.ait.jam.data.Match;


public class MatchAdapter
        extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private Context context;
    private List<Match> matchList;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.match_card, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvMatchName.setText(matchList.get(position).getMatchName());
        holder.tvMatchPhone.setText(matchList.get(position).getMatchPhone());
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMatchName;
        private TextView tvMatchPhone;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMatchName = (TextView) itemView.findViewById(R.id.tvMatchName);
            tvMatchPhone = (TextView) itemView.findViewById(R.id.tvMatchPhone);
        }
    }
}
