import kotlin.random.Random

fun generateTemperature(seed: Int): String {
    // write your code here
    val tempList = mutableListOf<Int>()
    val d = Random(seed)
    for (i in 1..7) {
        tempList.add(d.nextInt(20, 31))
    }
    return tempList.joinToString(" ")
}
/*
fun main () {
    println(generateTemperature(1))
}
 */