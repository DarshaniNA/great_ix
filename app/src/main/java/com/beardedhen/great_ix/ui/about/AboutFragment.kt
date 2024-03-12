package com.beardedhen.great_ix.ui.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import com.beardedhen.great_ix.BuildConfig
import com.beardedhen.great_ix.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = false
        menu.findItem(R.id.action_about)?.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bodyView.movementMethod = LinkMovementMethod.getInstance()

        versionView.text = "Version: ${BuildConfig.VERSION_NAME}"
    }
}
