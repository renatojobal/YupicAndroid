package com.yupic.yupic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yupic.yupic.ui.theme.YupicTheme

@Composable
fun NotFound() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
        ,
        contentAlignment = Alignment.Center

    ) {
        Text(text = "Not Found")
    }
}

@Preview(showBackground = true)
@Composable
fun NotFoundPreview() {
    YupicTheme {
        NotFound()
    }

}

const val NODE_TYPE_MULTIPLE_CHOICE = "multipleChoice"
const val NODE_TYPE_NUMBER = "number"
