package com.beardedhen.great_ix.db

enum class PublishComments(val dbId: Int) {

    YES_INCLUDE_NAME(0),
    YES_EXCLUDE_NAME(1),
    NO(2);

    companion object {
        fun getType(numeral: Int): PublishComments {
            return values().first { it.dbId == numeral }
        }
        fun getTypeInt(type: PublishComments): Int {
            return type.dbId
        }
    }
}