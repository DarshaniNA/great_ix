package com.beardedhen.great_ix.ui.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.configuration.Configuration
import com.beardedhen.great_ix.utils.DisplayUtil
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    val configuration = Configuration.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onPause() {
        super.onPause()

        // To prevent the keyboard to be showing whenever we pause the fragment.
        DisplayUtil.hideSoftKeyboard(this.passwordText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginButton.setOnClickListener{

            if( passwordText.validatePassword() ) {
                configuration.previousPassword = passwordText.getValue()
                findNavController().navigate( LoginFragmentDirections.actionLoginFragmentToSettingsFragment())
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = false
        menu.findItem(R.id.action_about)?.isVisible = false
    }
}