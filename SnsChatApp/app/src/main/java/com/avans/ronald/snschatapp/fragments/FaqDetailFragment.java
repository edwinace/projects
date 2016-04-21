package com.avans.ronald.snschatapp.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.activities.CreateChatActivity;
import com.avans.ronald.snschatapp.models.FAQ;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FaqDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FaqDetailFragment extends Fragment {

    public static final String ANSWER = "answer";
    public static final String TITLE = "title";

    private OnFragmentInteractionListener mListener;
    private TextView title;
    private TextView answer;
    private Bundle bundle;

    public FaqDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faq_detail, container, false);

        title = (TextView) v.findViewById(R.id.faq_answer_title);
        answer = (TextView) v.findViewById(R.id.faq_answer_content);

        bundle = this.getArguments();

        if(bundle != null){
            title.setText(bundle.getString(TITLE));
            answer.setText(bundle.getString(ANSWER));
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void updateView(FAQ faq){
        title.setText(faq.getTitle());
        answer.setText(faq.getAnswer());
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
