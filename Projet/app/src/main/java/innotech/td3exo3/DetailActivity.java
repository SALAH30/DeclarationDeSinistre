package innotech.td3exo3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class DetailActivity extends AppCompatActivity {

    private ViewPager pager;
    private AppSectionsPagerAdapter adapter;
    private static Dossier dossier;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        adapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        context= this;
        Intent intent = getIntent();
        dossier = (Dossier) intent.getSerializableExtra("dossier");
        System.out.println("ID dossier Video : " + dossier.getVideo());
        System.out.println("ID dossier : Photo " + dossier.getPhoto());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0 :
                    return DetailInfoFragment.getInstance(dossier);
                case 1 :
                    return Composant2.getInstance(context,dossier.getVideo());
                case 2 :
                    return Composant3.getInstance(dossier.getPhoto());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position)
            {
                case 0 :
                    return "Détails";
                case 1 :
                    return "Video";
                case 2 :
                    return "Photo";

            }
            return "";
        }
    }
}
