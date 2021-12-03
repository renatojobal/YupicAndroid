package com.yupic.yupic.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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

@Composable
fun HomeScreen (){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        CircularProgressBar(percentage = 0.8f, number = 100)
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
    radius: Dp = 50.dp,
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
    percentage: Float,
    title: String,
    iconResource: Int
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Icon(painterResource(id = iconResource), contentDescription = title)
            Text(text = title)
            Text(text = (percentage * 100).toInt().toString())
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    ActivityItem(percentage = 0.36f, title = "Transport", iconResource = R.drawable.ic_money)
}
