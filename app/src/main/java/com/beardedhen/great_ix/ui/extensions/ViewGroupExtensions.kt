package com.beardedhen.great_ix.ui.extensions

import android.view.View
import android.view.ViewGroup
import com.beardedhen.great_ix.ui.views.SettingsFieldView

/**
 * Returns the immediate children of a view group as a sequence.
 */
private fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {

    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0

        override fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }

        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

/**
 * Methods to return list of views sequently.
 */
private val ViewGroup.views: List<View>
    get() = asSequence().toList()

val ViewGroup.settingViews: List<SettingsFieldView>
    get() = views.flatMap {
        when (it) {
            is SettingsFieldView -> listOf(it)
            is ViewGroup -> it.settingViews
            else -> listOf()
        }
    }
