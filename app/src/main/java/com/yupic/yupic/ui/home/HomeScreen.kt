package com.yupic.yupic.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yupic.yupic.SharedViewModel
import com.yupic.yupic.model.Category
import timber.log.Timber

@Composable
fun HomeScreen (sharedViewModel: SharedViewModel, onOffsetClicked : () -> Unit){

    val user by sharedViewModel.user.observeAsState()
    val selectedCategory by sharedViewModel.selectedCategory.observeAsState()


    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 48.dp)
            .fillMaxSize()
    ){


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Timber.d("TargetPercentage: ${selectedCategory?.percentage ?: 100.00}")
            Timber.d("Hola")
            CircularProgressBar(
                percentage = selectedCategory?.percentage ?: 0.00,
                suffix = " kg",
                insideText = selectedCategory?.categoryCarbonFootprintKg?.trimDecimals() ?: "0"
            )
            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = { onOffsetClicked() }
            ) {
                Text(text = "COMPENSAR")
            }
            ActivitiesListPresenter(sharedViewModel)
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen {}
//}

@Composable
fun CircularProgressBar(
    percentage : Double = 0.0,
    suffix : String = "",
    insideText : String = percentage.toString(),
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
        targetValue = (if (animationPlayed) percentage else 0.0).toFloat(),
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
        modifier = Modifier
            .size(radius * 2f)
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
            text = insideText + suffix,
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold

        )
    }
    
}

@Preview(showBackground = true)
@Composable
fun CircularProgressPreview() {
    CircularProgressBar(percentage = 50.0)
}

@Composable
fun ActivityItem(
    category: Category
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = category.thumbnail?: "\uD83C\uDFED")
                Text(modifier = Modifier.padding(start=16.dp), text = category.title)
            }

            Text(text = category.categoryCarbonFootprintKg.trimDecimals())
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    val dummyActivity = Category(percentage = 0.36, title = "Transport", thumbnail = "\uD83C\uDFED")
    ActivityItem(dummyActivity)
}


@Composable
fun ActivitiesListPresenter(sharedViewModel: SharedViewModel) {


    val targetCategories by sharedViewModel.categories.observeAsState()

    Text(
        text = "Activities",
        modifier = Modifier
            .padding(4.dp),
        style = MaterialTheme.typography.h5
    )
    targetCategories?.let { listCategories ->
        if (listCategories.isNotEmpty()){
            LazyColumn{
                items(listCategories.size){ index ->
                    ActivityItem(category = listCategories[index])
                }
            }
        }else{
            Text(text = "No hay actividades registradas aún")
        }
    }?: run {
        Text(text = "No hay actividades registradas aún")
    }





}

//@Preview(showBackground = true)
//@Composable
//fun ActivitiesListPreview() {
//    val dummyActivity1 = Category(percentage = 0.36, title = "Transport", thumbnail = "\uD83C\uDFE1")
//    val dummyActivity2 = Category(percentage = 0.36, title = "Transport", thumbnail = "\uD83D\uDE8B")
//    ActivitiesListPresenter()
//}

