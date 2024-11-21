# Хэндлеры

**Хэндлеры** (Handlers) — это обработчики входящих запросов, которые позволяют реализовать бизнес-логику навыка, обрабатывая события от Алисы. Хэндлер определяет, как и когда реагировать на запросы, а также возвращает сформированный ответ.

Библиотека предоставляет готовый набор хэндлеров, которые можно найти в пакете [handlers.impl](https://github.com/danbeldev/alice-ktx/tree/master/alice-ktx/src/main/kotlin/com/github/alice/ktx/handlers/impl).

Каждый хэндлер реализует два основных метода:

1. **`shouldHandle`** — определяет, подходит ли данный хэндлер для обработки текущего сообщения.
2. **`processRequest`** — выполняет обработку сообщения и формирует ответ.

```kotlin
interface Handler {

    suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean

    suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse
}
```

## Environment

Хэндлеры работают в контексте окружения (Environment), которое используется для обработки запросов:

- **`ShouldRequestEnvironment`** — окружение для метода `shouldHandle`.

```kotlin
interface ShouldRequestEnvironment {
    val message: MessageRequest // Тело запроса.
    val context: ReadOnlyFSMContext // FSM Context, доступный только для чтения.
    val dialogApi: DialogApi? // Методы взаимодействия с API Диалогов.

    fun isValidFor(filter: Filter): Boolean {
        return filter.checkFor(this)
    }
}
```

- **`ProcessRequestEnvironment`** — окружение для метода `processRequest`. Расширяет `ShouldRequestEnvironment` и дополнительно предоставляет:

```kotlin
interface ProcessRequestEnvironment : ShouldRequestEnvironment {
    override val context: MutableFSMContext // FSM Context с возможностью изменения.
}
```

## Виды хэндлеров

### 1. Request

Обрабатывает **все входящие запросы**, предоставляя возможность работать с любыми событиями, независимо от их типа.

#### Kotlin DSL:
```kotlin
request( 
    shouldHandle = { // this: ShouldRequestEnvironment
        true
    },
    processRequest = { // this: ProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 2. Message

Обрабатывает сообщения от пользователя, тип запроса `SimpleUtterance`.

#### Environment

**`MessageShouldHandleEnvironment`**:

```kotlin
data class MessageShouldHandleEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    val command = request.message.request.command ?: "" // нормализованный текст запроса. 
    val messageText = request.message.request.originalUtterance ?: "" // полный текст пользовательского запроса.
    val nlu = request.message.request.nlu // слова и именованные сущности, извлечённые из запроса пользователя.
}
```

**`MessageProcessRequestEnvironment`**: включает всё, что есть в `MessageShouldHandleEnvironment`.

#### Kotlin DSL:
```kotlin
message( 
    shouldHandle = { // this: MessageShouldHandleEnvironment
        true
    },
    processRequest = { // this: MessageProcessRequestEnvironment
        response { 
            text = messageText
         }
    }
)
```

---

### 3. ButtonPressed

Обрабатывает события, связанные с нажатием кнопок, тип запроса `ButtonPressed`.

Срабатывает, если:

1. Нажата отдельная кнопка (`hide: true`) с заполненным `payload`.
2. Нажато изображение с `payload` в `card.button`.
3. Нажат элемент списка с `payload` в `items.button`.
4. Выбрано изображение из галереи с `payload` в `items.button`.


#### Environment

**`ButtonPressedShouldHandleEnvironment`**:

```kotlin
data class ButtonPressedShouldHandleEnvironment(
    private val requestEnvironment: ShouldRequestEnvironment
): ShouldRequestEnvironment by requestEnvironment {

    // Map<String, String> JSON-объект, полученный с нажатой кнопкой от обработчика навыка (в ответе на предыдущий запрос)
    val payload = requestEnvironment.message.request.payload 
}
```

#### Kotlin DSL:
```kotlin
buttonPressed( 
    shouldHandle = { // this: ButtonPressedShouldHandleEnvironment
        true
    },
    processRequest = { // this: ProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 4. NewSession

Срабатывает, если запрос является первым в текущей сессии.

#### Kotlin DSL:
```kotlin
newSession( 
    shouldHandle = { // this: ShouldRequestEnvironment
        true
    },
    processRequest = { // this: ProcessRequestEnvironment
        response { 
            text = messageText
         }
    }
)
```

---

### 5. AudioPlayer

Обрабатывает запросы, связанные с воспроизведением аудио.
Срабатывает при следующих типах запросов:

- `AudioPlayerPlaybackStarted`
- `AudioPlayerPlaybackFinished`
- `AudioPlayerPlaybackNearlyFinished`
- `AudioPlayerPlaybackStopped`
- `AudioPlayerPlaybackFailed`

#### Environment

`AudioPlayerShouldRequestEnvironment`:

```kotlin
data class AudioPlayerProcessRequestEnvironment(
    private val request: ProcessRequestEnvironment
) : ProcessRequestEnvironment by request {

    val type = message.request.type // тип запроса.
    val error = message.request.error // информация об ошибке (если есть).
}
```

- **`AudioPlayerProcessRequestEnvironment`**: содержит те же поля, что и `AudioPlayerShouldRequestEnvironment`.

#### Kotlin DSL:
```kotlin
audioPlayer( 
    shouldHandle = { // this: AudioPlayerShouldRequestEnvironment
        true
    },
    processRequest = { // this: AudioPlayerProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 6. PurchaseConfirmation

Обрабатывает запросы, связанные с подтверждением оплаты, тип запроса `PurchaseConfirmation`.

#### Environment

`PurchaseConfirmationShouldRequestEnvironment`:

```kotlin
data class PurchaseConfirmationShouldRequestEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    // идентификатор заказа.
    val purchaseRequestId = message.request.purchaseRequestId 

    // идентификатор транзакции.
    val purchaseToken = message.request.purchaseToken 

    // идентификатор заказа.
    val orderId = message.request.orderId 

    // время оплаты (в миллисекундах с 01.01.1970 00:00:00 UTC).
    val purchaseTimestamp = message.request.purchaseTimestamp 

    // данные, переданные при запуске сценария оплаты.
    val purchasePayload = message.request.purchasePayload 

    // строка для подписи.
    val signedData = message.request.signedData

    // цифровая подпись.
    val signature = message.request.signature 
}
```

`PurchaseConfirmationProcessRequestEnvironment` содержит те же поля, что и `PurchaseConfirmationShouldRequestEnvironment`.

#### Kotlin DSL:
```kotlin
purchaseConfirmation( 
    shouldHandle = { // this: PurchaseConfirmationShouldRequestEnvironment
        true
    },
    processRequest = { // this: PurchaseConfirmationProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 7. ShowPull

Обрабатывает запросы, связанные с запуском утреннего шоу Алисы.

#### Environment

`ShowPullShouldRequestEnvironment`:

```kotlin
data class ShowPullShouldRequestEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    // тип шоу.
    val showType = checkNotNull(request.message.request.showType)
}

```

`ShowPullProcessRequestEnvironment`: включает те же поля, что и `ShowPullShouldRequestEnvironment`.

#### Kotlin DSL:
```kotlin
showPull( 
    shouldHandle = { // this: ShowPullShouldRequestEnvironment
        true
    },
    processRequest = { // this: ShowPullProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 8. Help

Срабатывает при запросе помощи, если команда пользователя соответствует запросу "помощь".

#### Kotlin DSL:
```kotlin
help( 
    shouldHandle = { // this: ShouldRequestEnvironment
        true
    },
    processRequest = { // this: ProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

### 9. WhatCanYouDo

Срабатывает при запросе "Что ты умеешь?".

#### Kotlin DSL:
```kotlin
whatCanYouDo( 
    shouldHandle = { // this: ShouldRequestEnvironment
        true
    },
    processRequest = { // this: ProcessRequestEnvironment
        response { 
            text = "Response"
         }
    }
)
```

---

## Примечания

!!! info "Примечание"
    Вы можете создать собственный хэндлер, реализовав интерфейс [Handler](https://github.com/danbeldev/alice-ktx/blob/master/alice-ktx/src/main/kotlin/com/github/alice/ktx/handlers/Handler.kt).

!!! info "Примечание"
    Вы можете использовать [фильтры](https://github.com/danbeldev/alice-ktx/blob/master/alice-ktx/src/main/kotlin/com/github/alice/ktx/handlers/filters/Filter.kt) в блоке `shouldHandle` и `processRequest`.


## Примеры

- [FsmForm.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/FsmForm.kt)
- [Handlers.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/Handlers.kt)
- [Filters.kt](https://github.com/danbeldev/alice-ktx/blob/master/examples/src/main/kotlin/com/github/examples/Filters.kt)