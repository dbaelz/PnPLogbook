package de.dbaelz.pnp.logbook.di

import de.dbaelz.pnp.logbook.features.actionlog.ActionLogRepository
import de.dbaelz.pnp.logbook.features.actionlog.ActionLogRepositoryImpl
import de.dbaelz.pnp.logbook.features.currency.CurrencyRepository
import de.dbaelz.pnp.logbook.features.currency.CurrencyRepositoryImpl
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepository
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepositoryImpl
import de.dbaelz.pnp.logbook.features.logbook.LogbookRepository
import de.dbaelz.pnp.logbook.features.logbook.LogbookRepositoryImpl
import de.dbaelz.pnp.logbook.features.subject.SubjectRepository
import de.dbaelz.pnp.logbook.features.subject.SubjectRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single<LogbookRepository> { LogbookRepositoryImpl() }
    single<ExperienceRepository> { ExperienceRepositoryImpl() }
    single<CurrencyRepository> { CurrencyRepositoryImpl() }
    single<SubjectRepository> { SubjectRepositoryImpl() }

    single<ActionLogRepository> { ActionLogRepositoryImpl() }
}