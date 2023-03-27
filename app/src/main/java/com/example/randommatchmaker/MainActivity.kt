package com.example.randommatchmaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
                    randomMatchMaker()
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun randomMatchMaker() {

    var state: Int by remember {
        mutableStateOf(0)
    }
    var listOfContestants by remember {
        mutableStateOf(listOf<String>("Apple", "Banana", "Coconut", "Date", "Pineapple", "Pen", "Rose", "Sunshine", "Darkness"))
    }
    var randomListOfContestants by remember {
        mutableStateOf(listOfContestants)
    }

    var matchListofGameSets by remember {
        mutableStateOf(listOf<String>("-","-","-","-","-" ))
    }
    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .background(color = MaterialTheme.colors.secondary)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Table Tennis Tournament Tool",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp))
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
                    Row() {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { randomListOfContestants = makeMatches(listOfContestants) }) {
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
                    var list: List<String> = emptyList()
                    editContestants(
                        value = listOfContestants.toString().removeSurrounding("[", "]"),
                        onValueChange = {
                            listOfContestants = it.split(", ").map { it.trim() }
                            randomListOfContestants = listOfContestants
                        })
                    goBackButton(value = state, onClick = { state = 0 } )
                } // EDIT
                2 -> {
                    StartKOSystem(
                        value = randomListOfContestants,
                        onValueChange = { state = 0 }
                    ) // start KO System Game
                    goBackButton(value = state, onClick = { state = 0 } )
                }
                3 -> {
                    playMatch(firstPlayer = listOfContestants[0], secondPlayer = listOfContestants[2],)
                    goBackButton(value = state, onClick = { state = 0 } )
                }
                4 -> TODO()
                // Grouped Events Switch between different Groups show current games in front
                else -> state = 0
            }
        }
    }
}




@Composable
fun playMatch(firstPlayer: String, secondPlayer: String) {
    var playedMatch = TableTennisMatch(firstPlayer, secondPlayer)
    Row() {

        Spacer(modifier = Modifier.weight(1f))
        Card(modifier = Modifier
            .wrapContentSize()
            .weight(3f)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(modifier = Modifier) {
                    Text(text = playedMatch.firstPlayer.toString(), Modifier.weight(1f))
                    Text(text = " ${playedMatch.pointsPlayer(playedMatch.firstPlayer).toString()}",
                        Modifier.weight(2f)
                    )

                }
                Text(text = "vs")
                Row() {
                    Text(text = playedMatch.secondPlayer.toString(), Modifier.weight(1f))
                    Text(text = " ${playedMatch.pointsPlayer(playedMatch.secondPlayer).toString()}",
                        Modifier.weight(2f))
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StartKOSystem(value: List<String>, onValueChange: (String) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()

        ) {
        var challenger: String = ""
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
fun editContestants(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {"separate by , "},)
}

@Composable
fun goBackButton(value: Int, onClick: () -> Unit){
    Button(
        onClick = onClick) {
        Text(text = "Go Back")
    }
}

@Composable
fun enterData() {
    var contestants by remember { mutableStateOf(TextFieldValue()) }
    Column() {
        TextField(value = contestants,
            onValueChange = { contestants = it })
    }
}

fun makeMatches(contestants: List<String>): List<String> {
    val contestants = contestants.toMutableList()
    var matches: List<String>
    val random = java.util.Random()
    var drawnContestants = emptyList<String>()
    while (contestants.isNotEmpty()) {
        val index = random.nextInt(contestants.size)
        drawnContestants = drawnContestants + contestants[index]
        contestants.removeAt(index)
    }
    return drawnContestants
}


@Composable
fun ShowContestant(randomListOfContestants: List<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
    randomListOfContestants.forEach() { contestant ->
        Row() {
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
                        text = ("$contestant"),
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



@Composable
fun showBox(protagonist: String, antagonist: String){
    Card(modifier = Modifier
        .background(Color.White)
        .width(240.dp)
        .border(2.dp, Color.Black)
        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)


    ){
        Column() {
            Text(text = protagonist, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = "vs")
            Text(text = antagonist, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}