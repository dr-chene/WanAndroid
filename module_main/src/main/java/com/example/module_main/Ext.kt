package com.example.module_main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
Created by chene on @date 20-12-4 下午9:41
 **/
fun FragmentManager.nav(fragment: Fragment) {
    popBackStack()
    beginTransaction().addToBackStack(null)
        .replace(R.id.main_fragment_container, fragment)
        .commit()
}