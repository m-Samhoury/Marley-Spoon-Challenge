package com.challenge.marleyspoon

import com.challenge.marleyspoon.repository.Repository
import com.contentful.java.cda.CDAClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testingNetworkCall() {
        runBlocking {
            val client = CDAClient.builder()
                .setSpace(BuildConfig.API_SPACE_ID)
                .setToken(BuildConfig.API_ACCESS_TOKEN)
                .build()
            val repository = Repository(client)
            val response = repository.fetchRecipesList(200){

            }
            if (response != null) {

            } else {

            }
        }
    }
}
