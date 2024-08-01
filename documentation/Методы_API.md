## Методы API

Чтобы получить, загрузить и удалить загруженные изображения и звуки, 
надо передать [OAuth Token](https://yandex.ru/dev/direct/doc/start/token.html) при создании `DialogApi`:

```kotlin
skill {
    // ...
    dialogApi = ktorYandexDialogApi {
        oauthToken = "..."
    }
    // ...
}.run()
```

Для каждого аккаунта Яндекса на Диалоги можно загрузить не больше `100 МБ` картинок и `1 ГБ` аудио. 
Чтобы узнать, сколько места уже занято, используйте этот метод. 

```kotlin
dialogApi?.getStatus()
```

### Изображения ([Документация](https://yandex.ru/dev/dialogs/alice/doc/ru/resource-upload#http-images-load__quota))

- Список изображений, загруженных для навыка, можно получить этим методом.
```kotlin
dialogApi?.getAllImages()
```

- Чтобы загрузить картинку для навыка из интернета, передайте URL картинки в метод.
```kotlin
dialogApi?.uploadImage(url = "https://example.com")
```

- Чтобы загрузить файл, передайте `File` в метод.
```kotlin
val file = File("example.png")
dialogApi?.uploadImage(file = file)
```

- Чтобы удалить загруженное изображение, передайте его идентификатор в этот метод.
```kotlin
dialogApi?.deleteImage(imageId)
```

### Аудио ([Документация](https://yandex.ru/dev/dialogs/alice/doc/ru/resource-sounds-upload))

- Чтобы посмотреть аудиофайлы, загруженные для навыка, используйте этот метод.
```kotlin
dialogApi?.getAllSounds()
```

- Аудио можно загрузить только файлом, передайте `File` в метод.
```kotlin
val file = File("example.mp3")
dialogApi?.uploadSound(file = file)
```

- Чтобы удалить загруженное аудио, передайте его идентификатор в этот метод.
```kotlin
dialogApi?.deleteSound(soundId)
```

### Response

Все методы API возвращают обёртку `Response<>`.
```kotlin
sealed interface Response<T> {
    data class Failed<T>(val message: String): Response<T>
    data class Success<T>(val data: T): Response<T>
}
```

### Примеры
[ImageDialogsApi.kt](../examples/src/main/kotlin/com/github/examples/ImageDialogsApi.kt)
