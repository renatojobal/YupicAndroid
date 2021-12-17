package com.yupic.yupic.ui.login

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "YUPIC")
            
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Login with Facebook")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Login with Google")
            }

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Login with UTPL")
            }

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Register")
            }

        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}