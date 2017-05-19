package hu.ait.jam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import hu.ait.jam.adapter.MatchAdapter;
import hu.ait.jam.data.Match;

public class MatchesActivity extends BaseActivity {

    private ArrayList<Match> matches;
    private MatchAdapter matchRecyclerAdapter;
    private RecyclerView recyclerMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        Intent intent = getIntent();
        matches = (ArrayList<Match>) intent.getSerializableExtra(getString(R.string.matches));

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerMatch = (RecyclerView) findViewById(R.id.recyclerMatch);
        recyclerMatch.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMatch.setLayoutManager(layoutManager);

        matchRecyclerAdapter = new MatchAdapter(this, matches);
        recyclerMatch.setAdapter(matchRecyclerAdapter);

    }
}
