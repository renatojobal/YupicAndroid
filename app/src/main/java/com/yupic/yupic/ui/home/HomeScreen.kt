package com.yupic.yupic.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yupic.yupic.SharedViewModel
import com.yupic.yupic.model.Category
import timber.log.Timber

@Composable
fun HomeScreen(sharedViewModel: SharedViewModel, onOffsetClicked: () -> Unit) {


    val targetCategories by sharedViewModel.categories.observeAsState()
    var selectedCategory by remember { (mutableStateOf(targetCategories?.get(0))) }


    val animDuration = 1000
    val animDelay = 0


    val currentPercentage by animateFloatAsState(
        targetValue = (selectedCategory?.percentage ?: 0.0).toFloat(),
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )


    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 48.dp)
            .fillMaxSize()
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            selectedCategory?.let { safeCategory ->
                Text(text = safeCategory.title + " " + safeCategory.thumbnail,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top=8.dp))
            }

            CircularProgressBar(
                percentage = currentPercentage,
                suffix = " kg",
                insideText = selectedCategory?.categoryCarbonFootprintKg?.trimDecimals() ?: "0"
            )
            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = { onOffsetClicked() }
            ) {
                Text(text = "COMPENSAR", style = MaterialTheme.typography.button)
            }
            ActivitiesListPresenter(
                sharedViewModel,
                selectedCategory = selectedCategory
            ) { wantedCategory ->
                Timber.i("New category selected")
                selectedCategory = wantedCategory

            }
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
    percentage: Float = 0f,
    suffix: String = "",
    insideText: String = percentage.toString(),
    fontSize: TextUnit = 28.sp,
    radius: Dp = 100.dp,
    color: Color = MaterialTheme.colors.onSurface,
    strokeWidth: Dp = 8.dp
) {


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(radius * 2f)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                (360 * percentage / 100),
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
    CircularProgressBar(percentage = 50f)
}

@Composable
fun ActivityItem(
    presentingCategory: Category,
    selectedCategory: Category?,
    onSelectedCategory: (Category) -> Unit
) {

    val itemModifier: Modifier = if (selectedCategory == presentingCategory) {
        Modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
    } else {
        Modifier
            .fillMaxWidth()
    }

    Box(
        modifier = itemModifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (presentingCategory.thumbnail ?: "\uD83C\uDFED"),
                    modifier = Modifier.clickable { onSelectedCategory(presentingCategory) })
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { onSelectedCategory(presentingCategory) },
                    text = presentingCategory.title
                )
            }

            Text(
                text = presentingCategory.categoryCarbonFootprintKg.trimDecimals(),
                modifier = Modifier.clickable { onSelectedCategory(presentingCategory) })
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    val dummyActivity = Category(percentage = 0.36, title = "Transport", thumbnail = "\uD83C\uDFED")
    ActivityItem(dummyActivity, Category()) {}
}


@Composable
fun ActivitiesListPresenter(
    sharedViewModel: SharedViewModel,
    selectedCategory: Category?,
    onSelectedCategory: (Category) -> Unit
) {


    val targetCategories by sharedViewModel.categories.observeAsState()


    Text(
        text = "Activities",
        modifier = Modifier
            .padding(4.dp),
        style = MaterialTheme.typography.h5
    )
    targetCategories?.let { listCategories ->
        if (listCategories.isNotEmpty()) {
            LazyColumn {
                items(listCategories.size) { index ->
                    ActivityItem(
                        presentingCategory = listCategories[index],
                        selectedCategory = selectedCategory
                    ) {
                        onSelectedCategory(it)
                    }
                }
            }
        } else {
            Text(text = "No hay actividades registradas aún")
        }
    } ?: run {
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

