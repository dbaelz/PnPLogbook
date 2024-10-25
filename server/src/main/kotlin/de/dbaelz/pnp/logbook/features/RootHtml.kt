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
            +"This is the root page of the PnP Logbook. The REST API is available at $apiBasePath"
            ul {
                listOf(
                    ApiRoute.LOGBOOK.fullResourcePath to "Logbook",
                    ApiRoute.EXPERIENCE.fullResourcePath to "Experience",
                    ApiRoute.CURRENCY.fullResourcePath to "Currency",
                    ApiRoute.PERSONS.fullResourcePath to "Persons",
                    ApiRoute.GROUPS.fullResourcePath to "Groups",
                    ApiRoute.PLACES.fullResourcePath to "Places"
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