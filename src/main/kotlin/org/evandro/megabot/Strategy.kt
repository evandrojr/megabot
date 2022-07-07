package org.evandro.megabot

interface Strategy {
    var engine: Engine?
    fun trade() : Boolean
    override fun toString(): String
}