package wayne.edu.last.strems;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;


import wayne.edu.last.strems.fragment.RecentPostsFragment;
import wayne.edu.last.strems.fragment.MyPostsFragment;


public class  MainActivity extends BaseActivity {


    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[] {
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                   // new MyTopPostsFragment(),
            };


            private final String[] mFragmentNames = new String[] {
                    "Recent Emergencies",
                    "My Reported Case",
                   // "My Top Posts",
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewCaseActivity.class));
            }
        });

       /* findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEmergencyActivity.class));
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
