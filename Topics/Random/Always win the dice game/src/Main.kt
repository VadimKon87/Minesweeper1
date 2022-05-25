import kotlin.random.Random

fun createDiceGameRandomizer(n: Int): Int {
    // write your code here
    var friend = 0
    var house = 0

    var seed = 0
    while (friend >= house) {
        seed++
        //friend = 0
        //house = 0

        val rando = Random(seed)
        repeat(n) {
            friend += rando.nextInt(1, 7)
        }
        repeat(n) {
            house += rando.nextInt(1, 7)
        }
    }
    return seed
}
