package de.dbaelz.pnp.logbook.di

import de.dbaelz.pnp.logbook.app.AppViewModel
import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.ApiRoute.*
import de.dbaelz.pnp.logbook.features.currency.CurrencyRepository
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModel
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepository
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModel
import de.dbaelz.pnp.logbook.features.logbook.LogbookRepository
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModel
import de.dbaelz.pnp.logbook.features.subject.SubjectRepository
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModel
import de.dbaelz.pnp.logbook.network.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() }

    single { LogbookRepository(get()) }
    single { ExperienceRepository(get()) }
    single { CurrencyRepository(get()) }
    single(named(PERSONS.resource)) { SubjectRepository(get(), PERSONS.resource) }
    single(named(GROUPS.resource)) { SubjectRepository(get(), GROUPS.resource) }
    single(named(PLACES.resource)) { SubjectRepository(get(), PLACES.resource) }

    viewModelOf(::AppViewModel)
    viewModelOf(::LogbookViewModel)
    viewModelOf(::ExperienceViewModel)
    viewModelOf(::CurrencyViewModel)
    viewModel(named(PERSONS.resource)) { SubjectViewModel(get(named(PERSONS.resource))) }
    viewModel(named(GROUPS.resource)) { SubjectViewModel(get(named(GROUPS.resource))) }
    viewModel(named(PLACES.resource)) { SubjectViewModel(get(named(PLACES.resource))) }
}
