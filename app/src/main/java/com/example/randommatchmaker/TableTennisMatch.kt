package com.example.randommatchmaker

data class TableTennisMatch(val firstPlayer: String, val secondPlayer: String) {
    var listOfGameSets = mutableListOf(
        arrayOf(0, 0),
        arrayOf(13, 11),
        arrayOf(11, 2),
        arrayOf(11, 9),
        arrayOf(0, 0)
    )
}
