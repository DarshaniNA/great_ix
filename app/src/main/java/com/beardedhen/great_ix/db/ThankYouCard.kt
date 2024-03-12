package com.beardedhen.great_ix.db

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.beardedhen.great_ix.R

enum class ThankYouCard( val dbId: Int,
                         @DrawableRes
                         val drawableResId: Int,
                         @StringRes
                         val cardNameResId: Int ) {
    CARD_1(0, R.drawable.card_dragon, R.string.card_1),
    CARD_2(1, R.drawable.card_heart, R.string.card_2),
    CARD_3(2, R.drawable.card_rainbow, R.string.card_3),
    CARD_4(3, R.drawable.card_star, R.string.card_4);

    companion object {

        fun getType(numeral: Int): ThankYouCard {
            return values().first { it.dbId == numeral }
        }

        fun getTypeInt(type: ThankYouCard): Int {
            return type.dbId
        }
    }
}