package com.example.instagramclone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagramclone.R
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.databinding.FragmentSearchBinding
import com.example.instagramclone.recyclerview.adapter.ItemUserAdapter

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding

    private lateinit var adapterRV : ItemUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)


        adapterRV = ItemUserAdapter()
        binding.rvSearch.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterRV
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(view.context, query.toString(), Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            }
        )
    }
}