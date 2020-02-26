package com.xtooltech.adten.util


inline infix fun Boolean.trueLet(trueBlock: Boolean.() -> Unit): Else {
    if (this) {
        trueBlock()
        return NotDoElse(this)
    }
    return DoElse(this)
}

inline infix fun Boolean.falseLet(falseBlock: Boolean.() -> Unit): Else {
    if (!this) {
        falseBlock()
        return NotDoElse(this)
    }
    return DoElse(this)
}

interface Else {
    infix fun elseLeft(elseBlock: Boolean.() -> Unit)
}

class DoElse(private val boolean: Boolean) : Else {
    override infix fun elseLeft(elseBlock: Boolean.() -> Unit) {
        elseBlock(boolean)
    }
}

class NotDoElse(private val boolean: Boolean) : Else {
    override infix fun elseLeft(elseBlock: Boolean.() -> Unit) {
    }
}
