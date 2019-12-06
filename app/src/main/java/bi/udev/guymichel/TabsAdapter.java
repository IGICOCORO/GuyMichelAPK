package bi.udev.guymichel;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by KonstrIctor on 16/11/2019.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    String[] page_titles;
    MainActivity activity;

    public TabsAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.page_titles = new String[]{activity.getResources().getString(R.string.Emission),
                                        activity.getResources().getString(R.string.Archives),
                                        activity.getResources().getString(R.string.Messages)};
        this.activity = activity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return page_titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EmissionsFragment(activity);
            case 1:
                return new ArchivesFragment(activity);
            case 2:
                return new TemoignagesFragment(activity);
            default:
                return new EmissionsFragment(activity);
        }
    }

    @Override
    public int getCount() {
        return page_titles.length;
    }
}
