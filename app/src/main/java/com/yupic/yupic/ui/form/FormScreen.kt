package com.yupic.yupic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yupic.yupic.model.Node

@Composable
fun FormScreen() {
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

@Composable
fun QuestionCard(node: Node) {
    BoxWithConstraints {
        Column {
            Text(text = node.title)
            node.nestedNodes.forEach { targetNode ->
                SingleSelectableItem(targetNode)
            }
        }
    }
}

@Composable
fun SingleSelectableItem(node: Node) {
    val isChecked = remember { mutableStateOf(false) }
    Box(modifier = Modifier.background(MaterialTheme.colors.primaryVariant)) {
        Row {
            Text(text = node.title)
            Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
        }
    }
}


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