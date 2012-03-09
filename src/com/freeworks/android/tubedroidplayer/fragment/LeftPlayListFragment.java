package com.freeworks.android.tubedroidplayer.fragment;
import android.support.v4.app.*;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.freeworks.android.tubedroidplayer.R;
/**
 * Created by IntelliJ IDEA.
 * User: tarotaro
 * Date: 12/03/07
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class LeftPlayListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.leftplaylist, container, false);


        return view;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}
