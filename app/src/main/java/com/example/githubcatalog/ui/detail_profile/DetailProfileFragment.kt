package com.example.githubcatalog.ui.detail_profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

    lateinit var username: String
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
        
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        
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
            
            binding.textViewFollowers.text = "${it.followers.toString()} ${getString(R.string.followers)}"
            binding.textViewFollowing.text = "${it.following.toString()} ${getString(R.string.following)}"
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
            binding.progressBarRelationship.visibility = if (it) View.GONE else View.VISIBLE
        }
        
        viewModel.isRelationshipLoading.observe(viewLifecycleOwner) {
            Log.d("DetailProfileFragment", "ganti loading")
            binding.progressBarRelationship.visibility = if (it) View.GONE else View.GONE
        }
        
        viewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}