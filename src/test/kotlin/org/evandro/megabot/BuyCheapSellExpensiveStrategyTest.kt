package org.evandro.megabot

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BuyCheapSellExpensiveStrategyTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun trade() {
    }

    @Test
    fun shouldBuy() {
        val s = BuyCheapSellExpensiveStrategy(1.0, 1.0)
        assertTrue(s.shouldBuy(10.0, 5.0, 0.5))
    }

    @Test
    fun shouldBuyTooExpensive() {
        val s = BuyCheapSellExpensiveStrategy(1.0, 1.0)
        assertFalse(s.shouldBuy(10.0, 9.0, 0.5))
    }

    @Test
    fun shouldBuyEvenMoreExpensive() {
        val s = BuyCheapSellExpensiveStrategy(1.0, 1.0)
        assertFalse(s.shouldBuy(10.0, 11.0, 0.5))
    }

    @Test
    fun shouldSell() {
        val s = BuyCheapSellExpensiveStrategy(1.0, 1.0)
        assertTrue(s.shouldSell(10.0, 20.0, 1.0))
    }

    @Test
    fun shouldSellTooCheap() {
        val s = BuyCheapSellExpensiveStrategy(1.0, 1.0)
        assertFalse(s.shouldSell(10.0, 19.0, 1.0))
    }
}