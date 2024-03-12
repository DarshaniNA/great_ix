package com.beardedhen.great_ix.utils

import android.text.InputType

enum class FieldInputType(val viewInputType: Int) {

    FIRST_CHAR_EVERY_WORD_CAPITALIZED(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS),
    FIRST_CHAR_EACH_SENTENCE_CAPITALIZED(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE),
    PASSWORD(InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_TEXT_VARIATION_PASSWORD),
    EMAIL(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

    companion object {

        fun getInputTypeFromFieldName(configurationKey: String?): FieldInputType {
            return when (configurationKey) {
                "YOUR_NAME_KEY", "YOUR_WORK_PLACE_KEY", "YOUR_OTHER_WORK_PLACE_KEY", "YOUR_ROLE_KEY", "YOUR_OTHER_ROLE_KEY", "WHO_GREAT_KEY", "SPECIALITY_KEY" -> FIRST_CHAR_EVERY_WORD_CAPITALIZED
                "YOUR_EMAIL_KEY", "RECIPIENT_EMAIL_KEY" -> EMAIL
                "PASSWORD" -> PASSWORD
                else -> FIRST_CHAR_EACH_SENTENCE_CAPITALIZED
            }
        }
    }
}