package com.yupic.yupic.ui.form

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.yupic.yupic.SharedViewModel
import com.yupic.yupic.model.Node
import com.yupic.yupic.model.Option
import com.yupic.yupic.ui.EMOJI_DEFAULT
import com.yupic.yupic.ui.NODE_TYPE_MULTIPLE_CHOICE
import com.yupic.yupic.ui.NotFound
import com.yupic.yupic.ui.offset.ProjectCard
import com.yupic.yupic.ui.theme.YupicTheme
import timber.log.Timber

@ExperimentalPagerApi
@Composable
fun FormScreen(sharedViewModel: SharedViewModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val targetNodes = sharedViewModel.formNodes.observeAsState()

        targetNodes.value?.let { fetchedNodes ->
            if(fetchedNodes.isNotEmpty()){
                HorizontalPager(
                    count = fetchedNodes.size + 1 , // The last page will show the submit button
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) { page : Int ->

                    if(page == fetchedNodes.size){ // If it is the last page show submit button
                        SubmitButton {
                            sharedViewModel.calculateFormResult()

                        }
                    } else { // Show normal question card
                        QuestionCard(
                            fetchedNodes[page]
                        ){ updatedNode ->

                           sharedViewModel.updateNode(updatedNode)
                        }
                    }

                }
            }else{
                NotFound()
            }
        } ?: run {
            NotFound()
        }




    }

}

@Preview(showBackground = true)
@Composable
fun SubmitButtonPreview() {
    YupicTheme {
        SubmitButton {}
    }
}

@Composable
fun SubmitButton(onSubmit : () -> Unit) {
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { onSubmit() }) {
            Text(
                text = "Calcular",
                style = MaterialTheme.typography.button
            )
        }

    }
}

@Composable
fun QuestionCard(node: Node, onUpdateNode: (Node) -> Unit) {

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


            // Box with emoji
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.secondaryVariant)
            ) {
                val emojiContent =  if(node.category?.thumbnail?.isNotEmpty() == true){
                    node.category?.thumbnail ?: EMOJI_DEFAULT
                }else{
                    EMOJI_DEFAULT
                }
                Text(text = emojiContent, style = MaterialTheme.typography.h5)

            }

            Text(text = node.subtitle)

            if(node.type == NODE_TYPE_MULTIPLE_CHOICE){ // Present options
                LazyColumn{
                    node.options?.let { optionList ->
                        items(items = optionList, itemContent = {item ->

                            SingleSelectableItem(item, selectedValue = selectedOption.value){selected ->
                                selectedOption.value = selected
                                node.options?.forEach {
                                    it.selected = (selected.title == it.title)
                                }
                                onUpdateNode(node)
                            }

                        })
                    }
                }

            }else { // Present input text
                var text by remember {
                    mutableStateOf("")
                }
                Surface(modifier = Modifier
                    .padding(top = 128.dp)
                    .size(width = 64.dp, height = 64.dp)
                ) {
                    TextField(
                        value = text,
                        onValueChange = { value ->
                            if (value.length <= 2 && value.isDigitsOnly()) {
                                text = value
                                node.response = value.toDouble()
                                onUpdateNode(node)
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = MaterialTheme.typography.h6
                    )
                }
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

//@ExperimentalPagerApi
//@Preview(showBackground = true)
//@Composable
//fun FormScreenPreview() {
//    YupicTheme {
//        FormScreen(sharedViewModel)
//    }
//
//}

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
        ){}
    }


}