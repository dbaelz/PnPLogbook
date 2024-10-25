package de.dbaelz.pnp.logbook.features

enum class ApiRoute(override val resource: String) : BaseRoute {
    EXPERIENCE("experience"),
    CURRENCY("currency"),
    LOGBOOK("logbook"),
    PERSONS("persons"),
    GROUPS("groups"),
    PLACES("places"),

    ACTION_LOG("actionlog"),
}

interface BaseRoute {
    val resource: String

    val resourcePath: String
        get() = "/$resource"

    val fullResourcePath: String
        get() = "$apiBasePath/$resource"
}

val apiResource: String
    get() = "api"

val apiBasePath: String
    get() = "/$apiResource"




