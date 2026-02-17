package org.devkot.escape.utils

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.devkot.escape.enums.Cell
import org.devkot.escape.models.Floor

object FloorGenerator {

    private val mapCache = mutableMapOf<Int, Array<Array<Cell>>>()

    fun getMap(mapNumber: Int): Array<Array<Cell>> {
        return mapCache.getOrPut(mapNumber) {
            generateMapFromLayout(mapNumber)
        }
    }

    private fun generateMapFromLayout(mapNumber: Int): Array<Array<Cell>> {
        val layouts = arrayOf(
            listOf(
                "######################",
                "#                    #",
                "#                    #",
                "#  #######  #######  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #              #  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #     ##########  #",
                "#  #     #           #",
                "#  #     #           #",
                "#  ############ ###  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#        #  #     #  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #######  #######  #",
                "#                    #",
                "#                    #",
                "######################"
            ),
            listOf(
                "######################",
                "#                    #",
                "#                    #",
                "#  ################  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #              #  #",
                "#  #     ####     #  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #     #           #",
                "#  #     #           #",
                "#  #     #           #",
                "#  #     #  #######  #",
                "#  #     #  #     #  #",
                "#        #  #     #  #",
                "#  #     #  #     #  #",
                "#  #     #        #  #",
                "#  ################  #",
                "#                    #",
                "#                    #",
                "######################"
            ),
            listOf(
                "######################",
                "#                    #",
                "#                    #",
                "#  #######  #######  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #              #  #",
                "#  #     #  #     #  #",
                "#  #     #  #     #  #",
                "#  #     ##########  #",
                "#  #              #  #",
                "#  #              #  #",
                "#  ########### ####  #",
                "#  #              #  #",
                "#  #              #  #",
                "#  #              #  #",
                "#  #              #  #",
                "#  #              #  #",
                "#  #######  #######  #",
                "#                    #",
                "#                    #",
                "######################"
            ),
            listOf(
                "######################",
                "#                    #",
                "#                    #",
                "#  #####  #   #####  #",
                "#  #   #  #   #   #  #",
                "#  #   #  #   #   #  #",
                "#  #   #  #   #   #  #",
                "#  #   ####   #   #  #",
                "#  #              #  #",
                "#  ######## #######  #",
                "#  #              #  #",
                "#  #   ########   #  #",
                "#  #   #      #   #  #",
                "#  #   #      #   #  #",
                "#  #   #      #   #  #",
                "#  #   ###  ###   #  #",
                "#  #              #  #",
                "#  ################  #",
                "#         #          #",
                "#         #          #",
                "#                    #",
                "######################"
            )
        )

        val rawMap = layouts[mapNumber]

        fun charToCell(c: Char): Cell {
            return when (c) {
                '#' -> Cell.Border
                ' ' -> Cell.Empty
                'd' -> Cell.ElevatorDown
                'u' -> Cell.ElevatorUp
                '$' -> Cell.Block
                'k' -> Cell.Key
                else -> Cell.Empty
            }
        }

        return rawMap.map { string ->
            string.map { char -> charToCell(char) }.toTypedArray()
        }.toTypedArray()
    }

    private val names = arrayOf(
        "Командный узел",
        "Крио-отсек",
        "Ядро энергии",
        "Ксено-лаборатория"
    )

    suspend fun generateAllFloorsAsync(): List<Floor> = withContext(Dispatchers.Default) {
        coroutineScope {
            val mapIndices = (0..3).shuffled()

            val shuffledNames = Array(4) { i -> names[mapIndices[i]] }
            val colors = listOf(
                Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107),
                Color(0xFF9C27B0), Color(0xFFE91E63), Color(0xFF00BCD4)
            ).shuffled()

            val floor1 = async {
                createFloor(1, 0, mapIndices, shuffledNames, colors) { map ->
                    map[1][1] = Cell.ElevatorUp; map[20][20] = Cell.ElevatorUp
                    map[21][10] = Cell.Door; map[21][11] = Cell.Door; map[21][12] = Cell.Door
                }
            }

            val floor2 = async {
                createFloor(2, 1, mapIndices, shuffledNames, colors) { map ->
                    map[1][1] = Cell.ElevatorDown; map[20][20] = Cell.ElevatorDown
                    map[20][1] = Cell.ElevatorUp; map[1][20] = Cell.ElevatorUp
                }
            }

            val floor3 = async {
                createFloor(3, 2, mapIndices, shuffledNames, colors) { map ->
                    map[1][1] = Cell.ElevatorUp; map[20][20] = Cell.ElevatorUp
                    map[20][1] = Cell.ElevatorDown; map[1][20] = Cell.ElevatorDown
                }
            }

            val floor4 = async {
                createFloor(4, 3, mapIndices, shuffledNames, colors) { map ->
                    map[1][1] = Cell.ElevatorDown; map[20][20] = Cell.ElevatorDown
                }
            }

            listOf(
                floor1.await(),
                floor2.await(),
                floor3.await(),
                floor4.await()
            )
        }
    }

    fun generateAllFloors(): List<Floor> {
        val mapIndices = (0..3).shuffled()
        val shuffledNames = Array(4) { i -> names[mapIndices[i]] }
        val colors = listOf(
            Color(0xFF2DDC34),
            Color(0xFFFFC107),
            Color(0xFFCB0CE3),
            Color(0xFFE91E63),
            Color(0xFF00BCD4)
        ).shuffled()

        return listOf(
            createFloor(1, 0, mapIndices, shuffledNames, colors) { map ->
                map[1][1] = Cell.ElevatorUp; map[20][20] = Cell.ElevatorUp
                map[21][10] = Cell.Door; map[21][11] = Cell.Door; map[21][12] = Cell.Door
            },
            createFloor(2, 1, mapIndices, shuffledNames, colors) { map ->
                map[1][1] = Cell.ElevatorDown; map[20][20] = Cell.ElevatorDown
                map[20][1] = Cell.ElevatorUp; map[1][20] = Cell.ElevatorUp
            },
            createFloor(3, 2, mapIndices, shuffledNames, colors) { map ->
                map[1][1] = Cell.ElevatorUp; map[20][20] = Cell.ElevatorUp
                map[20][1] = Cell.ElevatorDown; map[1][20] = Cell.ElevatorDown
            },
            createFloor(4, 3, mapIndices, shuffledNames, colors) { map ->
                map[1][1] = Cell.ElevatorDown; map[20][20] = Cell.ElevatorDown
            }
        )
    }

    private fun createFloor(
        number: Int,
        index: Int,
        mapIndices: List<Int>,
        shuffledNames: Array<String>,
        colors: List<Color>,
        setupElevators: (Array<Array<Cell>>) -> Unit
    ): Floor {
        val template = getMap(mapIndices[index])
        val name = shuffledNames[index]

        val modifiedMap = template.map { it.copyOf() }.toTypedArray()

        setupElevators(modifiedMap)

        val emptyCells = findEmptyCells(modifiedMap)

        if (emptyCells.isNotEmpty()) {
            val (ky, kx) = emptyCells.removeAt(0)
            modifiedMap[ky][kx] = Cell.Key
        }

        repeat(minOf(25, emptyCells.size)) {
            val (cy, cx) = emptyCells.removeAt(0)
            modifiedMap[cy][cx] = Cell.Block
        }

        return Floor(number = number, color = colors[index], roomName = name, floorMap = modifiedMap)
    }

    private fun findEmptyCells(map: Array<Array<Cell>>): MutableList<Pair<Int, Int>> {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == Cell.Empty) {
                    emptyCells.add(y to x)
                }
            }
        }
        emptyCells.shuffle()
        return emptyCells
    }
}