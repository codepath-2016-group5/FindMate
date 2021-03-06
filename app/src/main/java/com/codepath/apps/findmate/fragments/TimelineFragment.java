package com.codepath.apps.findmate.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.findmate.R;
import com.codepath.apps.findmate.adapters.TimelineAdapter;
import com.codepath.apps.findmate.interfaces.NotifyActivity;
import com.codepath.apps.findmate.interfaces.ViewPagerFragment;
import com.codepath.apps.findmate.models.CheckIn;
import com.codepath.apps.findmate.models.Group;
import com.codepath.apps.findmate.utils.DividerItemDecoration;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimelineFragment extends Fragment implements ViewPagerFragment {

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    private List<CheckIn> checkIns;
    private Group group;

    private RecyclerView rvCheckIns;
    private TimelineAdapter adapter;

    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lookup the recyclerview in activity layout
        rvCheckIns = (RecyclerView) view.findViewById(R.id.rvCheckIns);

        // Initialize contacts
        checkIns = new ArrayList<>();

        adapter = new TimelineAdapter(getActivity(), checkIns);
        rvCheckIns.setAdapter(adapter);


        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        rvCheckIns.addItemDecoration(itemDecoration);

        rvCheckIns.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onGroupUpdated(Group group) {
        this.group = group;


        List<CheckIn> newCheckins = group.getCheckIns();
        if(newCheckins.size() > checkIns.size()) {
            for(int i=0; i<(newCheckins.size()-checkIns.size()); i++) {
                CheckIn checkin = newCheckins.get(i);
                String name = (String)checkin.getCreator().get("name");
                ((NotifyActivity)this.getActivity()).notifyUser(name, checkin.getPlace().getAddress());
            }
        }

        checkIns.clear();

        checkIns.addAll(newCheckins);

        Collections.reverse(checkIns);

        adapter.notifyDataSetChanged();
    }
}
