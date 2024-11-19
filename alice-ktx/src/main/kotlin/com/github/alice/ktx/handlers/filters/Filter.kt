package com.github.alice.ktx.handlers.filters

import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.request.RequestContentType

interface Filter {
    fun checkFor(request: ShouldRequestEnvironment): Boolean = request.predicate()
    fun ShouldRequestEnvironment.predicate(): Boolean

    infix fun and(otherFilter: Filter): Filter = object : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean =
            this@Filter.checkFor(this) && otherFilter.checkFor(this)
    }

    infix fun or(otherFilter: Filter): Filter = object : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean =
            this@Filter.checkFor(this) || otherFilter.checkFor(this)
    }

    operator fun not(): Filter = object : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = !this@Filter.checkFor(this)
    }

    class Custom(private val customPredicate: ShouldRequestEnvironment.() -> Boolean) : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = customPredicate()
    }

    object All : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = true
    }

    class Text(private val text: String? = null): Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean {
            return text?.let {
                message.request.originalUtterance?.contains(it, ignoreCase = true) == true
            } ?: (message.request.originalUtterance != null)
        }
    }

    class Type(private val type: RequestContentType) : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.request.type == type
    }

    /**
     * Пользователь может видеть ответ навыка на экране и открывать ссылки в браузере.
     * */
    object Screen : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.meta.interfaces.screen != null
    }

    /**
     * У пользователя есть возможность запросить [связку аккаунтов](https://yandex.ru/dev/dialogs/alice/doc/ru/auth/when-to-use).
     * */
    object AccountLinking : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.meta.interfaces.accountLinking != null
    }

    /**
     * На устройстве пользователя есть аудиоплеер.
     * */
    object AudioPlayer : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.meta.interfaces.audioPlayer != null
    }

    /**
     * Признак новой сессии.
     * */
    object NewSession : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.session.new
    }

    object Authorized : Filter {
        override fun ShouldRequestEnvironment.predicate(): Boolean = message.session.user?.userId != null
    }
}
