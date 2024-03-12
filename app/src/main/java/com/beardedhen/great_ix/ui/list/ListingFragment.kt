package com.beardedhen.great_ix.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormDao
import com.beardedhen.great_ix.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_listing.*

/**
 * A simple [Fragment] subclass as the listing destination in the navigation.
 */
class ListingFragment : Fragment() {

    private lateinit var formDao: FormDao

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listing, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).let { mainActivity ->
            formDao = mainActivity.viewModel.formDao
        }

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formViewSwitcherView.displayedChild = if( formDao.count() == 0L ) 0 else 1
    }

    override fun onResume() {
        super.onResume()

        setupFormList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = true
        menu.findItem(R.id.action_about)?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate( ListingFragmentDirections.actionListingFragmentToSettingsFragment() )
                false
            }
            R.id.action_about -> {
                findNavController().navigate( ListingFragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupFormList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        formsListView.setHasFixedSize(true)
        formsListView.layoutManager = LinearLayoutManager(context)
        formsListView.adapter = FormsListAdapter(
            formDao.getAll()
        )
    }
}
