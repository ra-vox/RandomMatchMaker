package com.example.randommatchmaker

data class TableTennisMatch(val firstPlayer: String, val secondPlayer: String) {
    var listOfGameSets: MutableList<Pair<Int?, Int?>> = mutableListOf(
        Pair(3, 11),
        Pair(13, 11),
        Pair(11, 2),
        Pair(11, 9),
        Pair(null, null)
    )

    fun pointsPlayer(player: String): MutableList<Int?> {
        var playerPoints: MutableList<Int?> = emptyList<Int?>().toMutableList()
        (listOfGameSets.forEach(){
            pair ->  if (player.equals(firstPlayer)) {
            playerPoints.add(pair.first)
        } else if (player.equals(secondPlayer)){
            playerPoints.add(pair.second)
        } else {
            playerPoints.add(-1)
        }
        })
        return playerPoints
    }

    override fun toString(): String {
        var gameString:String = ""
        listOfGameSets.forEach(){
                pair -> if (pair.first != null && pair.second != null){
            gameString = gameString + "${pair.first.toString()} , ${pair.second.toString()}/ "
        } else {
            gameString = gameString + "-, - /"
        }
        }
        return "$firstPlayer - $secondPlayer $gameString"
    }
}
