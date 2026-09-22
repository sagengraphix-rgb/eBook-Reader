package com.example.data.sample

import com.example.data.local.BookEntity
import com.example.data.model.Chapter
import com.example.data.parser.BookParser

object SampleBooks {

    fun getInitialBooks(): List<BookEntity> {
        val prideChapters = listOf(
            Chapter(
                title = "Chapter 1 — The Netherfield News",
                content = """It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.

However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of some one or other of their daughters.

"My dear Mr. Bennet," said his lady to him one day, "have you heard that Netherfield Park is let at last?"

Mr. Bennet replied that he had not.

"But it is," returned she; "for Mrs. Long has just been here, and she told me all about it."

Mr. Bennet made no answer.

"Do you not want to know who has taken it?" cried his wife impatiently.

"You want to tell me, and I have no objection to hearing it."

This was invitation enough.

"Why, my dear, you must know, Mrs. Long says that Netherfield is taken by a young man of large fortune from the north of England; that he came down on Monday in a chaise and four to see the place, and was so much delighted with it, that he agreed with Mr. Morris immediately; that he is to take possession before Michaelmas, and some of his servants are to be in the house by the end of next week."

"What is his name?"

"Bingley."

"Is he married or single?"

"Oh! Single, my dear, to be sure! A single man of large fortune; four or five thousand a year. What a fine thing for our girls!"

"How so? How can it affect them?"

"My dear Mr. Bennet," replied his wife, "how can you be so tiresome! You must know that I am thinking of his marrying one of them."

"Is that his design in settling here?"

"Design! Nonsense, how can you talk so! But it is very likely that he may fall in love with one of them, and therefore you must visit him as soon as he comes."

"I see no occasion for that. You and the girls may go, or you may send them by themselves, which perhaps will be still better, for as you are as handsome as any of them, Mr. Bingley might like you the best of the party."

"My dear, you flatter me. I certainly have had my share of beauty, but I do not pretend to be anything extraordinary now. When a woman has five grown-up daughters, she ought to give over thinking of her own beauty."

"In such cases, a woman has not often much beauty to think of."

"But, my dear, you must indeed go and see Mr. Bennet."

Mr. Bennet was so odd a mixture of quick parts, sarcastic humour, reserve, and caprice, that the experience of three-and-twenty years had been insufficient to make his wife understand his character. Her mind was less difficult to develop. She was a woman of mean understanding, little information, and uncertain temper. When she was discontented, she fancied herself nervous. The business of her life was to get her daughters married; its solace was visiting and news."""
            ),
            Chapter(
                title = "Chapter 2 — The Assembly at Meryton",
                content = """Not all that Mrs. Bennet, however, with the assistance of her five daughters, could ask on the subject, was sufficient to draw from her husband any satisfactory description of Mr. Bingley. They attacked him in various ways—with barefaced questions, ingenious suppositions, and distant surmises; but he eluded the skill of them all, and they were at last obliged to accept the second-hand intelligence of their neighbour, Lady Lucas.

Her report was highly favourable. Sir William had been delighted with him. He was quite young, wonderfully handsome, extremely agreeable, and, to crown the whole, he meant to be at the next assembly with a large party. Nothing could be more delightful! To be fond of dancing was a certain step towards falling in love; and very lively hopes of Mr. Bingley’s heart were entertained.

The evening altogether passed off pleasantly to the whole family. Mrs. Bennet had seen her eldest daughter much admired by the Netherfield party. Mr. Bingley had danced with her twice, and she had been distinguished by his sisters. Jane was as much gratified by this as her mother could be, though in a quieter way.

Elizabeth felt Jane’s pleasure very warmly.

Mr. Darcy danced only once with Mrs. Hurst and once with Miss Bingley, declined being introduced to any other lady, and spent the rest of the evening in walking about the room, speaking occasionally to one of his own party. His character was decided. He was the proudest, most disagreeable man in the world, and everybody hoped that he would never come there again.

Amongst the most violent against him was Mrs. Bennet, whose dislike of his general behaviour was sharpened into particular resentment by his having slighted one of her daughters.

Elizabeth Bennet had been obliged, by the scarcity of gentlemen, to sit down for two dances; and during part of that time, Mr. Darcy had been standing near enough for her to hear a conversation between him and Mr. Bingley, who came from the dance for a few minutes to press his friend to join it.

"Come, Darcy," said he, "I must have you dance. I hate to see you standing about by yourself in this stupid manner. You had much better dance."

"I certainly shall not. You know how I detest it, unless I am particularly acquainted with my partner. At such an assembly as this it would be insupportable. Your sisters are engaged, and there is not another woman in the room whom it would not be a punishment to me to stand up with."

"I would not be so fastidious as you are," cried Mr. Bingley, "for a kingdom! Upon my honour, I never met with so many pleasant girls in my life, as I have this evening; and there are several of them uncommonly pretty."

"You are dancing with the only handsome girl in the room," said Mr. Darcy, looking at the eldest Miss Bennet.

"Oh! She is the most beautiful creature I ever beheld! But there is one of her sisters sitting just behind you, who is very pretty, and I dare say very agreeable. Do let me ask my partner to introduce you."

"Which do you mean?" and turning round he looked for a moment at Elizabeth, till catching her eye, he withdrew his own and coldly said:

"She is tolerable, but not handsome enough to tempt me; I am in no humour at present to give consequence to young ladies who are slighted by other men. You had better return to your partner and enjoy her smiles, for you are wasting your time with me."

Mr. Bingley followed his advice. Mr. Darcy walked off; and Elizabeth remained with no very cordial feelings toward him. She told the story, however, with great spirit among her friends; for she had a lively, playful disposition, which delighted in anything ridiculous."""
            ),
            Chapter(
                title = "Chapter 3 — Netherfield Library Conversations",
                content = """Elizabeth passed the chief of the night in her sister’s room, and in the morning had the satisfaction of finding Jane a little better. She would not leave her, however, though she was very desirous of taking a walk.

At eight o’clock Elizabeth entered the breakfast-room at Netherfield, where she was received by Miss Bingley and Mrs. Hurst with that cold politeness which showed they were amazed at her sisterly zeal.

"Miss Eliza Bennet," said Miss Bingley, when the door was closed, "despises cards. She is a great reader, and has no pleasure in anything else."

"I deserve neither such praise nor such censure," cried Elizabeth; "I am not a great reader, and I have pleasure in many things."

"In nursing your sister I am sure you have pleasure," said Bingley; "and I hope it will soon be increased by seeing her quite well."

Elizabeth thanked him from her heart, and then walked towards a table where a few books were lying. He immediately offered to fetch her others—all that his library afforded.

"I wish my collection were larger for your benefit and my own credit; but I am an idle fellow, and though I have not many, I have more than I ever looked into."

Elizabeth assured him that she could suit herself perfectly with those in the room.

"I am astonished," said Miss Bingley, "that my father should have left so small a collection of books. What a delightful library you have at Pemberley, Mr. Darcy!"

"It ought to be good," he replied, "it has been the work of many generations."

"And then you have added so much to it yourself, you are always buying books."

"I cannot comprehend the neglect of a family library in such days as these."

"Neglect! I am sure you neglect nothing that can add to the beauties of that noble place. Charles, when you build your house, I wish it may be half as delightful as Pemberley."

"I wish it may," said Bingley.

"The world is filled with good books, but few readers worthy of their wisdom," murmured Elizabeth softly, turning a fresh leaf of the volume before her."""
            )
        )

        val holmesChapters = listOf(
            Chapter(
                title = "Part I — The Woman & The Bohemian King",
                content = """To Sherlock Holmes she is always the woman. I have seldom heard him mention her under any other name. In his eyes she eclipses and predominates the whole of her sex. It was not that he felt any emotion akin to love for Irene Adler. All emotions, and that one particularly, were abhorrent to his cold, precise but admirably balanced mind. He was, I take it, the most perfect reasoning and observing machine that the world has seen.

One night—it was on the twentieth of March, 1888—I was returning from a journey to a patient, for I had now returned to civil practice, when my way led me through Baker Street. As I passed the well-remembered door, which must always be associated in my mind with my wooing, and with the dark incidents of the Study in Scarlet, I was seized with a keen desire to see Holmes again, and to know how he was employing his extraordinary powers.

His rooms were brilliantly lit, and, even as I looked up, I saw his tall, spare figure pass twice in a dark silhouette against the blind. He was pacing the room swiftly, eagerly, with his head sunk upon his chest and his hands clasped behind him. To me, who knew his every mood and habit, his attitude and manner told their own story. He was at work again. He had risen out of his drug-created dreams and was hot upon the scent of some new problem.

I rang the bell and was shown up to the chamber which had formerly been in part my own.

His manner was not effusive. It seldom was; but he was glad, I think, to see me. With hardly a word spoken, but with a kindly eye, he waved me to an armchair, threw across his case of cigars, and indicated an acid-stained corner where stood a gasogene and a decanter.

Then he stood before the fire and looked me over in his singular introspective fashion.

"Wedlock suits you," he remarked. "I think, Watson, that you have put on seven and a half pounds since I saw you."

"Seven!" I answered.

"Indeed, I should have thought a little more. Just a trifle more, I fancy, Watson. And in practice again, I observe. You did not tell me that you intended to go into harness."

"Then, how do you know?"

"I see it, I deduce it. How do I know that you have been getting yourself very wet lately, and that you have a most clumsy and careless servant girl?"

"My dear Holmes," said I, "this is too much. You would certainly have been burned, had you lived a few centuries ago. It is true that I had a country walk on Thursday and came home in a dreadful mess, but as I have changed my clothes I can’t imagine how you deduce it. As to Mary Jane, she is incorrigible, and my wife has given her notice, but there, again, I fail to see how you work it out."

He chuckled to himself and rubbed his long, nervous hands together."""
            ),
            Chapter(
                title = "Part II — The Photograph & The Mask",
                content = """A slow and heavy step, which had been heard upon the stairs and in the passage, paused immediately outside the door. Then there was a loud and authoritative tap.

"Come in!" said Holmes.

A man entered who could hardly have been less than six feet six inches in height, with the chest and limbs of a Hercules. His dress was rich with a richness which would, in England, be regarded as akin to bad taste. Heavy bands of astrakhan were slashed across the sleeves and fronts of his double-breasted coat, while the deep blue cloak was lined with flame-coloured silk and secured at the neck with a brooch which consisted of a single flaming beryl.

He held a broad-brimmed hat in his hand, while he wore across the upper part of his face, extending down past the cheekbones, a black vizard mask, which he had apparently adjusted that very moment.

"You had my note?" he asked with a deep, harsh voice and a strongly marked German accent. "I told you that I would call." He looked from one to the other of us, as if uncertain to whom to address himself.

"Pray take a seat," said Holmes. "This is my friend and colleague, Dr. Watson, who is occasionally good enough to help me in my cases. Whom have I the honour to address?"

"You may address me as the Count Von Kramm, a Bohemian nobleman. I understand that this gentleman, your friend, is a man of honour and discretion, whom I may trust with a matter of the most extreme importance. If not, I should much prefer to communicate with you alone."

I rose to go, but Holmes caught me by the wrist and pushed me back into my chair. "It is both, or neither," said he. "You may say before this gentleman anything which you may say to me."

The Count shrugged his broad shoulders. "Then, as between us, the case is this: there exists a photograph. A cabinet photograph of myself and Mademoiselle Irene Adler. She has threatened to send it to the princess I am about to marry. It would ruin me, Holmes! It must be recovered."

"We must find where she keeps it," Holmes murmured, a gleam of razor-sharp interest kindling in his gray eyes."""
            )
        )

        val gatsbyChapters = listOf(
            Chapter(
                title = "Chapter 1 — The Promising Summer of 1922",
                content = """In my younger and more vulnerable years my father gave me some advice that I’ve been turning over in my mind ever since.

"Whenever you feel like criticizing any one," he told me, "just remember that all the people in this world haven’t had the advantages that you’ve had."

He didn’t say any more, but we’ve always been unusually communicative in a reserved way, and I understood that he meant a great deal more than that. In consequence, I’m inclined to reserve all judgements, a habit that has opened up many curious natures to me and also made me the victim of not a few veteran bores.

And, after boasting this way of my tolerance, I come to the admission that it has a limit. Conduct may be founded on the hard rock or the wet marshes, but after a certain point I don’t care what it’s founded on. When I came back from the East last autumn I felt that I wanted the world to be in uniform and at a sort of moral attention forever; I wanted no more riotous excursions with privileged glimpses into the human heart. Only Gatsby, the man who gives his name to this book, was exempt from my reaction—Gatsby, who represented everything for which I have an unaffected scorn. If personality is an unbroken series of successful gestures, then there was something gorgeous about him, some heightened sensitivity to the promises of life, as if he were related to one of those intricate machines that register earthquakes ten thousand miles away.

My house was at the very tip of the egg, only fifty yards from the Sound, and squeezed between two huge places that rented for twelve or fifteen thousand a season. The one on my right was a colossal affair by any standard—it was a factual imitation of some Hôtel de Ville in Normandy, with a tower on one side, spanking new under a thin beard of raw ivy, and a marble swimming pool, and more than forty acres of lawn and garden. It was Gatsby’s mansion.

Or, rather, as I didn’t yet know Mr. Gatsby, it was a mansion inhabited by a gentleman of that name.

The wind had blown off, leaving a loud, bright night, with wings beating in the trees and a persistent organ sound as the full bellows of the earth blew the frogs full of life. The silhouette of a moving cat wavered across the moonlight, and turning my head I saw that I was not alone—fifty feet away a figure had emerged from the shadow of my neighbour’s mansion and was standing with his hands in his pockets regarding the silver pepper of the stars."""
            ),
            Chapter(
                title = "Chapter 2 — The Valley of Ashes & The Green Light",
                content = """About half way between West Egg and New York the motor road hastily joins the railroad and runs beside it for a quarter of a mile, so as to shrink away from a certain desolate area of land. This is a valley of ashes—a fantastic farm where ashes grow like wheat into ridges and hills and grotesque gardens; where ashes take the forms of houses and chimneys and rising smoke and, finally, with a transcendent effort, of men who move dimly and already crumbling through the powdery air.

Above the gray land and the spasms of bleak dust which drift endlessly over it, you perceive, after a moment, the eyes of Doctor T. J. Eckleburg. The eyes of Doctor T. J. Eckleburg are blue and gigantic—their irises are one yard high. They look out of no face, but, instead, from a pair of enormous yellow spectacles which pass over a non-existent nose.

Evidently some wild wag of an oculist set them there to fatten his practice in the borough of Queens, and then sank down himself into eternal blindness, or forgot them and moved away. But his eyes, dimmed a little by many paintless days, under sun and rain, brood on over the solemn dumping ground.

He stretched out his arms toward the dark water in a curious way, and, far as I was from him, I could have sworn he was trembling. Involuntarily I glanced seaward—and distinguished nothing except a single green light, minute and far away, that might have been the end of a dock.

When I looked once more for Gatsby he had vanished, and I was alone again in the unquiet darkness."""
            )
        )

        return listOf(
            BookEntity(
                id = 1,
                title = "Pride and Prejudice",
                author = "Jane Austen",
                coverColorHex = "#854D0E", // Warm Amber
                format = "EPUB",
                totalChapters = prideChapters.size,
                currentChapterIndex = 0,
                currentPage = 1,
                totalPages = 12,
                progressPercent = 18f,
                lastReadTimestamp = System.currentTimeMillis() - 3600000 * 2, // 2 hours ago
                dateAdded = System.currentTimeMillis() - 86400000 * 3,
                tags = "Classics, Romance, Fiction",
                contentRaw = "",
                chaptersJson = BookParser.chaptersToJson(prideChapters),
                isFinished = false,
                estimatedMinutes = 45
            ),
            BookEntity(
                id = 2,
                title = "A Scandal in Bohemia",
                author = "Arthur Conan Doyle",
                coverColorHex = "#1E3A8A", // Oxford Navy
                format = "EPUB",
                totalChapters = holmesChapters.size,
                currentChapterIndex = 0,
                currentPage = 1,
                totalPages = 8,
                progressPercent = 42f,
                lastReadTimestamp = System.currentTimeMillis() - 86400000, // yesterday
                dateAdded = System.currentTimeMillis() - 86400000 * 5,
                tags = "Mystery, Detective, Classics",
                contentRaw = "",
                chaptersJson = BookParser.chaptersToJson(holmesChapters),
                isFinished = false,
                estimatedMinutes = 28
            ),
            BookEntity(
                id = 3,
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                coverColorHex = "#065F46", // Forest Emerald
                format = "EPUB",
                totalChapters = gatsbyChapters.size,
                currentChapterIndex = 0,
                currentPage = 1,
                totalPages = 10,
                progressPercent = 0f,
                lastReadTimestamp = System.currentTimeMillis() - 86400000 * 2,
                dateAdded = System.currentTimeMillis() - 86400000 * 7,
                tags = "Literary Fiction, American Classics",
                contentRaw = "",
                chaptersJson = BookParser.chaptersToJson(gatsbyChapters),
                isFinished = false,
                estimatedMinutes = 35
            )
        )
    }
}
