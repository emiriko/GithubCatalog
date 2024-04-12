package com.example.githubcatalog.ui.home

import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubcatalog.R
import com.example.githubcatalog.data.remote.response.ItemsItem
import com.example.githubcatalog.databinding.FragmentHomeBinding
import com.example.githubcatalog.ui.detail_profile.DetailProfileFragment
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<HomeViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        activity?.findViewById<TextView>(R.id.tvTitle)?.setText(R.string.title_home)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ATTRS = intArrayOf(android.R.attr.listDivider)

        val a = requireContext().obtainStyledAttributes(ATTRS)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
        val insetDivider = InsetDrawable(divider, inset, 16, inset, 16)
        a.recycle()

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(insetDivider)
        binding.recyclerView.addItemDecoration(itemDecoration)

        mainViewModel.result.observe(viewLifecycleOwner) {
            binding.emptyView.visibility = if (it.totalCount == 0) View.VISIBLE else View.GONE
            setAdapter(it.items)
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }

        mainViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
        
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.searchUser(query ?: "")

                    // Clear focus to close the keyboard
                    searchView.clearFocus()

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun setAdapter(items: List<ItemsItem>) {
        val adapter = ResultAdapter()
        adapter.submitList(items)
        binding.recyclerView.adapter = adapter

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