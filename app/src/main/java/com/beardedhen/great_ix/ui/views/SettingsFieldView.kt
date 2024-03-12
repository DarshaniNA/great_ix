package com.beardedhen.great_ix.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.core.text.HtmlCompat
import com.beardedhen.great_ix.BuildConfig
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.configuration.Configuration
import com.beardedhen.great_ix.ui.event.DoneEvent
import com.beardedhen.great_ix.ui.setRightDrawableTouchListener
import com.beardedhen.great_ix.utils.FieldInputType
import kotlinx.android.synthetic.main.settings_field_view.view.*
import org.greenrobot.eventbus.EventBus


@SuppressLint("ViewConstructor")
class SettingsFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnKeyListener {

    enum class SettingsType {
        TEXT_FIELD,
        //CHECKBOX,
        DROPDOWN,
        RADIO_BUTTON
    }

    enum class YesNo( val dbId: Int) {
        NO(1),
        YES(0);

        companion object {
            fun toBoolean( value: YesNo ): Boolean {
                return value == YES
            }

            fun fromBoolean(value: Boolean): YesNo {
                return if (value) YES else NO
            }
        }
    }

    interface OnOptionClickListener {
        fun optionSelected( optionDbId: Int, selected: Boolean )
    }

    private var minValue = 0
    lateinit var configurationKey:String

    private var fieldType = SettingsType.TEXT_FIELD
    private var fieldInputType = FieldInputType.FIRST_CHAR_EACH_SENTENCE_CAPITALIZED
    private var dropdownOptionsResId:Int = 0
    private var singleChoiceResId:Int = 0

    private var pleaseSpecifyFieldId: Int = 0

    private var optionSelectedListener: OnOptionClickListener? = null

    init {
        inflate(context, R.layout.settings_field_view, this)
        initialiseStyle(attrs)
    }

    @SuppressLint("Recycle")
    private fun initialiseStyle (attrs: AttributeSet?) {

        if(context == null){
            throw Exception("Null context!")
        }

        // Hide name label when defined explicitly.
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.SettingsView)

            configurationKey = BuildConfig.APPLICATION_ID + attributes.getString(R.styleable.SettingsView_configurationKey)

            minValue = attributes.getInt(R.styleable.SettingsView_minValue, 0)

            if( attributes.hasValue( R.styleable.SettingsView_fieldType) ) {
                val ordinal = attributes.getInt(R.styleable.SettingsView_fieldType, 0)
                fieldType = SettingsType.values()[ordinal]
            }

            if( attributes.hasValue( R.styleable.SettingsView_infoText) ) {
                infoView.text =  attributes.getString( R.styleable.SettingsView_infoText )
                infoView.visibility = View.VISIBLE
            } else {
                infoView.visibility = View.GONE
            }

            when( fieldType ) {
                SettingsType.TEXT_FIELD -> {

                    fieldInputType = FieldInputType.getInputTypeFromFieldName(attributes.getString(R.styleable.SettingsView_configurationKey))
                    fieldView.inputType = fieldInputType.viewInputType

                    if (attributes.hasValue(R.styleable.SettingsView_android_hint)) {
                        fieldView.hint = attributes.getString(R.styleable.SettingsView_android_hint)
                    }

                    fieldView.setOnKeyListener(this)

                    fieldView.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            return@OnEditorActionListener true
                        } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                            EventBus.getDefault().post(DoneEvent())
                        }
                        false
                    })

                    // Add show/hide password if >= Nougat
                    if (fieldView.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD != 0 ) {

                        if( Build.VERSION.SDK_INT > Build.VERSION_CODES.M ) {
                            setupPasswordButtonWithAction(fieldView)
                        } else {
                            fieldView.transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                    }
                }

                SettingsType.DROPDOWN -> {
                    dropdownOptionsResId =
                        attributes.getResourceId(R.styleable.SettingsView_optionSelector, 0)

                    val options = resources.getStringArray(dropdownOptionsResId)
                    val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                        context, R.layout.spinner_item,
                        options
                    )

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerView.adapter = spinnerArrayAdapter

                    if( attributes.hasValue( R.styleable.SettingsView_pleaseSpecifyFieldId) )  {
                        pleaseSpecifyFieldId = attributes.getResourceId( R.styleable.SettingsView_pleaseSpecifyFieldId,0 )
                    }
                }

                SettingsType.RADIO_BUTTON -> {
                    singleChoiceResId =
                        attributes.getResourceId(R.styleable.SettingsView_singleChoiceValue, 0)

                    val options = resources.getStringArray(singleChoiceResId)

                    for (option in options.withIndex() ) {

                        val optionView = RadioButton( context )
                        optionView.text = option.value

                        val scale = resources.displayMetrics.density
                        val dpAsPixels = ( 5 * scale + 0.5f).toInt()
                        optionView.setPadding(0,dpAsPixels,0,0)

                        optionView.tag = option.index       // TODO - use dbId from underlying enum

                        radioGroupView.addView( optionView )
                    }

                    radioGroupView.setOnCheckedChangeListener { _, checkedId ->
                        errorView.error = null
                    }
                }
            }

            labelView.text = attributes.getString(R.styleable.SettingsView_android_text)

            fieldFlipperView.displayedChild = fieldType.ordinal
        }
    }

    private val pleaseSpecifyFieldView : View
        get () = (parent.parent as ViewGroup).findViewById<View>(pleaseSpecifyFieldId)

    fun populate( configuration: Configuration ) {

        val value = configuration.getSetting( configurationKey )

        when( fieldType ) {
            SettingsType.TEXT_FIELD -> fieldView.setText( value )
            SettingsType.RADIO_BUTTON -> selectChoice( value )
            SettingsType.DROPDOWN -> {
                selectOption(value)

                if( pleaseSpecifyFieldId != 0 ) {

                    pleaseSpecifyFieldView.visibility = if( value == "Other" ) View.VISIBLE else View.GONE

                    spinnerView.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedValue = spinnerView.selectedItem as String
                            pleaseSpecifyFieldView.visibility = if (selectedValue == "Other" ) View.VISIBLE else View.GONE
                        }
                    }
                }
            }
        }
    }

    fun populate( value: String?, autoCompleteSeeds: List<String>? = null) {

        value?.let {

            when( fieldType ) {
                SettingsType.TEXT_FIELD -> {
                    fieldView.setText(value)

                    autoCompleteSeeds?.let { seeds ->
                        val adapter = ArrayAdapter(context,
                            android.R.layout.simple_list_item_1, seeds)
                        fieldView.setAdapter(adapter)
                    }
                }
                SettingsType.RADIO_BUTTON -> {
                    selectChoice(value)
                }
                //SettingsType.CHECKBOX -> checkboxView.isChecked = configuration.getSettingFlag( configurationKey )
                SettingsType.DROPDOWN -> {
                    selectOption(value)
                }
            }
        }
    }

    fun populate( value: Boolean? ) {

        value?.let {
            val pos = if (value) 0 else 1
            val selectedRadioButton = radioGroupView.getChildAt(pos) as RadioButton
            selectedRadioButton.isChecked = true
        }
    }

    fun populate( dbId: Int?, optionSelectedListener: OnOptionClickListener? = null ) {

        dbId?.let {id ->
            val selectedRadioButton = radioGroupView.getChildAt(id) as RadioButton    // TODO change to find tag
            selectedRadioButton.isChecked = true
        }

        this.optionSelectedListener = optionSelectedListener

        optionSelectedListener?.let { listener ->

            radioGroupView.setOnCheckedChangeListener { group, checkedId ->
                errorView.error = null
                val checkedRadioButton = group.findViewById(checkedId) as RadioButton
                listener.optionSelected( checkedRadioButton.tag as Int, checkedRadioButton.isSelected )
            }
        }
    }

    private fun selectOption(value: String) {
        val options = resources.getStringArray(dropdownOptionsResId)

        val selectedOption = options.indexOf(value)
        if (selectedOption > 0) {
            spinnerView.setSelection(selectedOption)
        }
    }

    private fun selectChoice(value: String) {
        val optionIndex = resources.getStringArray(singleChoiceResId).indexOf( value )
        if( optionIndex >= 0 ) {
            val selectedRadioButton = radioGroupView.getChildAt(optionIndex) as RadioButton
            selectedRadioButton.isChecked = true
        }
    }

    fun getValue() : String {
        return when( fieldType ) {
            SettingsType.TEXT_FIELD -> fieldView.text.toString().trim()
            SettingsType.RADIO_BUTTON -> {

                val radioButtonID = radioGroupView.checkedRadioButtonId

                if( radioButtonID != - 1 ) {

                    val radioButton = radioGroupView.findViewById(radioButtonID) as View
                    val selecetdIndex = radioGroupView.indexOfChild(radioButton)

                    val options = resources.getStringArray(singleChoiceResId)
                    options[selecetdIndex]
                } else ""
            }
            SettingsType.DROPDOWN -> {

                val pos = spinnerView.selectedItemPosition
                val options = resources.getStringArray(dropdownOptionsResId)
                options[pos]
            }
            //SettingsType.CHECKBOX -> configuration.storeSettingFlag( configurationKey, checkboxView.isChecked )
        }
    }


    fun storeValue( configuration: Configuration) {
        configuration.storeSetting( configurationKey, getValue() )
    }

    fun validate() : Boolean {

        return if( getValue().isBlank() &&
                   labelView.text.toString().contains("*")) {
            when(fieldType) {
                SettingsType.RADIO_BUTTON -> errorView.error = HtmlCompat.fromHtml("<font color='#FFFFFF'>Required</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                else -> fieldView.error = HtmlCompat.fromHtml("<font color='#FFFFFF'>Required</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            false
        } else if( fieldType == SettingsType.TEXT_FIELD && fieldInputType == FieldInputType.EMAIL ) {

            val value = getValue()

            val valid = value.isBlank() || Patterns.EMAIL_ADDRESS.matcher(value ).matches()

            if( !valid ) {
                fieldView.error = HtmlCompat.fromHtml("<font color='#FFFFFF'>Invalid Email Address</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                fieldView.error = null
            }

            valid

        } else {

            when(fieldType) {
                SettingsType.RADIO_BUTTON -> errorView.error = null
                else -> fieldView.error = null
            }
            true
        }
    }

    fun validatePassword() : Boolean {
        return if( getValue().isBlank() &&
            labelView.text.toString().contains("*")) {
            fieldView.error = HtmlCompat.fromHtml("<font color='#FFFFFF'>Required</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            false
        } else {
            if (getValue() == resources.getString(R.string.password)) {
                true
            } else {
                Toast.makeText(context, "Incorrect Password", Toast.LENGTH_LONG ).show()
                false
            }
        }
    }
    fun getSelectedDbId(): Int {
        val radioButtonID: Int = radioGroupView.checkedRadioButtonId
        val radioButton: View = radioGroupView.findViewById(radioButtonID)
        return radioButton.tag as Int
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }


//    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordButtonWithAction(view: EditText) {

        view.tag = false     // hide to start with (true will be immediately toggled to false!)
        togglePasswordShowIcon(view)

        view.setRightDrawableTouchListener { togglePasswordShowIcon(view) }
    }

    private fun togglePasswordShowIcon(view: EditText) {

        val show = !(view.tag as Boolean)

        val showHideIcon = if (show) R.drawable.ic_eye_off_grey_18dp else R.drawable.ic_eye_grey_18dp
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, showHideIcon, 0)

        view.transformationMethod = if (!show) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }

        view.tag = show
    }
}
