package com.example.githubcatalog.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubcatalog.R
import com.example.githubcatalog.data.Result
import com.example.githubcatalog.data.local.entity.FavoriteEntity
import com.example.githubcatalog.data.remote.response.ItemsItem
import com.example.githubcatalog.databinding.FragmentFavoriteBinding
import com.example.githubcatalog.ui.ViewModelFactory
import com.example.githubcatalog.ui.detail_profile.DetailProfileFragment
import com.example.githubcatalog.ui.home.ResultAdapter
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val root: View = binding.root

        activity?.findViewById<TextView>(R.id.tvTitle)?.setText(R.string.title_favorites)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel: FavoriteViewModel by viewModels {
            factory
        }


        val layoutManager = LinearLayoutManager(context)
        binding.rvFavorite.layoutManager = layoutManager

        viewModel.getAllFavoriteUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarFavorite.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBarFavorite.visibility = View.GONE
                        setAdapter(result.data)
                    }

                    is Result.Error -> {
                        binding.progressBarFavorite.visibility = View.GONE
                        Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun setAdapter(items: List<FavoriteEntity>) {
        val newItems = items.map {
            ItemsItem(
                login = it.username,
                avatarUrl = it.avatarUrl
            )
        }
        
        val adapter = ResultAdapter()
        adapter.submitList(newItems)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : ResultAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val detailProfileFragment = DetailProfileFragment()

                val bundle = Bundle()
                bundle.putString(DetailProfileFragment.USERNAME, data.login)

                detailProfileFragment.arguments = bundle

                val fragmentManager = activity?.supportFragmentManager

                fragmentManager?.commit {
                    replace(
                        R.id.container,
                        detailProfileFragment,
                        DetailProfileFragment::class.java.simpleName
                    )
                    addToBackStack(null)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}