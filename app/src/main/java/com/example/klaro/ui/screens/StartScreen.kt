package com.example.klaro.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klaro.R
import com.example.klaro.ui.theme.KlaroTheme

@Composable
fun StartScreen(onStartClick: () -> Unit = {})
{


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {

        Image(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(20.dp)),
            painter = painterResource(id = R.drawable.logo_main),
            contentDescription = "Klaro Logo",
            )

        Text(modifier = Modifier.align(CenterHorizontally), text = "Klaro", textAlign = TextAlign.Center,
            fontSize = 78.sp,
            fontWeight = FontWeight.Bold,
            style = typography.displayLarge,
            color = colorScheme.onBackground,
        )

        Text(modifier = Modifier.padding(bottom = 100.dp), text = "Whatever you learn,\n you learn for yourself", textAlign = TextAlign.Center,
            fontSize = 15.sp,
            style = typography.titleMedium,
            color = colorScheme.onBackground,
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 100.dp)
                .height(48.dp),
            shape = RoundedCornerShape(20),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            onClick =  onStartClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Zaloguj się", style = typography.labelLarge)
                Icon(modifier = Modifier.padding(start = 10.dp),
                     imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                     contentDescription = null,
                     tint = colorScheme.onPrimary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview(){
    KlaroTheme {
        StartScreen()
    }
}