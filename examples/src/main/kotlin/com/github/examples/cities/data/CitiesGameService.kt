package com.github.examples.cities.data

import com.github.examples.cities.data.models.City
import com.github.examples.cities.data.models.User
import java.io.File

class CitiesGameService {

    private val users = mutableMapOf<String, User>()
    private val currentCityForUser = mutableMapOf<String, City>()
    private val unusedCitiesForUser = mutableMapOf<String, MutableList<City>>()
    private val allCities = mutableListOf<City>()

    init {
        /**
         * Cities [Source](https://github.com/queenofpigeons/SQL_lab/blob/main/Список%20городов%20России.txt)
         * */
        val citiesFile = File("examples/src/main/resources/cities.txt")
        citiesFile.readLines().forEach { line ->
            val city = City(name = line)
            allCities.add(city)
        }
    }

    fun isUserExisting(id: String): Boolean {
        return users.containsKey(id)
    }

    fun createUser(id: String, firstName: String) {
        val user = User(id = id, firstName = firstName)
        users[id] = user
    }

    fun startGame(userId: String): City {
        val city = allCities.random()
        currentCityForUser[userId] = city

        val unusedCities = mutableListOf<City>().apply {
            addAll(allCities)
            remove(city)
        }
        unusedCitiesForUser[userId] = unusedCities

        return city
    }

    fun processUserAnswer(userId: String, cityName: String): String {
        val city = allCities.firstOrNull { it.name.equals(cityName.trim(), ignoreCase = true) }
        if (city == null) return "Я не знаю такого города."

        val currentCity = currentCityForUser[userId]!!
        val lastChar = getEffectiveLastChar(currentCity.name)
        val unusedCities = unusedCitiesForUser[userId]!!

        return when {
            !unusedCities.contains(city) -> "Такой город уже был."
            cityName.first().lowercaseChar() != lastChar -> "Город должен начинаться на букву '$lastChar'."
            else -> generateNextCity(userId, city)
        }
    }

    private fun generateNextCity(userId: String, city: City): String {
        val unusedCities = unusedCitiesForUser[userId]!!
        unusedCities.remove(city)

        val lastChar = getEffectiveLastChar(city.name)
        val nextCity = unusedCities.firstOrNull { it.name.first().lowercaseChar() == lastChar }

        return if (nextCity == null) {
            "Поздравляю, вы победили! Начинаем заново: ${startGame(userId).name}."
        } else {
            unusedCities.remove(nextCity)
            currentCityForUser[userId] = nextCity
            nextCity.name
        }
    }

    private fun getEffectiveLastChar(cityName: String): Char {
        val trimmedName = cityName.trim().lowercase()
        return when (val lastChar = trimmedName.last()) {
            'ь', 'ъ', 'ы', 'й' -> trimmedName[trimmedName.length - 2]
            else -> lastChar
        }
    }
}
