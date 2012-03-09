package com.freeworks.android.tubedroidplayer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.freeworks.android.tubedroidplayer.listener.OnMenuButtonListener;
import com.freeworks.android.tubedroidplayer.widget.FacebookLikePager;

import android.view.ViewGroup;
import android.widget.VideoView;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class TubeDroidPlayerActivity  extends FragmentActivity implements OnMenuButtonListener{

    public FacebookLikePager mPager;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainfragmentdisplay);

        mPager = (FacebookLikePager)findViewById(R.id.fragment_slide_fragment_support_pager);

    }

    public void openBehind() {
        if (!mPager.isOpened()) {
            mPager.open();
            View vb=findViewById(R.id.leftspacearea);
            vb.setVisibility(View.GONE);

            View vi=findViewById(R.id.rightspacearea);
            vi.bringToFront();
            vi.setVisibility(View.VISIBLE);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TubeDroidPlayerActivity.this.openBehind();
                }
            });

        } else {
            View vb=findViewById(R.id.leftspacearea);
            vb.setVisibility(View.VISIBLE);
            vb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            View vi=findViewById(R.id.rightspacearea);
            vi.setVisibility(View.GONE);

            mPager.close();
        }

    }

    @Override
    public void onMenuButtonTouch() {
        //To change body of implemented methods use File | Settings | File Templates.
        openBehind();
    }


}