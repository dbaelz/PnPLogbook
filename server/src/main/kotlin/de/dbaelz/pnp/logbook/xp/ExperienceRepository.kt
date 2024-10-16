package de.dbaelz.pnp.logbook.xp

class ExperienceRepository {
    private val experiences = mutableListOf<Experience>()

    fun getExperience(): List<Experience> {
        return experiences
    }

    fun add(experience: Experience) {
        // TODO: Check for only positive values?
        experiences.add(experience)
    }
}