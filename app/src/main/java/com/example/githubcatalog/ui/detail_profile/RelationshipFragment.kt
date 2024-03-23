package com.example.githubcatalog.ui.detail_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubcatalog.data.response.ItemsItem
import com.example.githubcatalog.databinding.FragmentRelationshipBinding
import com.example.githubcatalog.ui.home.ResultAdapter

@Suppress("UNCHECKED_CAST")
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

        val root: View = binding.root
        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        arguments?.let {
            username = it.getString(USERNAME).toString()
            action = it.getString(ACTION).toString()
        }
        
        Log.d("RelationshipFragment", "onViewCreated: $username $action")
        detailViewModel.getRelationship(username, action)
        
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewRelationship.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.recyclerViewRelationship.addItemDecoration(itemDecoration)
        
        detailViewModel.relationship.observe(viewLifecycleOwner) {
            Log.d("RelationshipFragment", "onViewCreated: $it")
            setAdapter(it.relationshipResponse)
        }
        
        detailViewModel.isRelationshipLoading.observe(viewLifecycleOwner) {
            binding.recyclerViewRelationship.visibility = if (it) View.GONE else View.VISIBLE
        }
    }
    
    private fun setAdapter(data: List<ItemsItem>) {
        val adapter = ResultAdapter()
        adapter.submitList(data)
        binding.recyclerViewRelationship.adapter = adapter
    }
}