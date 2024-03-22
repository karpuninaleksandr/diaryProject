package com.example.diaryproject.addtag

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
import com.example.diaryproject.databinding.FragmentAddTagBinding

class AddTagFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddTagBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_tag, container, false)

        val application = requireNotNull(this.activity).application

        val args = AddTagFragmentArgs.fromBundle(requireArguments())
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = AddTagViewModelFactory(args.tagId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AddTagViewModel::class.java]

        binding.confirmButton.setOnClickListener {
            viewModel.onConfirmAddRecord(binding.tagName.text.toString())
        }

        binding.denyButton.setOnClickListener {
            viewModel.onDenyAddRecord()
        }

        viewModel.navigateToTagListView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    AddTagFragmentDirections.actionAddTagToTagListView()
                )
                viewModel.doneNavigating()
            }
        }

        return binding.root
    }
}