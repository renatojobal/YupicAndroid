package com.yupic.yupic.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yupic.yupic.R
import com.yupic.yupic.model.Activity

@Composable
fun HomeScreen (){
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressBar(percentage = 0.8f, number = 100)
            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Compensar")
            }
            ActivitiesListPresenter(activities = listOf())
        }

    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun CircularProgressBar(
    percentage : Float = 0f,
    number : Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 100.dp,
    color : Color = MaterialTheme.colors.onSurface,
    strokeWidth : Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true){
        animationPlayed = true
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
            .padding(8.dp)
    ){
        Canvas(modifier = Modifier.size(radius * 2f)){
            drawArc(
                color = color,
                -90f,
                360 * currentPercentage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = (currentPercentage.value * number).toInt().toString(),
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold

        )
    }
    
}

@Preview(showBackground = true)
@Composable
fun CircularProgressPreview() {
    CircularProgressBar(percentage = 0.8f, number = 100)
}

@Composable
fun ActivityItem(
    activity: Activity
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Icon(painterResource(id = activity.iconResource), contentDescription = activity.title)
            Text(text = activity.title)
            Text(text = (activity.percentage * 100).toInt().toString())
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    val dummyActivity = Activity(percentage = 0.36f, title = "Transport", iconResource = R.drawable.ic_money)
    ActivityItem(dummyActivity)
}


@Composable
fun ActivitiesListPresenter(activities : List<Activity>) {
    Text(
        text = "Activities",
        modifier = Modifier
            .padding(4.dp)
    )

    if (activities.isNotEmpty()){
        LazyColumn{
            items(activities.size){index ->
                ActivityItem(activity = activities[index])
            }
        }
    }else{
        Text(text = "No hay actividades registradas aún")
    }



}

@Preview(showBackground = true)
@Composable
fun ActivitiesListPreview() {
    val dummyActivity1 = Activity(percentage = 0.36f, title = "Transport", iconResource = R.drawable.ic_money)
    val dummyActivity2 = Activity(percentage = 0.36f, title = "Transport", iconResource = R.drawable.ic_money)
    ActivitiesListPresenter(listOf(dummyActivity1, dummyActivity2))
}

