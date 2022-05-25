import kotlin.random.Random

fun generatePredictablePassword(seed: Int): String {
    var randomPassword = ""
    // write your code here
    val list = mutableListOf<String>()
    val s = Random(seed)
    for (i in 1..10) {
        list.add(s.nextInt(33, 127).toChar().toString())
    }
    randomPassword = list.joinToString("")
    return randomPassword
}
