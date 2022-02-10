package com.yupic.yupic.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yupic.yupic.R
import com.yupic.yupic.ui.theme.YupicTheme

@Composable
fun LoginScreen(onLoggedSuccess: () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 64.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_yupic),
                contentDescription = "yupic",
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "YUPIC",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(16.dp)
            )
            val modifierButton = Modifier
                .padding(horizontal = 40.dp, vertical = 8.dp)
                .fillMaxWidth()

            Button(modifier = modifierButton, onClick = {
                onLoggedSuccess()
            }) {
                Text(text = "LOGIN WITH GOOGLE", style = MaterialTheme.typography.button)
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    YupicTheme {
        LoginScreen({})
    }

}