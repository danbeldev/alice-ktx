package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.storage.models.StorageKey
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.util.*

class MemoryStorageTest {

    private val storage = MemoryStorage(json = Json)

    private val defaultKey = StorageKey(
        skillId = UUID.randomUUID().toString(),
        userId = UUID.randomUUID().toString(),
        sessionId = UUID.randomUUID().toString(),
        applicationId = UUID.randomUUID().toString(),
        strategy = FSMStrategy.USER
    )

    @Serializable
    private data class User(
        val name: String
    )

    @After
    fun after() = runTest {
        storage.clear(defaultKey)
    }

    @Test
    fun `set and get state`() = runTest {
        storage.setState(defaultKey, "SET_TEST_1")
        Assert.assertEquals(storage.getState(defaultKey), "SET_TEST_1")

        storage.setState(defaultKey, "SET_TEST_2")
        Assert.assertEquals(storage.getState(defaultKey), "SET_TEST_2")
    }

    @Test
    fun `set and get data`() = runTest {
        storage.setData(defaultKey, "test_1" to "a", "test_2" to "b")

        Assert.assertEquals(storage.getData(defaultKey)["test_1"], "a")
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), "b")

        storage.setData(defaultKey, "test_3" to "c")
        Assert.assertEquals(storage.getData(defaultKey, "test_3"), "c")
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), null)
    }

    @Test
    fun `set and get data type`() = runTest {
        val user1 = User(name = "DanBel_1")
        val user2 = User(name = "DanBel_2")

        storage.setTypedData(defaultKey, User::class, "user_1" to user1, "user_2" to user2)

        Assert.assertEquals(storage.getTypedData(defaultKey, "user_1", User::class), user1)
        Assert.assertEquals(storage.getTypedData(defaultKey, "user_2", User::class), user2)
    }

    @Test
    fun `update and get data`() = runTest {
        storage.setData(defaultKey, "test_1" to "a")

        Assert.assertEquals(storage.getData(defaultKey, "test_1"), "a")

        storage.updateData(defaultKey, "test_2" to "b")

        Assert.assertEquals(storage.getData(defaultKey, "test_1"), "a")
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), "b")

        storage.updateData(defaultKey, "test_1" to "c")

        Assert.assertEquals(storage.getData(defaultKey, "test_1"), "c")
    }

    @Test
    fun `update and get data type`() = runTest {
        val user1 = User(name = "DanBel_1")
        val user2 = User(name = "DanBel_2")

        storage.setTypedData(defaultKey, User::class, "test_1" to user1)

        Assert.assertEquals(storage.getTypedData(defaultKey, "test_1", User::class), user1)

        storage.updateTypedData(defaultKey, User::class, "test_1" to user2)

        Assert.assertEquals(storage.getTypedData(defaultKey, "test_1", User::class), user2)
    }

    @Test
    fun `remove and get data`() = runTest {
        storage.setData(defaultKey, "test_1" to "a", "test_2" to "b")

        Assert.assertEquals(storage.getData(defaultKey, "test_1"), "a")
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), "b")

        val result = storage.removeData(defaultKey, "test_1")
        Assert.assertEquals("a", result)

        Assert.assertEquals(storage.getData(defaultKey, "test_1"), null)
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), "b")
    }

    @Test
    fun `remove and get data type`() = runTest {
        val user1 = User(name = "DanBel_1")

        storage.setTypedData(defaultKey, User::class, "user_1" to user1)

        Assert.assertEquals(storage.getTypedData(defaultKey, "user_1", User::class), user1)

        val result = storage.removeTypedData(defaultKey, "user_1", User::class)
        Assert.assertEquals(user1, result)

        Assert.assertEquals(storage.getData(defaultKey, "user_1"), null)
    }

    @Test
    fun `clear data and state`() = runTest {
        storage.setState(defaultKey, "SET_TEST_1")
        storage.setData(defaultKey, "test_1" to "a", "test_2" to "b")

        Assert.assertEquals(storage.getState(defaultKey), "SET_TEST_1")
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), "b")

        storage.clear(defaultKey)

        Assert.assertEquals(storage.getState(defaultKey), null)
        Assert.assertEquals(storage.getData(defaultKey, "test_2"), null)
    }
}