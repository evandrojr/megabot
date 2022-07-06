package org.evandro.megabot

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LogTest {

    @Test
    fun write() {
        Log().error("Oi1")
        Log().info("Oi2")
    }
}