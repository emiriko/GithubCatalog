package com.example.githubcatalog.ui.detail_profile

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubcatalog.R
import com.example.githubcatalog.data.remote.response.ItemsItem
import com.example.githubcatalog.databinding.FragmentRelationshipBinding
import com.example.githubcatalog.ui.home.ResultAdapter


class RelationshipFragment : Fragment() {
    companion object {
        const val ACTION = "action"
        const val USERNAME = "username"
    }

    private lateinit var username: String
    private lateinit var action: String

    private var _binding: FragmentRelationshipBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by viewModels<DetailProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRelationshipBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            username = it.getString(USERNAME).toString()
            action = it.getString(ACTION).toString()
        }

        detailViewModel.getRelationship(username, action)

        val attrs = intArrayOf(android.R.attr.listDivider)

        val a = requireContext().obtainStyledAttributes(attrs)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
        val insetDivider = InsetDrawable(divider, inset, 16, inset, 16)
        a.recycle()

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewRelationship.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(insetDivider)
        binding.recyclerViewRelationship.addItemDecoration(itemDecoration)

        detailViewModel.relationship.observe(viewLifecycleOwner) {
            setAdapter(it)
        }

        detailViewModel.isRelationshipLoading.observe(viewLifecycleOwner) {
            binding.recyclerViewRelationship.visibility = if (it) View.GONE else View.VISIBLE
            binding.progressBarRelationship.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setAdapter(data: List<ItemsItem>) {
        val adapter = ResultAdapter()
        adapter.submitList(data)
        binding.recyclerViewRelationship.adapter = adapter

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
}