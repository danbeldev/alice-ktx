# Авторизация

[Подробнее](https://yandex.ru/dev/dialogs/alice/doc/ru/auth/when-to-use)

```kotlin
message {
    authorization(onAlreadyAuthenticated = {
        response {
            text = "Already Authenticated"
        }
    }, onAuthorizationFailed = {
        response {
            text = "Authorization Failed"
        }
    })
}
```

## Примеры

- [Authorization.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/Authorization.kt)
