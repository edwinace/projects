package com.avans.ronald.snschatapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.activities.FaqActivity;
import com.avans.ronald.snschatapp.adapters.FaqAdapter;
import com.avans.ronald.snschatapp.models.Category;
import com.avans.ronald.snschatapp.models.CategoryContainer;
import com.avans.ronald.snschatapp.models.FAQ;
import com.avans.ronald.snschatapp.models.FaqListContainer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FaqListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FaqListFragment extends Fragment {

    private static final String TAG = FaqListFragment.class.getSimpleName();
    private static final String LOREM_IPSUM = "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"";
    private OnFragmentInteractionListener mListener;

    private CategoryContainer categoryContainer;
    private FaqListContainer faqListContainer;
    private FaqAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<FAQ>> listDataChild;


    public FaqListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_faq_list, container, false);

        listAdapter = new FaqAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);


        listAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(listAdapter.getChildrenCount(groupPosition) == 0){
                    Toast.makeText(getActivity().getApplicationContext(), "Er zijn nog geen vragen voor deze categorie", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                mListener.onFaqListInteraction((FAQ) listAdapter.getChild(groupPosition, childPosition));
                mListener.setCurrentCategory(listDataHeader.get(groupPosition));
                return true;
            }
        });

        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getDataFromLocalStorage(){

    }

    public FaqAdapter getFaqAdapter(){
        return this.listAdapter;
    }

    public void updateData(){
        listAdapter.notifyDataSetChanged();
    }

    public void setFaqListContainer(FaqListContainer container){
        this.faqListContainer = container;
        prepareListData();
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<FAQ>>();
        HashMap<String, List<FAQ>> tempList = faqListContainer.getData();
            // Adding child data
            for (String s : faqListContainer.getData().keySet()) {
                listDataHeader.add(s);
                listDataChild.put(s, faqListContainer.getData().get(s));
            }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFaqListInteraction(FAQ faq);
        public void setCurrentCategory(String categoryName);
    }

}
