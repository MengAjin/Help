package com.example.fragment_tab;

import android.app.Fragment;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
    
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = new sosFragment();
                break;
            case 2:
                fragment = new helpFragment();
                break;
            case 3:
                fragment = new askFragment();
                break;
          
        }
        return fragment;
    }
}
