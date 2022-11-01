package nicestring

fun String.isNice(): Boolean {

    val notContainSubstrings = setOf("bu", "ba", "be").all { !this.contains(it) }
    val containThreeVowels = count { it in "aeiou" } >= 3
    val containDoubleLetter = zipWithNext().any { it.first == it.second }

    return listOf(notContainSubstrings, containThreeVowels, containDoubleLetter).count { it } >= 2
}