package com.example.data.sample

import com.example.data.model.DictionaryEntry

object DictionaryRepository {

    private val words = mapOf(
        "fortune" to DictionaryEntry(
            word = "fortune",
            phonetic = "/ˈfɔːr.tʃuːn/",
            partOfSpeech = "noun",
            definition = "A large amount of money or valuable assets; also, chance or luck affecting human affairs.",
            example = "A single man in possession of a good fortune must be in want of a wife.",
            synonyms = listOf("wealth", "prosperity", "opulence", "chance")
        ),
        "fastidious" to DictionaryEntry(
            word = "fastidious",
            phonetic = "/fæsˈtɪd.i.əs/",
            partOfSpeech = "adjective",
            definition = "Very attentive to and concerned about accuracy and detail; having very demanding standards.",
            example = "I would not be so fastidious as you are for a kingdom!",
            synonyms = listOf("meticulous", "scrupulous", "punctilious", "fussy")
        ),
        "insupportable" to DictionaryEntry(
            word = "insupportable",
            phonetic = "/ˌɪn.səˈpɔːr.tə.bəl/",
            partOfSpeech = "adjective",
            definition = "Unable to be endured; intolerable.",
            example = "At such an assembly as this it would be insupportable.",
            synonyms = listOf("intolerable", "unbearable", "oppressive")
        ),
        "deduce" to DictionaryEntry(
            word = "deduce",
            phonetic = "/dɪˈdjuːs/",
            partOfSpeech = "verb",
            definition = "To arrive at a fact or a conclusion by reasoning; draw as a logical inference.",
            example = "I see it, I deduce it from the observations before me.",
            synonyms = listOf("infer", "conclude", "gather", "derive")
        ),
        "singular" to DictionaryEntry(
            word = "singular",
            phonetic = "/ˈsɪŋ.ɡjə.lər/",
            partOfSpeech = "adjective",
            definition = "Exceptionally good or remarkable; unusual or eccentric.",
            example = "He looked me over in his singular introspective fashion.",
            synonyms = listOf("exceptional", "unique", "peculiar", "extraordinary")
        ),
        "tolerable" to DictionaryEntry(
            word = "tolerable",
            phonetic = "/ˈtɑː.lɚ.ə.bəl/",
            partOfSpeech = "adjective",
            definition = "Able to be tolerated; fair, moderate, or passable though not exceptional.",
            example = "She is tolerable, but not handsome enough to tempt me.",
            synonyms = listOf("passable", "acceptable", "adequate", "mediocre")
        ),
        "eloquent" to DictionaryEntry(
            word = "eloquent",
            phonetic = "/ˈel.ə.kwənt/",
            partOfSpeech = "adjective",
            definition = "Fluent or persuasive in speaking or writing; clearly expressing or indicating something.",
            example = "His speech was marked by eloquent appeals to their compassion.",
            synonyms = listOf("articulate", "expressive", "persuasive", "fluent")
        ),
        "luminous" to DictionaryEntry(
            word = "luminous",
            phonetic = "/ˈluː.mə.nəs/",
            partOfSpeech = "adjective",
            definition = "Emitting or reflecting light; glowing; also very clear or intellectually brilliant.",
            example = "The green light at the end of Daisy's dock shone luminous through the mist.",
            synonyms = listOf("radiant", "gleaming", "lucid", "brilliant")
        ),
        "serenity" to DictionaryEntry(
            word = "serenity",
            phonetic = "/səˈren.ə.t̬i/",
            partOfSpeech = "noun",
            definition = "The state of being calm, peaceful, and untroubled.",
            example = "The quiet library was filled with an aura of contemplation and serenity.",
            synonyms = listOf("tranquility", "peace", "calmness", "repose")
        ),
        "epiphany" to DictionaryEntry(
            word = "epiphany",
            phonetic = "/ɪˈpɪf.ən.i/",
            partOfSpeech = "noun",
            definition = "A moment of sudden and profound revelation or insight.",
            example = "Standing before the painting, she experienced a sudden artistic epiphany.",
            synonyms = listOf("revelation", "realization", "insight", "awakening")
        ),
        "resilient" to DictionaryEntry(
            word = "resilient",
            phonetic = "/rɪˈzɪl.jənt/",
            partOfSpeech = "adjective",
            definition = "Able to recoil or spring back into shape after bending, stretching, or being compressed; recovering quickly from difficulties.",
            example = "Her resilient spirit helped her overcome all societal constraints.",
            synonyms = listOf("strong", "buoyant", "adaptable", "durable")
        ),
        "melancholy" to DictionaryEntry(
            word = "melancholy",
            phonetic = "/ˈmel.ən.kɑː.li/",
            partOfSpeech = "noun / adjective",
            definition = "A feeling of pensive sadness, typically with no obvious cause.",
            example = "The autumn leaves instilled a gentle melancholy across the gardens.",
            synonyms = listOf("sorrow", "woe", "pensive", "somber")
        )
    )

    fun lookup(query: String): DictionaryEntry {
        val clean = query.trim().lowercase().replace(Regex("[^a-z]"), "")
        val direct = words[clean]
        if (direct != null) return direct

        // Check plural / ed / ing forms
        for ((key, entry) in words) {
            if (clean.startsWith(key) || key.startsWith(clean)) {
                return entry
            }
        }

        // Contextual fallback entry
        val capitalized = clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return DictionaryEntry(
            word = capitalized,
            phonetic = "/ˈ${clean}/",
            partOfSpeech = "noun / verb",
            definition = "A literary term denoting an idea, quality, or action referenced in the literary text. Expresses nuanced tone and contextual atmosphere.",
            example = "The author employed '$clean' to evoke vivid characterization.",
            synonyms = listOf("concept", "expression", "attribute")
        )
    }
}
