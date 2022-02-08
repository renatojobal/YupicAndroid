package com.yupic.yupic.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.yupic.yupic.model.Node
import com.yupic.yupic.model.Option
import com.yupic.yupic.ui.offset.ProjectCard
import com.yupic.yupic.ui.theme.YupicTheme
import timber.log.Timber

@ExperimentalPagerApi
@Composable
fun FormScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        HorizontalPager(
            count = 6,
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) { page ->
            QuestionCard(
                Node(
                    title = "¿Qué tipo de transporte usas?",
                    subtitle = "¿Qúe tipo de vehículo usa?",
                    factor = 10.0,
                    type = "multipleChoice",
                    options = listOf(
                        Option(
                            title = "Menos de 100 metros cuadrados",
                            value = 40.0
                        ),
                        Option(
                            title = "Hasta de 150 metros cuadrados",
                            value = 40.0
                        ),
                        Option(
                            title = "Más de 150 metros cuadrados",
                            value = 40.0
                        )
                    )
                )
            )
        }

    }

}

@Composable
fun QuestionCard(node: Node) {

    val selectedOption = remember { (mutableStateOf<Option?>(null)) }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)

    ) {
        val (_, refImage) = createRefs()
        val (_, refBackground) = createRefs()


        // Rounded background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, bottom = 64.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                )

        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(refImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }
        ) {


            // Box of temperature with emoji
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.secondaryVariant)
            ) {
                Text(text = "\uD83D\uDD25")
            }

            Text(text = node.subtitle)



            if(node.type == "multipleChoice"){ // Present options

                LazyColumn{
                    node.options?.let {
                        items(items = it, itemContent = {item ->

                            SingleSelectableItem(item, selectedValue = selectedOption.value){
                                selectedOption.value = it
                            }

                        })
                    }
                }

            }else { // Present input text

            }



        }

    }
}

@Composable
fun SingleSelectableItem(option: Option, selectedValue : Option?,  onClickListener: (Option) -> Unit) {
    val isChecked = option.title == selectedValue?.title ?: "z"

    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onSecondary,
                shape = MaterialTheme.shapes.large
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isChecked,
                onClick = {
                    Timber.d("Radio button clicked")
                    onClickListener(option)
                }
            )
            Text(modifier = Modifier.padding(start = 8.dp),
                text = option.title,
                style = MaterialTheme.typography.body1
            )

        }
    }
}

@Preview
@Composable
fun SingleSelectableItemPreview(){
    val example = Node(
        title = "Transporte",
        subtitle = "¿Qúe tipo de vehículo usa?",
        factor = 10.0,
        type = "multipleChoice",
        options = listOf(
            Option(
                title = "Menos de 100 metros cuadrados",
                value = 40.0
            ),
            Option(
                title = "Hasta de 150 metros cuadrados",
                value = 40.0
            ),
            Option(
                title = "Más de 150 metros cuadrados",
                value = 40.0
            )
        )
    )

    YupicTheme {
        SingleSelectableItem(option = example.options?.get(1)!!,
            selectedValue = example.options?.get(0)!!){

        }
    }
}

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    YupicTheme {
        FormScreen()
    }

}

@Preview(showBackground = true)
@Composable
fun QuestionCardPreview() {
    YupicTheme {
        QuestionCard(
            Node(
                title = "Transporte",
                subtitle = "¿Qúe tipo de vehículo usa?",
                factor = 10.0,
                type = "multipleChoice",
                options = listOf(
                    Option(
                        title = "Menos de 100 metros cuadrados",
                        value = 40.0
                    ),
                    Option(
                        title = "Hasta de 150 metros cuadrados",
                        value = 40.0
                    ),
                    Option(
                        title = "Más de 150 metros cuadrados",
                        value = 40.0
                    )
                )
            )
        )
    }


}