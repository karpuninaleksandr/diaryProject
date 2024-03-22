package com.example.diaryproject.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diaryproject.R
import com.example.diaryproject.database.DiaryDatabase
import com.example.diaryproject.databinding.FragmentTagBinding

class TagFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTagBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tag, container, false)

        val application = requireNotNull(this.activity).application

        val args = TagFragmentArgs.fromBundle(requireArguments())
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = TagFragmentViewModelFactory(args.tagId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TagFragmentViewModel::class.java)

        binding.deleteTag.setOnClickListener {
            viewModel.onDeleteTag()
        }

        binding.tagName.text = viewModel.tag?.tagName

        binding.goToTags.setOnClickListener {
            viewModel.onGoToTagList()
        }

        viewModel.navigateToTagListView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    TagFragmentDirections.actionTagToTagListView()
                )
                viewModel.doneNavigating()
            }
        }

        return binding.root
    }
}