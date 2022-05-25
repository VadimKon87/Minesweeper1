package minesweeper

import kotlin.random.Random

class Minesweeper(private val height: Int, private val width: Int) {

    private val minesweeperList = MutableList(height) { MutableList(width) { "." } }        // invisible field

    private val minesweeperListCopy = MutableList(height) { MutableList(width) { "." } }    // visible field

    private var numOfMines = 0                          // will be changed to user input

    private var x = 0                                   // will be changed to user input

    private var y = 0                                   // will be changed to user input

    var wrongMark = 0

    private val hintList = listOf("1", "2", "3", "4", "5", "6", "7", "8")

    private fun makeField() {

        print("How many mines do you want on the field? ")

        numOfMines = readln().toInt()                   // input number of mines

    }

    private fun placeMines() {

        var mineCount = 0

        while (mineCount != numOfMines) {                   // avoids placing mines on top of each other
            val randomN1 = Random.nextInt(0, height)
            val randomN2 = Random.nextInt(0, width)
            if (minesweeperList[randomN1][randomN2] != "X" && randomN1 != y || randomN2 != x) { //avoids placing on y and x which are first move
                minesweeperList[randomN1][randomN2] = "X"       // this list is not visible
                mineCount++
            }
        }
    }

    private fun placeHints() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (minesweeperList[y][x] != "X") {
                    var nMines = 0                                                // number of mines next to cell
                    if (y > 0 && x > 0) {
                        when {
                            minesweeperList[y - 1][x - 1] == "X" -> nMines++      // cell up left
                        }
                    }
                    if (y > 0) {
                        when {
                            minesweeperList[y - 1][x] == "X" -> nMines++          // cell up
                        }
                    }
                    if (y > 0 && x < width - 1) {
                        when {
                            minesweeperList[y - 1][x + 1] == "X" -> nMines++      // cell up right
                        }
                    }
                    if (x > 0) {
                        when {
                            minesweeperList[y][x - 1] == "X" -> nMines++          // cell left
                        }
                    }
                    if (x < width - 1) {
                        when {
                            minesweeperList[y][x + 1] == "X" -> nMines++          // cell right
                        }
                    }
                    if (y < height - 1 && x > 0) {
                        when {
                            minesweeperList[y + 1][x - 1] == "X" -> nMines++      // cell bottom left
                        }
                    }
                    if (y < height - 1) {
                        when {
                            minesweeperList[y + 1][x] == "X" -> nMines++          // cell bottom
                        }
                    }
                    if (y < height - 1 && x < width - 1)
                        when {
                            minesweeperList[y + 1][x + 1] == "X" -> nMines++      // cell bottom right
                        }
                    if (nMines != 0) {
                        minesweeperList[y][x] = nMines.toString()                 // place number, if it's not 0
                    }
                }
            }
        }
    }

    // show mines when game is lost
    private fun showMines() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (minesweeperList[y][x] == "X") {
                    minesweeperListCopy[y][x] = "X"                 // replace mines and hints with unknown cell "."
                }
            }
        }
    }

    // prints field
    private fun printFieldCopy() {
        val listX = (1..width).toMutableList().joinToString("")     //numbers of x-axis
        println()
        println(" |$listX|")                                                 // prints x axis
        println("—|${"—".repeat(width)}|")                                  // prints a line under numbers

        var n = 1
        for (i in minesweeperListCopy) {                           // prints field with Y axis numbers and line on left
            println("$n|${i.joinToString("")}|")
            n++
        }
        println("—|${"—".repeat(width)}|")                          // line under
    }

    // shows field with hints and mines. REMOVE when everything works
    private fun printField() {
        val listX = (1..width).toMutableList().joinToString("")     //numbers of x-axis
        println()
        println(" |$listX|")                                                 // prints x axis
        println("—|${"—".repeat(width)}|")                                  // prints a line under numbers

        var n = 1
        for (i in minesweeperList) {                           // prints field with Y axis numbers and line on left
            println("$n|${i.joinToString("")}|")
            n++
        }
        println("—|${"—".repeat(width)}|")                          // line under
    }

    private fun floodFillUtil(x: Int, y: Int) {

        // Base cases
        if (x < 0 || x >= width || y < 0 || y >= height) return
        if (minesweeperList[x][y] == "X" || minesweeperList[x][y] == "/") return
        if (minesweeperListCopy[x][y] == "*" && minesweeperList[x][y] == "X") return

        // Adds the hint number to the border cells
        if (hintList.contains(minesweeperList[x][y])) {
            minesweeperListCopy[x][y] = minesweeperList[x][y]
            return
        }

        if (minesweeperListCopy[x][y] == "*" && minesweeperList[x][y] == ".") {
            minesweeperListCopy[x][y] = "/"
            wrongMark--
            //return
        }

        if (minesweeperListCopy[x][y] == "*" && hintList.contains(minesweeperList[x][y])) {
            minesweeperListCopy[x][y] = minesweeperList[x][y]
            wrongMark--
            return
        }

        // replaces "." with "/"
        minesweeperListCopy[x][y] = "/"
        minesweeperList[x][y] = "/"

        // Recur for north, east, south and west + diagonal
        floodFillUtil(x + 1, y)
        floodFillUtil(x + 1, y + 1)
        floodFillUtil(x - 1, y + 1)
        floodFillUtil(x - 1, y - 1)
        floodFillUtil( x, y + 1)
        floodFillUtil(x, y - 1)
        floodFillUtil(x + 1, y - 1)
        floodFillUtil(x - 1, y)
    }

    private fun floodFill(x: Int, y: Int) {
        val prevC = minesweeperListCopy[x][y]
        if (prevC == "/") return
        floodFillUtil(x, y)
    }

    fun playGame() {
        makeField()
        printFieldCopy()

        print("Set/unset mines marks or claim a cell as free: ")
        val (_x, _y, freeOrMine) = readln().split(' ')
        x = _x.toInt() - 1
        y = _y.toInt() - 1

        var correctMark = 0

        placeMines()
        placeHints()
        printField() // show hints and mines. REMOVE when everything works

        if (minesweeperList[y][x] == "." && freeOrMine == "free") {
            floodFill(y, x)
            printFieldCopy()
        }
        if (hintList.contains(minesweeperList[y][x]) && freeOrMine == "free") {
            minesweeperListCopy[y][x] = minesweeperList[y][x]
            printFieldCopy()
        }
        if (freeOrMine == "mine") {
            minesweeperListCopy[y][x] = "*"
            wrongMark++
            printFieldCopy()
        }

        // loop until all mines are marked and no wrong marks
        while (correctMark != numOfMines || wrongMark != 0) {

            print("Set/unset mines marks or claim a cell as free: ")
            val (_x1, _y1, freeOrMine1) = readln().split(' ')
            x = _x1.toInt() - 1
            y = _y1.toInt() - 1

            // mark cell as free
            if (minesweeperList[y][x] == "." && freeOrMine1 == "free") {
                floodFill(y, x)
                minesweeperListCopy[y][x] = "/"
                printFieldCopy()

                // open hint cell
            } else if (hintList.contains(minesweeperList[y][x]) && freeOrMine1 == "free") {
                minesweeperListCopy[y][x] = minesweeperList[y][x]
                printFieldCopy()

                // removes correctly placed mine
            } else if (minesweeperList[y][x] == "X"
                    && minesweeperListCopy[y][x] == "*" && freeOrMine1 == "mine") {
                minesweeperListCopy[y][x] = "."
                correctMark--
                printFieldCopy()

                // checks list with mines visible for mine
            } else if (minesweeperList[y][x] == "X" && freeOrMine1 == "mine") {
                minesweeperListCopy[y][x] = "*"             // place mark in correct cell
                correctMark++
                printFieldCopy()

                // removes mark
            } else if (minesweeperListCopy[y][x] == "*" && freeOrMine1 == "mine") {
                minesweeperListCopy[y][x] = "."
                wrongMark--
                printFieldCopy()

                // replace wrong mine mark with free cell
            } else if (minesweeperListCopy[y][x] == "*" && freeOrMine1 == "free" && minesweeperList[y][x] == ".") {
                minesweeperListCopy[y][x] = "/"
                wrongMark--
                printFieldCopy()

                // replace wrong mine mark with number
            } else if (hintList.contains(minesweeperList[y][x]) && freeOrMine1 == "free") {
                    minesweeperListCopy[y][x] = minesweeperList[y][x]
                wrongMark--
                printFieldCopy()

                // mark placed on number
            } else if (hintList.contains(minesweeperListCopy[y][x])) {
                println("There is a number here!")

                // mark wrong cell
            } else if (freeOrMine1 == "mine") {
                minesweeperListCopy[y][x] = "*"
                wrongMark++
                printFieldCopy()

                // step on mine
            } else if (minesweeperList[y][x] == "X" && freeOrMine1 == "free") {
                showMines()
                printFieldCopy()
                println("You stepped on a mine and failed!")
                break
            }

            var i = 0

            if (wrongMark == 0) {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        if (minesweeperListCopy[y][x] == ".") {
                            i++
                        }
                    }
                }
            }
            if (wrongMark == 0 && i == numOfMines) {
                println("Congratulations! You found all the mines!")
                break
            }
        }

        if (correctMark == numOfMines && wrongMark == 0) {
            println("Congratulations! You found all the mines!")
        }
    }
}

fun main() {
    val minesweeper = Minesweeper(9, 9)
    minesweeper.playGame()
}