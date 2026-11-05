package com.yonuxamxam.app.domain.repository

import com.yonuxamxam.app.data.model.BacBlanc
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.model.ClasseStat
import com.yonuxamxam.app.data.model.CoursEnCours
import com.yonuxamxam.app.data.model.Eleve
import com.yonuxamxam.app.data.model.Matiere
import com.yonuxamxam.app.data.model.Professeur
import com.yonuxamxam.app.data.model.SearchResultItem
import com.yonuxamxam.app.data.model.SuggestionCours

/**
 * Interface du repository (Couche Domain).
 * Définit le contrat d'accès aux données du dashboard et des chapitres
 * indépendamment de toute implémentation technique sous-jacente.
 */
interface HomeRepository {
    suspend fun getEleve(): Eleve
    suspend fun getClasses(): List<ClasseStat>
    suspend fun getMatieres(classeCode: String): List<Matiere>
    suspend fun getChapitres(classeCode: String, matiereNom: String): List<Chapitre>
    suspend fun getCoursEnCours(): List<CoursEnCours>
    suspend fun getProfesseurs(): List<Professeur>
    suspend fun getSuggestionsCours(): List<SuggestionCours>
    suspend fun getBacBlancs(classeCode: String): List<BacBlanc>
    suspend fun search(query: String, classeCode: String): List<SearchResultItem>
}

