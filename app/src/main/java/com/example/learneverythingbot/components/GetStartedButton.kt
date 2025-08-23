package com.example.learneverythingbot.components

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.learneverythingbot.R
import com.example.learneverythingbot.viewmodel.ChatActivity

@Composable
fun GetStartedButton(
    onClickNavigate: () -> Unit
) {
    val context = LocalContext.current


    Button(
        onClick = onClickNavigate,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ){
        Text(
            text = stringResource(id = R.string.comenzar),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GetStartedButtonPreview() {
    GetStartedButton(
        onClickNavigate = { },

    )
}