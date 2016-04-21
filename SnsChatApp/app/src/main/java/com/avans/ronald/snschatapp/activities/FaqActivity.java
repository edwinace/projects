package com.avans.ronald.snschatapp.activities;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.fragments.FaqDetailFragment;
import com.avans.ronald.snschatapp.fragments.FaqListFragment;
import com.avans.ronald.snschatapp.models.FAQ;
import com.avans.ronald.snschatapp.models.FaqListContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.logging.Filter;

public class FaqActivity extends ActionBarActivity implements FaqListFragment.OnFragmentInteractionListener, FaqDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = FaqActivity.class.getSimpleName();
    public static final String FAQ_FILE_NAME = "faq_data";

    private FaqListFragment listFragment;
    private FaqListContainer faqListContainer;
    private FaqListener faqListener;
    private IntentFilter filter;
    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqListener = new FaqListener();
        filter = new IntentFilter("android.intent.action.SYNC_FAQ");

        try {
            faqListContainer = FaqListContainer.deSerializeSelf(openFileInput(FAQ_FILE_NAME));
        }catch (FileNotFoundException e){
            Log.e(TAG, e.getMessage());
        }

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            listFragment = new FaqListFragment();
            listFragment.setFaqListContainer(faqListContainer);

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment).commit();
        }else{
            listFragment = (FaqListFragment)
                    getFragmentManager().findFragmentById(R.id.faq_list_fragment);
            listFragment.setFaqListContainer(faqListContainer);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void askQuestion(View v){
        if(v.getId() == R.id.ask_question_from_faq_button){
            Bundle bundle = new Bundle();
            bundle.putBoolean(CreateChatActivity.COMES_FROM_FAQ, true);
            bundle.putString("category", currentCategory);
            startActivity(new Intent(this, CreateChatActivity.class).putExtras(bundle));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        GcmIntentService.checkRunningMode = false;
        unregisterReceiver(faqListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(faqListener, filter);
        GcmIntentService.checkRunningMode = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFaqListInteraction(FAQ faq) {
        FaqDetailFragment detailFragment =
                (FaqDetailFragment) getFragmentManager().findFragmentById(R.id.faq_detail_fragment);

        if(detailFragment != null){
            detailFragment.updateView(faq);
        }else{
            FaqDetailFragment fragment = new FaqDetailFragment();

            Bundle args = new Bundle();
            args.putString(fragment.TITLE, faq.getTitle());
            args.putString(fragment.ANSWER, faq.getAnswer());

            fragment.setArguments(args);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void setCurrentCategory(String categoryName) {
        currentCategory = categoryName;
        System.out.println(categoryName);
    }


    private class FaqListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //TODO dynamically update data in list when new data is available.
            try {
                FaqListContainer temp = FaqListContainer.deSerializeSelf(openFileInput(FAQ_FILE_NAME));
                listFragment.getFaqAdapter().setData(temp.getData());
                listFragment.updateData();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
