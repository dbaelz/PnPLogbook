package de.dbaelz.pnp.logbook.di

import de.dbaelz.pnp.logbook.features.actionlog.ActionLogRepository
import de.dbaelz.pnp.logbook.features.currency.CurrencyRepository
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepository
import de.dbaelz.pnp.logbook.features.logbook.LogbookRepository
import de.dbaelz.pnp.logbook.features.subject.SubjectRepository
import org.koin.dsl.module

val koinModule = module {
    single { LogbookRepository() }
    single { ExperienceRepository() }
    single { CurrencyRepository() }
    single { SubjectRepository() }

    single { ActionLogRepository() }
}