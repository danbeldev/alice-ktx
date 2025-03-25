package com.github.alice.ktx.models

import com.github.alice.ktx.models.response.button.button
import com.github.alice.ktx.models.response.button.mediaButton
import com.github.alice.ktx.models.response.card.CardType
import com.github.alice.ktx.models.response.card.cardBigImage
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.testutils.builders.ProcessRequestEnvironmentBuilder.Companion.processRequestEnvironment
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class MessageResponseTest {

    @Test
    fun `response should preserve original message version`() = runTest {
        val processRequestEnvironment = processRequestEnvironment()

        val result = processRequestEnvironment.response {}

        Assert.assertEquals(processRequestEnvironment.message.version, result.version)
    }

    @Test
    fun `response builder should set all basic response fields correctly`() = runTest {
        val processRequestEnvironment = processRequestEnvironment()
        val responseText = "test message"
        val responseTts = "tts message"
        val responseEnsSession = true
        val responseShouldListen = true

        val result = processRequestEnvironment.response {
            text = responseText
            tts = responseTts
            endSession = responseEnsSession
            shouldListen = responseShouldListen
        }

        Assert.assertEquals(responseText, result.response?.text)
        Assert.assertEquals(responseTts, result.response?.tts)
        Assert.assertEquals(responseEnsSession, result.response?.endSession)
        Assert.assertEquals(responseShouldListen, result.response?.shouldListen)
    }

    @Test
    fun `button builder should create button with all specified properties`() = runTest {
        val processRequestEnvironment = processRequestEnvironment()
        val buttonTitle = "title button"
        val buttonUrl = "url button"
        val buttonHide = false
        val buttonPayload = mapOf("key" to "value")

        val result = processRequestEnvironment.response {
            button {
                title = buttonTitle
                url = buttonUrl
                hide = buttonHide
                payload = buttonPayload
            }
        }

        Assert.assertEquals(1, result.response?.buttons?.size)
        Assert.assertEquals(buttonTitle, result.response?.buttons?.first()?.title)
        Assert.assertEquals(buttonUrl, result.response?.buttons?.first()?.url)
        Assert.assertEquals(buttonHide, result.response?.buttons?.first()?.hide)
        Assert.assertEquals(buttonPayload, result.response?.buttons?.first()?.payload)
    }

    @Test
    fun `cardBigImage builder should set all card properties and media button correctly`() = runTest {
        val processRequestEnvironment = processRequestEnvironment()
        val cardImageId = "image id"
        val cardTitle = "card title"
        val cardDescription = "card description"
        val mediaButtonText = "button text"
        val mediaButtonUrl = "button url"
        val mediaButtonPayload = mapOf("key" to "value")

        val result = processRequestEnvironment.response {
            cardBigImage {
                imageId = cardImageId
                title = cardTitle
                description = cardDescription
                mediaButton {
                    text = mediaButtonText
                    url = mediaButtonUrl
                    payload = mediaButtonPayload
                }
            }
        }

        Assert.assertTrue(result.response?.card?.type == CardType.BigImage)
        Assert.assertEquals(cardImageId, result.response?.card?.imageId)
        Assert.assertEquals(cardTitle, result.response?.card?.title)
        Assert.assertEquals(cardDescription, result.response?.card?.description)
        Assert.assertEquals(mediaButtonText, result.response?.card?.button?.text)
        Assert.assertEquals(mediaButtonUrl, result.response?.card?.button?.url)
        Assert.assertEquals(mediaButtonPayload, result.response?.card?.button?.payload)
    }
}