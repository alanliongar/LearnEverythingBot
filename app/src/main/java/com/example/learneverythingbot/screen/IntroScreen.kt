package com.example.learneverythingbot.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.learneverythingbot.R
import com.example.learneverythingbot.components.GetStartedButton

@Composable
fun IntroScreen(innerPadding: PaddingValues, navController: NavHostController) {
    Box(modifier = Modifier.padding(innerPadding)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                IntroImagen()
                Spacer(modifier = Modifier.height(24.dp))
                WelcomeText(navController = navController)
            }
        }
    }

}

@Composable
fun IntroImagen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Si tienes una imagen de letras, asegúrate de que se llame letras_intro.webp
        // Si no la tienes, comenta o elimina esta parte
        /*
        Image(
            painter = painterResource(id = R.drawable.letras_intro),
            contentDescription = "Texto de introducción",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        */

        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier
                .widthIn(max = 400.dp)
                .heightIn(max = 400.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun WelcomeText(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.Bemvindo),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.mensaje_de_bienvenida),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        GetStartedButton(
            onClickNavigate = {
                navController.navigate(route = "chatScreen")
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    //IntroScreen()
}