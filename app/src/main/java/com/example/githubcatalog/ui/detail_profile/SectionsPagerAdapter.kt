package com.example.githubcatalog.ui.detail_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: Fragment, private val bundle: Bundle) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val username = bundle.getString(DetailProfileFragment.USERNAME)
        val relationshipFragment = RelationshipFragment()

        val bundle = Bundle()
        bundle.putString(RelationshipFragment.USERNAME, username)
        when (position) {
            0 -> {
                bundle.putString(RelationshipFragment.ACTION, "followers")
                relationshipFragment.arguments = bundle
            }

            1 -> {
                bundle.putString(RelationshipFragment.ACTION, "following")
                relationshipFragment.arguments = bundle
            }
        }
        return relationshipFragment
    }

}
