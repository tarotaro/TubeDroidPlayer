package com.freeworks.android.tubedroidplayer.fragment;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.freeworks.android.tubedroidplayer.R;
import com.freeworks.android.tubedroidplayer.listener.OnMenuButtonListener;

/**
 * Created by IntelliJ IDEA.
 * User: tarotaro
 * Date: 12/03/07
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class PlayerFragment extends Fragment {
    private View mSelfView;
    private OnMenuButtonListener menuButtonListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.playerfragment, container, false);
        mSelfView=view;

        ImageButton menubtn=(ImageButton)view.findViewById(R.id.menu_btn);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButtonListener.onMenuButtonTouch();
            }
        });
        return view;
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume(){
        super.onResume();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            menuButtonListener = (OnMenuButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMenuButtonListener");
        }
    }



}
