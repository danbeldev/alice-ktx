package com.github.alice.ktx.storage.apiStorage

/**
 * Если класс, реализующий интерфейс [com.github.alice.ktx.storage.Storage], помечен аннотацией `@EnableApiStorage`,
 * данные из этого хранилища автоматически сохраняются в хранилище Алисы.
 * Метод [com.github.alice.ktx.storage.Storage.getData] определяет, какие данные будут отправлены для сохранения в хранилище Алисы.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnableApiStorage