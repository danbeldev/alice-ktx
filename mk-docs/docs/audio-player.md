# Audio Player

[Подробнее #1](https://yandex.ru/dev/dialogs/alice/doc/ru/response-audio-player)  
[Подробнее #2](https://yandex.ru/dev/dialogs/alice/doc/ru/request-audioplayer)

```kotlin
response {
    shouldListen = false
    audioPlayer {
        url = "https://example.com/stream-audio-url"
        title = "Песня"
        subTitle = "Артист"
        artUrl = "https://example.com/art.png"
        backgroundImageUrl = "https://example.com/background-image.png"
    }
}
```

## Примеры

- [AudioPlayer.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/AudioPlayer.kt)