package bi.udev.guymichel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by KonstrIctor on 16/11/2019.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    String[] page_titles;

    public TabsAdapter(FragmentManager fm, String[] page_titles) {
        super(fm);
        this.page_titles = page_titles;
    }
// = new String[]{"emissions", "archives", "Annonces"};

    @Override
    public CharSequence getPageTitle(int position) {
        return page_titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EmissionsFragment();
            case 1:
                return new ArchivesFragment();
            case 2:
                return new TemoignagesFragment();
            default:
                return new EmissionsFragment();
        }
    }

    @Override
    public int getCount() {
        return page_titles.length;
    }
}
