package com.example.randommatchmaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randommatchmaker.ui.theme.RandomMatchMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomMatchMakerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomMatchMakerApp()
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun RandomMatchMakerApp() {

    var state: Int by remember {
        mutableStateOf(0)
    }


    var listOfContestants by remember {
        mutableStateOf(listOf("Pineapple","Banana","Coconut","Date","Apple","Pen","Rose","Sunshine","Darkness"))
    }


    var randomListOfContestants by remember {
        mutableStateOf(listOfContestants)

    }

    Scaffold(
        topBar = {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.secondary)
            .fillMaxWidth(),) {
            Row(modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GoBackButton {
                    state = 0
                }
                Text(text = "Table Tennis Tournament Tool",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    )

            }
        }

        }
        ) {
        Column(
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(8.dp)

        ) {

            when (state) {
                0 -> {
                    Text(
                        "Matches",
                        color = MaterialTheme.colors.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold

                    )

                    ShowContestant(randomListOfContestants)
                    Row {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {randomListOfContestants = listOfContestants.shuffled().toMutableList()
                                }) {
                            Text(text = "Random")
                        }
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { state = 1 }) {
                            Text(text = "Edit")
                        }
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { state = 2 }) {
                            Text(text = "KO")
                        }
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { state = 3}) {
                            Text(text = "Play Match")
                        }
                    }
                }
                1 -> {
                    EditContestants(
                        value = listOfContestants.toString().removeSurrounding("[", "]"),
                        onValueChange = { showContestantList ->
                            listOfContestants = showContestantList.split(", ").map { it.trim() } as MutableList<String>
                            randomListOfContestants = listOfContestants
                        })

                } // EDIT
                2 -> {
                    StartKOSystem(
                        value = randomListOfContestants
                    ) // start KO System Game

                }
                3 -> {
                    PlayMatch(firstPlayer = listOfContestants[0], secondPlayer = listOfContestants[2])

                }
                4 -> TODO()
                // Grouped Events Switch between different Groups show current games in front
                else -> state = 0
            }
        }
    }
}


@Composable
fun PlayMatch(firstPlayer: String, secondPlayer: String) {

    //todo: write logic to transition from gameset to gameset, needs winning condition.
    val currentGame by remember {
        mutableStateOf(2) }

    val game = TableTennisMatch(firstPlayer, secondPlayer)

    val gamePoints by remember {
        mutableStateOf(game.listOfGameSets[currentGame])
    }


        // Spacer(modifier = Modifier.weight(1f))
        Card(modifier = Modifier
            .padding(start = 8.dp, bottom = 24.dp, end = 8.dp, top = 4.dp)
            .fillMaxSize(),
            elevation = 8.dp

        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier
                        .background(MaterialTheme.colors.secondary)
                        .fillMaxWidth()
                        .weight(1f),
                horizontalArrangement = Arrangement.Center ) {
                    Text(text = "Set $currentGame",
                    fontSize = 36.sp)
                }

                PlayerCards(modifier = Modifier.weight(8f), value = game, points = gamePoints)
                val buttonModifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 16.dp)
                Row(Modifier.weight(1.1f)) {


                    PlusOneButton(buttonModifier

                    ) {

                        gamePoints[0] = gamePoints[0] + 1
                        println("click ${gamePoints[0]}")
                    }
                    PlusOneButton(buttonModifier

                    ) {
                        gamePoints[1] = gamePoints[1] + 1
                        println("click ${gamePoints[1]}")

                    }

                }


                Row(modifier = Modifier
                    .weight(0.5f)
                    .background(MaterialTheme.colors.onPrimary)
                    .fillMaxWidth()) {
                    Text(text = "Edit Score",
                        modifier = Modifier
                            )
                }
            }
        }
}

/*private fun checkWinCondition(currentGame: Int, game: TableTennisMatch): Boolean {
    if (game.listOfGameSets[currentGame][0] == 11)
        println("Player one wins.")
    else if (game.listOfGameSets[currentGame][1] == 11)
        println("Player two wins.")
    else
        return false
    return true

}*/

@Composable
private fun PlayerCards(
    modifier: Modifier,
    value: TableTennisMatch,
    points: Array<Int>,

) {
    Row(modifier){
        Column(
            modifier = Modifier
                .weight(1f)
                    ,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value.firstPlayer,
                fontSize = 36.sp,
                modifier = Modifier.weight(2f)
            )
            Text(text = points[0].toString(),
                fontSize = 64.sp,
                color = Color.White,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier
                .weight(2f)
            ) // to align the name and numbers.



        }
        Column(modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colors.secondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier
                .weight(2f)
            )
            Text(text = " ${points[1]}",
                fontSize = 64.sp,
                color = Color.White,
                modifier = Modifier
                    .weight(2f)
            )
            Text(text = value.secondPlayer,
                fontSize = 36.sp,
                modifier = Modifier
                    .weight(2f)
            )


        }

    }
}

@Composable
private fun PlusOneButton(modifier: Modifier, onClick: () -> Unit){
Card(modifier.wrapContentSize()) {
        Button(onClick = onClick,
        ) {
            Text(text = "+1",
                fontSize = 24.sp
            )

        }
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StartKOSystem(value: List<String>) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()

        ) {
        var challenger = ""
        value.forEachIndexed { index, contestant ->
            if (index.mod(2) == 0 && index+1 < value.size) {
                challenger = contestant
            } else if ( index +1 >= value.size){
                Card(modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(percent = 10))
                    .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
                    elevation = 8.dp
                ){
                    Column(modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)) {
                        Text(text = contestant, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text(text = "vs")
                        Text(text = "Free", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                }
            }
            else {
                Card(modifier = Modifier
                    .width(200.dp)
                    //.background(MaterialTheme.colors.primary)
                    .clip(RoundedCornerShape(percent = 10))
                    .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
                    elevation = 8.dp
                ){
                    Column(modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)) {
                        Text(text = challenger, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text(text = "vs")
                        Text(text = contestant, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                }
                //Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

}


//TODO: how do I create a TextField so that i can edit a String List?
@Composable
fun EditContestants(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            @Suppress("UNUSED_EXPRESSION")
            "separate by , "
        },)
}

@Composable
fun GoBackButton(onClick: () -> Unit){
    IconButton(
        modifier = Modifier.padding(start = 0.dp),
        onClick = onClick) {
        Icon(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = "Home Button")
    }
}


@Composable
fun ShowContestant(randomListOfContestants: List<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
    randomListOfContestants.forEach { contestant ->
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Card(

                modifier = Modifier
                    .padding(4.dp)
                    .wrapContentHeight()
                    .weight(3f)
                    .clip(RoundedCornerShape(percent = 25))
                ,
                elevation = 8.dp,
            ) {
                Row(horizontalArrangement = Arrangement.Center
                    ) {
                    //Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = (contestant),
                        //fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    //Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



/* TODO: use in KO System
@Composable
fun ShowBox(firstPlayer: String, secondPlayer: String){
    Card(modifier = Modifier
        .background(Color.White)
        .width(240.dp)
        .border(2.dp, Color.Black)
        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)

    ){
        Column {
            Text(text = firstPlayer, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = "vs")
            Text(text = secondPlayer, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}*/
