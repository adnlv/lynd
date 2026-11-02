package com.adnlv.lynd

import com.adnlv.lynd.core.AppConstants
import com.adnlv.lynd.data.network.NbuPaymentDto
import com.adnlv.lynd.data.network.NbuSecurityDto
import com.adnlv.lynd.data.network.PAYMENT_TYPE_COUPON
import com.adnlv.lynd.data.network.PAYMENT_TYPE_REDEMPTION
import com.adnlv.lynd.data.network.toBondEntity
import com.adnlv.lynd.data.network.toBondPaymentEntities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class NbuMappersTest {
    @Test
    fun toBondEntity_withAllFieldsPresent_mapsCorrectly() {
        val dto =
            NbuSecurityDto(
                cpcode = "UA4000187348",
                nominal = 1000.0,
                aukProc = 15.5,
                pgsDate = "2027-06-15",
                valCode = "USD",
                emitName = "Gov Bond USD",
            )

        val entity = dto.toBondEntity("UA4000187348")

        assertEquals("UA4000187348", entity.isin)
        assertEquals("Gov Bond USD", entity.name)
        assertEquals("USD", entity.currency)
        assertEquals(BigDecimal.valueOf(1000.0), entity.nominalValue)
        assertEquals(BigDecimal.valueOf(15.5), entity.couponRate)
        assertEquals(LocalDate.of(2027, 6, 15), entity.maturityDate)
    }

    @Test
    fun toBondEntity_withNullFields_appliesFallbacks() {
        val dto =
            NbuSecurityDto(
                cpcode = null,
                nominal = null,
                aukProc = null,
                pgsDate = null,
                valCode = null,
                emitName = null,
            )

        val beforeTime = LocalDate.now().minusDays(1)
        val entity = dto.toBondEntity("UA4000187348")
        val afterTime = LocalDate.now().plusDays(1)

        assertEquals("UA4000187348", entity.isin)
        assertEquals("UA4000187348", entity.name)
        assertEquals(AppConstants.DEFAULT_CURRENCY, entity.currency)
        assertEquals(AppConstants.DEFAULT_NOMINAL_VALUE, entity.nominalValue)
        assertEquals(BigDecimal.ZERO, entity.couponRate)
        assertTrue(!entity.maturityDate.isBefore(beforeTime) && !entity.maturityDate.isAfter(afterTime))
    }

    @Test
    fun toBondPaymentEntities_withStandardCodes_mapsCorrectly() {
        val dtos =
            listOf(
                NbuPaymentDto(payDate = "2026-03-15", payType = "1", payVal = 75.0),
                NbuPaymentDto(payDate = "2026-09-15", payType = "2", payVal = 1000.0),
            )

        val entities = dtos.toBondPaymentEntities("UA4000187348")

        assertEquals(2, entities.size)
        assertEquals("UA4000187348", entities[0].bondIsin)
        assertEquals(LocalDate.of(2026, 3, 15), entities[0].payDate)
        assertEquals(PAYMENT_TYPE_COUPON, entities[0].payType)
        assertEquals(BigDecimal.valueOf(75.0), entities[0].payVal)

        assertEquals(PAYMENT_TYPE_REDEMPTION, entities[1].payType)
        assertEquals(BigDecimal.valueOf(1000.0), entities[1].payVal)
    }

    @Test
    fun toBondPaymentEntities_withCustomAndNullPayType_mapsExpectedType() {
        val dtos =
            listOf(
                NbuPaymentDto(payDate = "2026-03-15", payType = "dividend", payVal = 50.0),
                NbuPaymentDto(payDate = "2026-09-15", payType = null, payVal = 50.0),
            )

        val entities = dtos.toBondPaymentEntities("UA4000187348")

        assertEquals(2, entities.size)
        assertEquals("dividend", entities[0].payType)
        assertEquals(PAYMENT_TYPE_COUPON, entities[1].payType)
    }

    @Test
    fun toBondPaymentEntities_withNullPayDate_skipsItem() {
        val dtos =
            listOf(
                NbuPaymentDto(payDate = null, payType = "1", payVal = 75.0),
                NbuPaymentDto(payDate = "2026-03-15", payType = "1", payVal = 75.0),
            )

        val entities = dtos.toBondPaymentEntities("UA4000187348")

        assertEquals(1, entities.size)
        assertEquals(LocalDate.of(2026, 3, 15), entities[0].payDate)
    }

    @Test
    fun toBondPaymentEntities_withNullPayVal_defaultsToZero() {
        val dtos =
            listOf(
                NbuPaymentDto(payDate = "2026-03-15", payType = "1", payVal = null),
            )

        val entities = dtos.toBondPaymentEntities("UA4000187348")

        assertEquals(1, entities.size)
        assertEquals(BigDecimal.ZERO, entities[0].payVal)
    }

    @Test
    fun toBondPaymentEntities_withNullAndEmptyList_returnsEmptyList() {
        val nullList: List<NbuPaymentDto>? = null
        assertTrue(nullList.toBondPaymentEntities("UA4000187348").isEmpty())
        assertTrue(emptyList<NbuPaymentDto>().toBondPaymentEntities("UA4000187348").isEmpty())
    }
}
