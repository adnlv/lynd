package com.adnlv.lynd

import com.adnlv.lynd.data.db.PayoutRow
import com.adnlv.lynd.domain.PayoutType
import com.adnlv.lynd.domain.calculateTotalAmount
import com.adnlv.lynd.domain.displayTypeLabel
import com.adnlv.lynd.domain.isRedemption
import com.adnlv.lynd.domain.resolvePayoutType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class PayoutRowExtensionsTest {
    private fun createRow(
        payType: String,
        payVal: BigDecimal = BigDecimal("50.00"),
        quantity: Int = 10,
    ): PayoutRow =
        PayoutRow(
            isin = "UA4000187348",
            bondName = "Test Bond",
            payDate = LocalDate.of(2026, 6, 15),
            payType = payType,
            payVal = payVal,
            quantity = quantity,
            currency = "UAH",
            purchaseDate = LocalDate.of(2025, 1, 1),
        )

    @Test
    fun isRedemption_returnsTrueForRedemptionCodeAndString() {
        assertTrue(createRow(payType = "2").isRedemption)
        assertTrue(createRow(payType = "redemption").isRedemption)
        assertTrue(createRow(payType = "REDEMPTION").isRedemption)
    }

    @Test
    fun isRedemption_returnsFalseForCouponAndOtherTypes() {
        assertFalse(createRow(payType = "1").isRedemption)
        assertFalse(createRow(payType = "coupon").isRedemption)
        assertFalse(createRow(payType = "dividend").isRedemption)
    }

    @Test
    fun resolvePayoutType_returnsExpectedEnum() {
        assertEquals(PayoutType.REDEMPTION, createRow(payType = "2").resolvePayoutType())
        assertEquals(PayoutType.REDEMPTION, createRow(payType = "redemption").resolvePayoutType())
        assertEquals(PayoutType.COUPON, createRow(payType = "1").resolvePayoutType())
        assertEquals(PayoutType.COUPON, createRow(payType = "coupon").resolvePayoutType())
    }

    @Test
    fun displayTypeLabel_mapsStandardCodesAndLabels() {
        assertEquals("Coupon", createRow(payType = "1").displayTypeLabel())
        assertEquals("Coupon", createRow(payType = "coupon").displayTypeLabel())
        assertEquals("Coupon", createRow(payType = "COUPON").displayTypeLabel())
        assertEquals("Redemption", createRow(payType = "2").displayTypeLabel())
        assertEquals("Redemption", createRow(payType = "redemption").displayTypeLabel())
        assertEquals("Redemption", createRow(payType = "REDEMPTION").displayTypeLabel())
    }

    @Test
    fun displayTypeLabel_fallsBackToCapitalizedCustomType() {
        assertEquals("Dividend", createRow(payType = "dividend").displayTypeLabel())
        assertEquals("Bonus", createRow(payType = "bonus").displayTypeLabel())
    }

    @Test
    fun calculateTotalAmount_multipliesValueByQuantity() {
        val row = createRow(payType = "1", payVal = BigDecimal("75.50"), quantity = 4)
        assertEquals(BigDecimal("302.00"), row.calculateTotalAmount())
    }
}
