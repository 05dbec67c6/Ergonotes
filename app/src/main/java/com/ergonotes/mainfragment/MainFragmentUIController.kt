package com.ergonotes.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ergonotes.R
import com.ergonotes.database.NoteDatabase
import com.ergonotes.databinding.FragmentMainBinding
import com.ergonotes.mainfragment.recyclerview.NoteEntryAdapterNotes
import com.ergonotes.mainfragment.recyclerview.NoteEntryAdapterTitles
import com.ergonotes.mainfragment.recyclerview.NoteEntryNotesListener
import com.ergonotes.mainfragment.recyclerview.NoteEntryTitlesListener

class MainFragmentUIController : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// -------------------------------------------------------------------------------------------------
// Reference binding object and inflate fragment----------------------------------------------------

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        val application = requireNotNull(this.activity).application

// -------------------------------------------------------------------------------------------------
// Setting up ViewModel and Factory-----------------------------------------------------------------

        // Create an instance of the ViewModel Factory
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = MainFragmentViewModelFactory(dataSource)

        // Associate ViewModel with this Fragment
        val mainFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainFragmentViewModel::class.java)

        // Use View Model with data binding
        binding.mainFragmentViewModel = mainFragmentViewModel

// -------------------------------------------------------------------------------------------------
// Setting current activity as lifecycle owner of the binding for LiveData--------------------------

        binding.lifecycleOwner = this

// -------------------------------------------------------------------------------------------------
// Setting up Recyclerview in Gridlayout------------------------------------------------------------

        val managerNotes = GridLayoutManager(activity, 3)
        binding.recyclerViewNotes.layoutManager = managerNotes

        val managerTitles = GridLayoutManager(activity, 3)
        binding.recyclerViewTitles.layoutManager = managerTitles

// -------------------------------------------------------------------------------------------------
// Instance of adapter that handles click on listItem and submit recyclerviews list-----------------

        val adapterNotes = NoteEntryAdapterNotes(NoteEntryNotesListener { noteId ->
            mainFragmentViewModel.onNoteEntryClicked(noteId)
        })

        binding.recyclerViewNotes.adapter = adapterNotes

        // Submitting the whole list for the recyclerview
        mainFragmentViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterNotes.submitList(it)
            }
        })


        val adapterTitles = NoteEntryAdapterTitles(NoteEntryTitlesListener { noteId ->
            mainFragmentViewModel.onNoteEntryClicked(noteId)
        })

        binding.recyclerViewTitles.adapter = adapterTitles

        // Submitting the whole list for the recyclerview
        mainFragmentViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterTitles.submitList(it)
            }
        })
//--------------------------------------------------------------------------------------------------
// Experimental here later for XML------------------------------------------------------------------

        binding.buttonAdd.setOnClickListener { mainFragmentViewModel.onAdd() }
        binding.buttonClear.setOnClickListener { mainFragmentViewModel.onClear() }

        mainFragmentViewModel.navigateToNewFragment.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                this.findNavController().navigate(
                    MainFragmentUIControllerDirections
                        .actionMainFragmentUIControllerToNewFragmentUIController(note)
                )
            }
        })

// -------------------------------------------------------------------------------------------------
// New stuff here--------------------------------------------------------------------------------


// -------------------------------------------------------------------------------------------------
        return binding.root
    }
}

