<p align="center">
  <a href="https://github.com/K1rl3s/aliceio">
    <img width="200px" height="200px" alt="aliceio" src="/docs/logo.png">
  </a>
</p>
<h1 align="center">
  Alice Skill
</h1>

<div align="center">

[![License](https://img.shields.io/pypi/l/aliceio.svg?style=flat)](https://github.com/K1rL3s/aliceio/blob/master/LICENSE)
[![Status](https://img.shields.io/pypi/status/aliceio.svg?style=flat)](https://pypi.org/project/aliceio/)

</div>
<p align="center">
    <b>
        Асинхронный фреймворк для разработки
        <a target="_blank" href="https://dialogs.yandex.ru/store">навыков Алисы</a>
        из
        <a target="_blank" href="https://dialogs.yandex.ru/development">Яндекс.Диалогов</a>
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
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            message({ message.session.new }) {
                response {
                    text = "Привет!"
                }
            }

            message {
                response {
                    text = message.request.command.toString()
                }
            }
        }
    }.run()
}
```

## Документация
- [Примеры](https://github.com/danbeldev/kotlin-alice-skill/tree/master/examples/src/main/kotlin/com/github/examples)


## Связь
Если у вас есть вопросы, вы можете посетить чат сообщества в Telegram
-   🇷🇺 [\@alice_skill_chat](https://t.me/alice_skill_chat)


## Лицензия
Copyright © 2024 [DanBel](https://github.com/danbeldev) \
Этот проект использует [MIT](https://github.com/K1rL3s/aliceio/blob/master/LICENSE) лицензию