package org.evandro.megabot

interface Strategy {
    var executer: Executer?
    fun trade() : Boolean
    override fun toString(): String
}