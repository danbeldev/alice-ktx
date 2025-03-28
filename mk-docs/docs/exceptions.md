# Обработка исключений

`responseFailure` — это расширение для Dispatcher, которое позволяет обрабатывать ошибки, возникающие при выполнении
запросов.
Оно предоставляет возможность задать обработчики ошибок для различных типов исключений и условий.

## Примеры использования

- Обработка конкретного исключения:

```kotlin
responseFailure(ArithmeticException::class) {
    response {
        text = "Произошла арифметическая ошибка"
    }
}
```

- Общий обработчик исключений:

```kotlin
responseFailure {
    response {
        text = "Произошла ошибка"
    }
}
```

- Обработка ошибок по условию:

```kotlin
responseFailure({ message.session.new }) {
    response {
        text = "В начале сессии произошла ошибка"
    }
}
```

## Примеры

- [ErrorHandling.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/ErrorHandling.kt)
