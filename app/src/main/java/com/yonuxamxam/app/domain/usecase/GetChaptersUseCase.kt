package com.yonuxamxam.app.domain.usecase

import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.domain.repository.HomeRepository

/**
 * Cas d'utilisation : Récupérer la liste des chapitres d'une classe et d'une matière.
 */
class GetChaptersUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(classe: String, matiere: String): List<Chapitre> {
        return repository.getChapitres(classe, matiere)
    }
}

