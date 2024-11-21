<p align="center">
  <a href="https://github.com/danbeldev/alice-ktx/tree/master">
    <img width="200px" height="200px" alt="alice-skill" src="/mk-docs/docs/assets/alice-ktx.png">
  </a>
</p>

<div align="center">

[![Maven Central](https://badgen.net/badge/Maven%20Central/v0.1.0/blue?icon=github)](https://central.sonatype.com/artifact/io.github.danbeldev/alice-ktx)
[![License](https://img.shields.io/github/license/danbeldev/alice-ktx)](https://github.com/danbeldev/alice-ktx/blob/master/LICENSE)
![Last commit](https://img.shields.io/github/last-commit/danbeldev/alice-ktx)

</div>
<p align="center">
    <b>
        Фреймворк на Kotlin, упрощающая разработку 
        <a target="_blank" href="https://dialogs.yandex.ru/store">навыков Алисы</a>
        из
        <a target="_blank" href="https://dialogs.yandex.ru/development">Яндекс.Диалогов</a>
        .
    </b>
</p>

## Особенности
- Kotlin DSL
- Server Application ([Ktor](https://ktor.io))
- [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html)
- Асинхронность ([Coroutines](https://github.com/Kotlin/kotlinx.coroutines))
- Машина состояний (Finite State Machine)
- Мидлвари (для входящих событий и вызовов API)

## Быстрый старт

```kotlin
fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                response {
                    text = "Привет!"
                }
            }

            message {
                response {
                    text = messageText
                }
            }
        }
    }.run()
}
```

## Документация
- [Документация](mk-docs/)
- [Примеры](https://github.com/danbeldev/alice-ktx/tree/master/examples/src/main/kotlin/com/github/examples)


## Лицензия
Copyright © 2024 [DanBel](https://github.com/danbeldev) \
Этот проект использует [MIT](https://github.com/danbeldev/alice-ktx/blob/master/LICENSE) лицензию
