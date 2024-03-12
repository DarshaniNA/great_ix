package com.beardedhen.great_ix.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.beardedhen.great_ix.BuildConfig
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.configuration.Configuration
import com.beardedhen.great_ix.ui.list.ListingFragmentDirections
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

open class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXIT_TAP_AGAIN_TIMEOUT = 1000L
    }

    private lateinit var navController: NavController
    lateinit var viewModel: FormViewModel

    val configuration = Configuration.getInstance()

    private var tapAgainTimestamp = 0L

    private var firstFormPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if( !BuildConfig.DEBUG ) {
            Fabric.with(this, Crashlytics())
        }

        navController = findNavController(R.id.nav_host_fragment)

        // List the fragments that will not have a back key...
        // https://developer.android.com/guide/navigation/navigation-ui#appbarconfiguration
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.ListingFragment,
                  R.id.SettingsFragment,
                  R.id.LoginFragment)
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(application)).get(FormViewModel::class.java)

        addNewCardFAB.setOnClickListener { view ->
            addNewForm()
        }

        supportActionBar?.setDisplayShowTitleEnabled(false)

        if( !isValidPassword() ) {
            if(  configuration.previousPassword.isBlank() ) {
                navController.navigate(ListingFragmentDirections.actionGoToLogin())
            } else {
                GreatIxAlert(
                    context = this,
                    titleResId = R.string.password_changed_title,
                    messageResId = R.string.password_changed_text,
                    positiveButtonFunction = {
                    navController.navigate(ListingFragmentDirections.actionGoToLogin())
                }).display()
            }
        } else if( configuration.isSettingsIncomplete ) {
            navController.navigate( ListingFragmentDirections.actionListingFragmentToSettingsFragment() )
        }

        setFabVisibility()
    }

    private fun addNewForm() {
        navController.navigate(ListingFragmentDirections.actionListingFragmentToFormFragment())
    }

    private fun setFabVisibility() {

        navController.addOnDestinationChangedListener { _: NavController,
                                                        navDestination: NavDestination,
                                                        _: Bundle? ->

            firstFormPage = navDestination.id == R.id.FormFragment

            val showFab = navDestination.id == R.id.ListingFragment
            addNewCardFAB.visibility = if (showFab) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onBackPressed() {

        // Close fragment instead if there were any open (i.e. Help or Training).
        if (addNewCardFAB.visibility == View.VISIBLE) {
            checkForExitConfirmation()
        } else {

            if( firstFormPage ) {
                GreatIxAlert(
                    context = this,
                    titleResId = R.string.discard_changes_title,
                    messageResId = R.string.discard_changes_text,
                    positiveButtonText = "Discard",
                    positiveButtonFunction = {
                        super.onBackPressed()
                    },
                    negativeButtonText = "Cancel"
                ).display()
            } else if( !isValidPassword() || configuration.isSettingsIncomplete ) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    // Prevent the user exiting accidentally by getting them to confirm by tapping back again quickly.
    private fun checkForExitConfirmation() {

        val now = Date().time
        if (now - tapAgainTimestamp < EXIT_TAP_AGAIN_TIMEOUT) {

            setResult(RESULT_CANCELED)
            finish()
        } else {
            Toast.makeText(this, R.string.tap_again_to_exit, Toast.LENGTH_SHORT).show()
            tapAgainTimestamp = now
        }
    }

    fun refreshButtons( showFab: Boolean) {
        addNewCardFAB.visibility = if (showFab) View.VISIBLE else View.GONE
    }

    fun isValidPassword() : Boolean {
        return configuration.previousPassword == getString( R.string.password)
    }
}
