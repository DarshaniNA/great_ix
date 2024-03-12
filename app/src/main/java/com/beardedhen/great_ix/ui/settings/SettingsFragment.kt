package com.beardedhen.great_ix.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.ui.BaseFieldFragment
import com.beardedhen.great_ix.ui.GreatIxAlert
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SettingsFragment : BaseFieldFragment() {


    companion object {
        private const val FILE_PERMISSION_REQUEST_CODE = 3230
    }

    override val layoutResId: Int
        get() = R.layout.fragment_settings

    override val fieldContainer: ViewGroup
        get() = pageContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveButton.setOnClickListener{

            if( validateFields() ) {

                saveFields()

                if (ActivityCompat.checkSelfPermission(context!!,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                               Manifest.permission.READ_EXTERNAL_STORAGE),
                                       FILE_PERMISSION_REQUEST_CODE )
                } else {
                    configuration.isSettingsIncomplete = false

                    findNavController().navigate( SettingsFragmentDirections.actionBack() )
                }
            } else {
                GreatIxAlert( context = context!!,
                              message = "Please complete these details" ).display()
            }
        }

        populateFields()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = false
        menu.findItem(R.id.action_about)?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate( SettingsFragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                FILE_PERMISSION_REQUEST_CODE -> {
                    configuration.isSettingsIncomplete = false

                    findNavController().navigate( SettingsFragmentDirections.actionBack() )
                }
            }
        }
    }
}
