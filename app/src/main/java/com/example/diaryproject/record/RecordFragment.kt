package com.example.diaryproject.record

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diaryproject.R
import com.example.diaryproject.Utils
import com.example.diaryproject.database.DiaryDatabase
import com.example.diaryproject.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_record, container, false)

        val application = requireNotNull(this.activity).application

        val args = RecordFragmentArgs.fromBundle(requireArguments())
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = RecordViewModelFactory(args.recordId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[RecordViewModel::class.java]

        binding.goToDiary.setOnClickListener {
            viewModel.onGoToDiary(binding.recordValue.text.toString())
        }

        binding.deleteRecord.setOnClickListener {
            viewModel.onDeleteRecord()
        }

        binding.recordValue.text = Editable.Factory.getInstance().newEditable(viewModel.record?.recordValue)
        binding.recordTags.text = viewModel.tags
        binding.recordLastChangeTime.text = viewModel.record?.lastChangeTimeMillis?.let {
            Utils.convertLongToDateString(
                it
            )
        }

        val tagsSpinnerAdapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_item, viewModel.tagsList.map { it.tagName })
        binding.tagsSpinner.adapter = tagsSpinnerAdapter
        tagsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.deleteTag.setOnClickListener {
            viewModel.onDeleteTag(binding.tagsSpinner.selectedItemPosition)
        }
        binding.addTag.setOnClickListener {
            viewModel.onAddTag(binding.tagsSpinner.selectedItemPosition)
        }

        viewModel.updateTagsList.observe(viewLifecycleOwner) { tags ->
            if (tags != null) {
                binding.recordTags.text = tags
                viewModel.doneUpdating()
            }
        }

        viewModel.navigateToDiaryView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    RecordFragmentDirections.actionRecordToDiaryView()
                )
                viewModel.doneNavigating()
            }
        }

        return binding.root
    }
}