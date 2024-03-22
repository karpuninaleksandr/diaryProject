package com.example.diaryproject.taglistviewment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diaryproject.R
import com.example.diaryproject.adapters.TagAdapter
import com.example.diaryproject.database.DiaryDatabase
import com.example.diaryproject.databinding.FragmentTagListViewBinding

class TagListViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        var binding: FragmentTagListViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tag_list_view, container, false)

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = TagListViewModelFactory(dao, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)[TagListViewModel::class.java]

        val adapter = TagAdapter()
        binding.tagsList.adapter = adapter

        viewModel.tags.observe(viewLifecycleOwner) { tags ->
            if (tags != null)
                adapter.data = tags
        }

        adapter.onItemClick = { tag ->
            viewModel.onTagClick(tag.tagId)
        }

        viewModel.navigateToTag.observe(viewLifecycleOwner) { tag ->
            if (tag != null) {
                this.findNavController().navigate(
                    TagListViewFragmentDirections
                        .actionTagListViewToTag(tag.tagId)
                )
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToAddTag.observe(viewLifecycleOwner) { tag ->
            if (tag != null) {
                this.findNavController().navigate(
                    TagListViewFragmentDirections
                        .actionTagListViewToAddTag(tag.tagId)
                )
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToDiaryView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    TagListViewFragmentDirections.actionTagListViewToDiaryView()
                )
                viewModel.doneNavigating()
            }
        }

        binding.addTag.setOnClickListener {
            viewModel.onAddTag()
        }

        binding.goToDiary.setOnClickListener {
            viewModel.onGoToDiary()
        }

        return binding.root
    }
}