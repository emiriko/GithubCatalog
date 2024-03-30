package com.example.githubcatalog.ui.detail_profile

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.githubcatalog.R
import com.example.githubcatalog.databinding.FragmentDetailProfileBinding
import com.example.githubcatalog.ui.setting.SettingPreferences
import com.example.githubcatalog.ui.setting.dataStore
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailProfileFragment : Fragment() {

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private var _binding: FragmentDetailProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String
    private val viewModel: DetailProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProfileBinding.inflate(inflater, container, false)

        val root: View = binding.root

        arguments?.getString(USERNAME)?.let {
            username = it
        }

        activity?.findViewById<TextView>(R.id.tvTitle)?.text = username

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#121212")))
                activity.window.statusBarColor = Color.parseColor("#121212")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#DAC0A3")))
                activity.window.statusBarColor = Color.parseColor("#DAC0A3")
            }
        }

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetailInformation(username)

        val bundle = Bundle().apply {
            putString(USERNAME, username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.result.observe(viewLifecycleOwner) {
            binding.textViewUsername.text = it.login
            if (it.name != null) {
                binding.textViewGithubName.text = it.name.toString()
            } else {
                binding.textViewGithubName.visibility = View.GONE
            }

            binding.textViewFollowers.text = "${it.followers} ${getString(R.string.followers)}"
            binding.textViewFollowing.text = "${it.following} ${getString(R.string.following)}"
            Glide.with(binding.root)
                .load(it.avatarUrl)
                .into(binding.profileImage)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.profileImage.visibility = if (it) View.GONE else View.VISIBLE
            binding.textViewUsername.visibility = if (it) View.GONE else View.VISIBLE
            binding.textViewGithubName.visibility = if (it) View.GONE else View.VISIBLE
            binding.textViewFollowers.visibility = if (it) View.GONE else View.VISIBLE
            binding.textViewFollowing.visibility = if (it) View.GONE else View.VISIBLE
            binding.progressBarDetail.visibility = if (it) View.VISIBLE else View.GONE
            binding.tabs.visibility = if (it) View.GONE else View.VISIBLE
            binding.viewPager.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}