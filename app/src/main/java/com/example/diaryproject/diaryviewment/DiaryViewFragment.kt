package com.example.diaryproject.diaryviewment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diaryproject.R
import com.example.diaryproject.adapters.RecordAdapter
import com.example.diaryproject.database.DiaryDatabase
import com.example.diaryproject.database.DiaryRecord
import com.example.diaryproject.databinding.FragmentDiaryViewBinding


class DiaryViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: FragmentDiaryViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_diary_view, container, false)

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).getDiaryDatabaseDao()
        val viewModelFactory = DiaryViewModelFactory(dao, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)[DiaryViewModel::class.java]

        val adapter = RecordAdapter()
        binding.recordsList.adapter = adapter

        viewModel.updateRecordList.observe(viewLifecycleOwner) { newRecords ->
            if (newRecords != null) {
                adapter.data = newRecords as List<DiaryRecord>
                viewModel.doneUpdating()
            }
        }

        val tagsSpinnerAdapter = ArrayAdapter<String>(this.requireContext(),
            android.R.layout.simple_spinner_item)

        tagsSpinnerAdapter.add("---")
        tagsSpinnerAdapter.addAll(viewModel.tags.map { it.tagName })
        
        adapter.onItemClick = { record ->
            viewModel.onRecordClick(record.recordId)
        }

        binding.tagsSpinner.adapter = tagsSpinnerAdapter
        tagsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.tagsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onFilter(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewModel.navigateToRecord.observe(viewLifecycleOwner) { record ->
            if (record != null) {
                this.findNavController().navigate(
                    DiaryViewFragmentDirections
                        .actionDiaryViewToRecord(record.recordId)
                )
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToAddRecord.observe(viewLifecycleOwner) { record ->
            if (record != null) {
                this.findNavController().navigate(
                    DiaryViewFragmentDirections
                        .actionDiaryViewToAddRecord(record.recordId)
                )
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToTagListView.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    DiaryViewFragmentDirections.actionDiaryViewToTagListView()
                )
                viewModel.doneNavigating()
            }
        }

        binding.addRecord.setOnClickListener {
            viewModel.onAddRecord()
        }

        binding.goToTags.setOnClickListener {
            viewModel.onGoToTagListView()
        }

        return binding.root
    }
}