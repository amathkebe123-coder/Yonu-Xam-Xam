package com.yonuxamxam.app.data.repository

import com.yonuxamxam.app.data.model.BacBlanc
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.model.ClasseStat
import com.yonuxamxam.app.data.model.CoursEnCours
import com.yonuxamxam.app.data.model.Eleve
import com.yonuxamxam.app.data.model.Matiere
import com.yonuxamxam.app.data.model.Professeur
import com.yonuxamxam.app.data.model.SearchResultItem
import com.yonuxamxam.app.data.model.SearchResultType
import com.yonuxamxam.app.data.model.SuggestionCours
import com.yonuxamxam.app.domain.repository.HomeRepository
import kotlinx.coroutines.delay

/**
 * Implémentation en mémoire de [HomeRepository] fournissant des données
 * réalistes pour le cursus scolaire sénégalais (collège et lycée jusqu'au BAC).
 */
class FakeHomeRepository : HomeRepository {

    // Données des matières par classe
    private val rawMatieres = listOf(
        Matiere("m_maths", "Maths", "M"),
        Matiere("m_pc", "PC", "P"),
        Matiere("m_svt", "SVT", "S"),
        Matiere("m_anglais", "Anglais", "A"),
        Matiere("m_francais", "Français", "F"),
        Matiere("m_espagnol", "Espagnol", "E"),
        Matiere("m_arabe", "Arabe", "A"),
        Matiere("m_philo", "Philo", "P")
    )

    // Dictionnaire des chapitres par matière pour TS (et génériques pour les autres matières)
    private val chapitresMap = mutableMapOf<String, List<Chapitre>>()

    init {
        initChapitres()
    }

    private fun initChapitres() {
        // Spécifique TS Maths
        chapitresMap["TS_Maths"] = listOf(
            Chapitre("ch_ts_m_1", 1, "Chapitre 1 - Bases", "Maths"),
            Chapitre("ch_ts_m_2", 2, "Chapitre 2 - Approfondissement", "Maths"),
            Chapitre("ch_ts_m_3", 3, "Chapitre 3 - Exercices types BAC", "Maths"),
            Chapitre("ch_ts_m_4", 4, "Chapitre 4 - Annales", "Maths")
        )

        // Spécifique TS PC
        chapitresMap["TS_PC"] = listOf(
            Chapitre("ch_ts_pc_1", 1, "Cinématique et dynamique", "PC"),
            Chapitre("ch_ts_pc_2", 2, "Circuits oscillants", "PC"),
            Chapitre("ch_ts_pc_3", 3, "Acides et bases en solution", "PC"),
            Chapitre("ch_ts_pc_4", 4, "Cinétique chimique", "PC"),
            Chapitre("ch_ts_pc_5", 5, "Annales PC BAC Sénégal", "PC")
        )

        // Spécifique TS SVT
        chapitresMap["TS_SVT"] = listOf(
            Chapitre("ch_ts_svt_1", 1, "Brassage génétique", "SVT"),
            Chapitre("ch_ts_svt_2", 2, "Immunologie fondamentale", "SVT"),
            Chapitre("ch_ts_svt_3", 3, "Activité nerveuse", "SVT"),
            Chapitre("ch_ts_svt_4", 4, "Exercices types BAC SVT", "SVT")
        )

        // Spécifique TL Philo
        chapitresMap["TL_Philo"] = listOf(
            Chapitre("ch_tl_ph_1", 1, "La Conscience et le Sujet", "Philo"),
            Chapitre("ch_tl_ph_2", 2, "La Liberté et la Morale", "Philo"),
            Chapitre("ch_tl_ph_3", 3, "La Société et l'État", "Philo"),
            Chapitre("ch_tl_ph_4", 4, "Méthodologie Dissertation", "Philo"),
            Chapitre("ch_tl_ph_5", 5, "Commentaire de texte philosophique", "Philo"),
            Chapitre("ch_tl_ph_6", 6, "Annales BAC Sénégal Philo", "Philo")
        )

        // Génération par défaut pour les autres combinaisons
        val matieresNoms = listOf("Maths", "PC", "SVT", "Anglais", "Français", "Espagnol", "Arabe", "Philo")
        val classes = listOf("6e", "5e", "4e", "3e", "2nde S", "2nde L", "1ère S", "1ère L", "TS", "TL")

        for (classe in classes) {
            for (matiere in matieresNoms) {
                val key = "${classe}_${matiere}"
                if (!chapitresMap.containsKey(key)) {
                    val count = when (matiere) {
                        "Maths" -> if (classe.contains("S")) 6 else 4
                        "Philo" -> if (classe.contains("L")) 6 else 3
                        "PC" -> if (classe.contains("S")) 5 else 2
                        "Français" -> if (classe.contains("L")) 5 else 3
                        else -> 4
                    }
                    chapitresMap[key] = (1..count).map { num ->
                        Chapitre(
                            id = "ch_${classe.lowercase().replace(" ", "_")}_${matiere.lowercase()}_$num",
                            numero = num,
                            titre = "Chapitre $num - Notions et applications ($matiere)",
                            matiereNom = matiere
                        )
                    }
                }
            }
        }
    }

    override suspend fun getEleve(): Eleve {
        return Eleve(
            id = "eleve_fatou",
            nom = "Ndiaye",
            prenom = "Fatou",
            classe = "TS",
            aDesNotificationsNonLues = true
        )
    }

    override suspend fun getClasses(): List<ClasseStat> {
        val classes = listOf("6e", "5e", "4e", "3e", "2nde S", "2nde L", "1ère S", "1ère L", "TS", "TL")
        return classes.map { classeCode ->
            val matieres = rawMatieres
            val totalChapitres = matieres.sumOf { mat ->
                chapitresMap["${classeCode}_${mat.nom}"]?.size ?: 4
            }
            ClasseStat(
                code = classeCode,
                nbMatieres = matieres.size,
                nbChapitres = totalChapitres
            )
        }
    }

    override suspend fun getMatieres(classeCode: String): List<Matiere> {
        return rawMatieres
    }

    override suspend fun getChapitres(classeCode: String, matiereNom: String): List<Chapitre> {
        val key = "${classeCode}_${matiereNom}"
        return chapitresMap[key] ?: chapitresMap["TS_$matiereNom"] ?: listOf(
            Chapitre("ch_def_1", 1, "Chapitre 1 - Bases", matiereNom),
            Chapitre("ch_def_2", 2, "Chapitre 2 - Approfondissement", matiereNom),
            Chapitre("ch_def_3", 3, "Chapitre 3 - Exercices types BAC", matiereNom),
            Chapitre("ch_def_4", 4, "Chapitre 4 - Annales", matiereNom)
        )
    }

    override suspend fun getCoursEnCours(): List<CoursEnCours> {
        return listOf(
            CoursEnCours(
                id = "prog_1",
                matiere = "Maths",
                titre = "Suites numériques & Récurrence",
                progression = 65
            ),
            CoursEnCours(
                id = "prog_2",
                matiere = "PC",
                titre = "Mécanique : Lois de Newton",
                progression = 40
            ),
            CoursEnCours(
                id = "prog_3",
                matiere = "SVT",
                titre = "Génétique et Hérédité humaine",
                progression = 80
            )
        )
    }

    override suspend fun getProfesseurs(): List<Professeur> {
        return listOf(
            Professeur("prof_1", "M. Diop", "Maths", "MD"),
            Professeur("prof_2", "Mme Sow", "PC", "MS"),
            Professeur("prof_3", "M. Fall", "Philo", "MF"),
            Professeur("prof_4", "Mme Ba", "SVT", "MB"),
            Professeur("prof_5", "M. Ndao", "Anglais", "MN"),
            Professeur("prof_6", "Mme Sarr", "Français", "MS")
        )
    }

    override suspend fun getSuggestionsCours(): List<SuggestionCours> {
        return listOf(
            SuggestionCours(
                id = "sug_1",
                titre = "Ondes mécaniques progressives",
                professeur = "Mme Sow",
                note = 4.9,
                vues = 1420
            ),
            SuggestionCours(
                id = "sug_2",
                titre = "Limites, Continuité & Fonctions exponentielles",
                professeur = "M. Diop",
                note = 4.8,
                vues = 2100
            ),
            SuggestionCours(
                id = "sug_3",
                titre = "Dissertation : L'art et la technique",
                professeur = "M. Fall",
                note = 4.7,
                vues = 980
            ),
            SuggestionCours(
                id = "sug_4",
                titre = "Immunologie : Réponses adaptatives",
                professeur = "Mme Ba",
                note = 4.9,
                vues = 1650
            )
        )
    }

    override suspend fun getBacBlancs(classeCode: String): List<BacBlanc> {
        return listOf(
            BacBlanc("bb_1", "Maths", classeCode, "4h00"),
            BacBlanc("bb_2", "PC", classeCode, "3h30"),
            BacBlanc("bb_3", "SVT", classeCode, "3h00"),
            BacBlanc("bb_4", "Philosophie", if (classeCode == "TL") "TL" else "TS", "4h00"),
            BacBlanc("bb_5", "Français", classeCode, "3h00")
        )
    }

    override suspend fun search(query: String, classeCode: String): List<SearchResultItem> {
        if (query.isBlank()) return emptyList()
        val normalized = query.trim().lowercase()

        val results = mutableListOf<SearchResultItem>()

        // Recherche dans les cours
        val allCourses = listOf(
            "Suites arithmétiques et géométriques" to "Maths • TS",
            "Fonctions exponentielles et logarithmes" to "Maths • TS",
            "Calcul intégral et primitives" to "Maths • TS",
            "Lois de Newton et applications" to "PC • TS",
            "Oscillations électriques libres" to "PC • TS",
            "Réactions acido-basiques" to "PC • TS",
            "Génétique formelle et brassage" to "SVT • TS",
            "Dissertation philosophique" to "Philo • TS / TL",
            "Grammaire et figures de style" to "Français • 1ère / Terminale"
        )

        allCourses.filter { it.first.lowercase().contains(normalized) || it.second.lowercase().contains(normalized) }
            .forEachIndexed { index, pair ->
                results.add(
                    SearchResultItem(
                        id = "search_c_$index",
                        titre = pair.first,
                        sousTitre = pair.second,
                        type = SearchResultType.COURS
                    )
                )
            }

        // Recherche dans les leçons
        val allLessons = listOf(
            "Leçon : Démonstration par récurrence" to "Maths • Chapitre 1",
            "Leçon : Forces d'interaction gravitationnelle" to "PC • Chapitre 1",
            "Leçon : Électrolyse et dosages" to "PC • Chapitre 3",
            "Leçon : La structure du neurone" to "SVT • Chapitre 3",
            "Leçon : Thèse, antithèse, synthèse en philosophie" to "Philo • Chapitre 4"
        )

        allLessons.filter { it.first.lowercase().contains(normalized) || it.second.lowercase().contains(normalized) }
            .forEachIndexed { index, pair ->
                results.add(
                    SearchResultItem(
                        id = "search_l_$index",
                        titre = pair.first,
                        sousTitre = pair.second,
                        type = SearchResultType.LECON
                    )
                )
            }

        // Recherche dans les exercices
        val allExercises = listOf(
            "Exercice type BAC : Étude d'une suite récurrente" to "Maths • 45 min",
            "Exercice BAC Sénégal 2023 : Mouvement d'un projectile" to "PC • 1h15",
            "Exercice corrigé : Titrage pH-métrique" to "PC • 30 min",
            "QCM d'entraînement : Brassage interchromosomique" to "SVT • 20 questions",
            "Sujet de dissertation BAC guidé" to "Philo • 3h00"
        )

        allExercises.filter { it.first.lowercase().contains(normalized) || it.second.lowercase().contains(normalized) }
            .forEachIndexed { index, pair ->
                results.add(
                    SearchResultItem(
                        id = "search_e_$index",
                        titre = pair.first,
                        sousTitre = pair.second,
                        type = SearchResultType.EXERCICE
                    )
                )
            }

        return results
    }
}

