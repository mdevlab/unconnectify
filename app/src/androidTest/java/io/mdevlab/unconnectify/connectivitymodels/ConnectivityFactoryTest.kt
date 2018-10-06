package io.mdevlab.unconnectify.connectivitymodels

import android.support.test.InstrumentationRegistry
import io.mdevlab.unconnectify.utils.Connection
import org.junit.Assert.assertEquals
import org.junit.Test


class ConnectivityFactoryTest {

    private val connectivityFactory = ConnectivityFactory
    private val context = InstrumentationRegistry.getTargetContext()

    @Test
    fun shouldReturnSameConnectivityInstances_whenCalledMultipleTimes() {
        val firstConnectivity = connectivityFactory.get(Connection.WIFI, context)
        val secondConnectivity = connectivityFactory.get(Connection.WIFI, context)

        assertEquals(firstConnectivity, secondConnectivity)
    }
}