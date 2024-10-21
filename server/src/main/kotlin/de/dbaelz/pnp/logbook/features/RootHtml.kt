package de.dbaelz.pnp.logbook.features

import kotlinx.html.*

fun rootHtml(): HTML.() -> Unit = {
    head {
        title("PnP Logbook")
    }
    body {
        h1 {
            +"Welcome to the PnP Logbook"
        }
        p {
            +"This is the root page of the PnP Logbook. The REST API is available at /api"
            ul {
                listOf(
                    "/api/logbook" to "Logbook",
                    "/api/experience" to "Experience",
                    "/api/currency" to "Currency",
                    "/api/persons" to "Persons",
                    "/api/groups" to "Groups",
                    "/api/places" to "Places"
                ).forEach { (href, text) ->
                    li {
                        a(href = href) {
                            +text
                        }
                    }
                }
            }
        }
    }
}