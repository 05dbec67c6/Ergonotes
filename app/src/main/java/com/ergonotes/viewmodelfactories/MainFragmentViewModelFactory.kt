package com.ergonotes.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.database.NoteEntryDao
import com.ergonotes.viewmodels.MainFragmentViewModel

class MainFragmentViewModelFactory(
    private val dataSource: NoteEntryDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            return MainFragmentViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}