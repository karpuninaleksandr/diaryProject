package com.example.diaryproject.addrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diaryproject.R
import com.example.diaryproject.database.DiaryDatabase
import com.example.diaryproject.databinding.FragmentAddRecordBinding

class AddRecordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_record, container, false)

        val application = requireNotNull(this.activity).application

        val args = AddRecordFragmentArgs.fromBundle(requireArguments())
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = AddRecordViewModelFactory(args.recordId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AddRecordViewModel::class.java]

        binding.confirmButton.setOnClickListener {
            viewModel.onConfirm(System.currentTimeMillis(),
                binding.recordValue.text.toString())
        }

        binding.denyButton.setOnClickListener {
            viewModel.onDeny()
        }

        viewModel.navigateToDiaryView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    AddRecordFragmentDirections
                        .actionAddRecordToDiaryView()
                )
                viewModel.doneNavigating()
            }
        }

        viewModel.updateTagsList.observe(viewLifecycleOwner) { tags ->
            if (tags != null) {
                binding.tagsList.text = tags
                viewModel.doneUpdating()
            }
        }

        val tagsSpinnerAdapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_item, viewModel.tagsList.map { it.tagName })

        binding.tagsSpinner.adapter = tagsSpinnerAdapter
        tagsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.addTagToRecord.setOnClickListener {
            viewModel.onAddTag(binding.tagsSpinner.selectedItemPosition)
        }
        return binding.root
    }
}