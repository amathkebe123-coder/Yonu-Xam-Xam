package com.yonuxamxam.app.domain.usecase

import com.yonuxamxam.app.data.model.SearchResultItem
import com.yonuxamxam.app.domain.repository.HomeRepository

/**
 * Cas d'utilisation : Recherche de cours, leçons et exercices selon la classe active.
 */
class SearchDashboardUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(query: String, classeCode: String): List<SearchResultItem> {
        return repository.search(query, classeCode)
    }
}

