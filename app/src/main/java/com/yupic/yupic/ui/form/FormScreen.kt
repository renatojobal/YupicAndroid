package com.yupic.yupic.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.yupic.yupic.model.Node

@ExperimentalPagerApi
@Composable
fun FormScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        
        HorizontalPager(
            count = 6,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { page ->
             QuestionCard(
                        Node(
                            title = "¿Qué tipo de transporte usas?",
                            weight = 0.0,
                            nestedNodes = listOf(
                                Node(
                                    "Carro",
                                    weight = 2.0
                                ),
                                Node(
                                    "Transporte público",
                                    weight = 1.0
                                ),
                                Node(
                                    "Tren",
                                    weight = 2.0
                                )
                            )
                        )
                    )
        }

    }

}

@Composable
fun QuestionCard(node: Node) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primaryVariant,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp, vertical = 24.dp)
            )
    {
        Column {
            Text(
                text = node.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            node.nestedNodes.forEach { targetNode ->
                SingleSelectableItem(targetNode)
            }
        }
    }
}

@Composable
fun SingleSelectableItem(node: Node) {
    val isChecked = remember { mutableStateOf(false) }
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = node.title,
            style = MaterialTheme.typography.body1
            )
            Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
        }
    }
}


@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    FormScreen()
}

@Preview(showBackground = true)
@Composable
fun QuestionCardPreview() {
    QuestionCard(
        Node(
            title = "¿Qué tipo de transporte usas?",
            weight = 0.0,
            nestedNodes = listOf(
                Node(
                    "Carro",
                    weight = 2.0
                ),
                Node(
                    "Transporte público",
                    weight = 1.0
                ),
                Node(
                    "Tren",
                    weight = 2.0
                )
            )
        )
    )
}