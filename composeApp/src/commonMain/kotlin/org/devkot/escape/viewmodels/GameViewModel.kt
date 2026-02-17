package org.devkot.escape.viewmodels


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.devkot.escape.enums.Cell
import org.devkot.escape.enums.Direction
import org.devkot.escape.enums.VisionType
import org.devkot.escape.models.Floor
import org.devkot.escape.states.UiState
import org.devkot.escape.utils.FloorGenerator
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val floors = mutableStateListOf<Floor>().apply {
        addAll(FloorGenerator.generateAllFloors())
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Menu)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    private var isMoving = false


    fun changeFloor(targetFloorIndex: Int, x: Int, y: Int) {
        val currentState = _uiState.value as? UiState.Playing ?: return

        viewModelScope.launch {
            val currentFloor = currentState.floor
            val currentMap = currentFloor.floorMap
            val currentCell = currentMap[currentState.heroY][currentState.heroX]

            currentMap[currentState.heroY][currentState.heroX] = when (currentCell) {
                Cell.HeroOnElevatorUp -> Cell.ElevatorUp
                Cell.HeroOnElevatorDown -> Cell.ElevatorDown
                Cell.Hero -> Cell.Empty
                else -> Cell.Empty
            }

            floors[currentFloor.number - 1] = currentFloor.copy(floorMap = currentMap)

            _uiState.value = currentState.copy(
                heroX = x, heroY = y
            )

            val nextFloorData = floors[targetFloorIndex]
            val nextMap = nextFloorData.floorMap.map { it.copyOf() }.toTypedArray()

            val cellUnderHero = nextMap[y][x]
            nextMap[y][x] = when (cellUnderHero) {
                Cell.ElevatorUp -> Cell.HeroOnElevatorUp
                Cell.ElevatorDown -> Cell.HeroOnElevatorDown
                else -> Cell.Hero
            }

            val updatedNextFloor = nextFloorData.copy(floorMap = nextMap)

            delay(100)

            _uiState.value = (_uiState.value as? UiState.Playing)?.copy(
                floor = updatedNextFloor, heroX = x, heroY = y
            ) ?: return@launch

            isMoving = false
        }
    }

    fun moveHero(direction: Direction) {
        if (isMoving) return
        val state = _uiState.value as? UiState.Playing ?: return

        viewModelScope.launch {
            isMoving = true
            val currentFloor = state.floor
            val map = currentFloor.floorMap

            val newX = state.heroX + direction.x
            val newY = state.heroY + direction.y

            if (newY !in map.indices || newX !in map[0].indices) {
                isMoving = false
                return@launch
            }

            val targetCell = map[newY][newX]

            if (targetCell == Cell.Border || (targetCell == Cell.Door && !state.doorOpened)) {
                isMoving = false
                return@launch
            }

            val currentCell = map[state.heroY][state.heroX]
            map[state.heroY][state.heroX] = when (currentCell) {
                Cell.HeroOnElevatorUp -> Cell.ElevatorUp
                Cell.HeroOnElevatorDown -> Cell.ElevatorDown
                Cell.HeroOnDoor -> Cell.Door
                Cell.Hero -> Cell.Empty
                else -> Cell.Empty
            }


            var newMoneyCount = state.moneyCount
            var newKeyCount = state.keyCount

            var nextFloorIndex: Int? = null
            var shouldWin = false

            when (targetCell) {
                Cell.ElevatorUp -> nextFloorIndex = currentFloor.number
                Cell.ElevatorDown -> nextFloorIndex = currentFloor.number - 2
                Cell.Block -> {
                    newMoneyCount += 1
                }

                Cell.Key -> {
                    newKeyCount = (state.keyCount + 1).coerceAtMost(4)
                }

                Cell.Door -> {
                    shouldWin = true
                }

                else -> {}
            }

            if (shouldWin) {
                finishGame(newMoneyCount)
                return@launch
            }

            if (nextFloorIndex != null && nextFloorIndex in floors.indices) {
                map[newY][newX] = when (targetCell) {
                    Cell.ElevatorUp -> Cell.HeroOnElevatorUp
                    Cell.ElevatorDown -> Cell.HeroOnElevatorDown
                    else -> Cell.Hero
                }

                val updatedFloor = currentFloor.copy(floorMap = map)
                floors[currentFloor.number - 1] = updatedFloor

                _uiState.value = state.copy(
                    heroX = newX,
                    heroY = newY,
                    moneyCount = newMoneyCount,
                    keyCount = newKeyCount,
                    doorOpened = newKeyCount >= 4,
                    floor = updatedFloor
                )

                changeFloor(nextFloorIndex, newX, newY)
            } else {
                map[newY][newX] = when (targetCell) {
                    Cell.Block, Cell.Key -> Cell.Hero
                    else -> Cell.Hero
                }

                val updatedFloor = currentFloor.copy(floorMap = map)
                floors[currentFloor.number - 1] = updatedFloor

                _uiState.value = state.copy(
                    heroX = newX,
                    heroY = newY,
                    floor = updatedFloor,
                    moneyCount = newMoneyCount,
                    keyCount = newKeyCount,
                    doorOpened = newKeyCount >= 4
                )

                isMoving = false
            }
        }
    }

    private fun finishGame(moneyCount: Int) {
        _uiState.value = UiState.Win(moneyCount = moneyCount)
    }


    private fun updateDoorState(currentState: UiState.Playing, newKeyCount: Int) {
        val canOpen = newKeyCount >= 4

        _uiState.value = currentState.copy(
            keyCount = newKeyCount, doorOpened = canOpen
        )
    }

    fun inc() {
        val currentState = _uiState.value as? UiState.Playing ?: return
        val nextKeys = (currentState.keyCount + 1).coerceAtMost(4)
        updateDoorState(currentState, nextKeys)
    }


    fun openMenu() {
        _uiState.value = UiState.Menu
    }

    fun startGame(visionType: VisionType) {

        viewModelScope.launch {
            isMoving = false
            val newFloors = FloorGenerator.generateAllFloorsAsync()
            floors.clear()
            floors.addAll(newFloors)

            val startFloorIndex = Random.nextInt(floors.size)
            val startFloor = floors[startFloorIndex]

            val emptyCells = mutableListOf<Pair<Int, Int>>()
            val map = startFloor.floorMap
            for (y in map.indices) {
                for (x in map[y].indices) {
                    if (map[y][x] == Cell.Empty) {
                        emptyCells.add(x to y)
                    }
                }
            }

            val (startX, startY) = emptyCells.randomOrNull() ?: (2 to 2)

            val mutableMap = map.map { row -> row.copyOf() }.toTypedArray()
            mutableMap[startY][startX] = Cell.Hero

            val updatedFloor = startFloor.copy(floorMap = mutableMap)
            floors[startFloorIndex] = updatedFloor

            _uiState.value = UiState.Playing(
                visionType = visionType,
                heroX = startX,
                heroY = startY,
                doorOpened = false,
                keyCount = 0,
                moneyCount = 0,
                floor = updatedFloor
            )
        }
    }


}
