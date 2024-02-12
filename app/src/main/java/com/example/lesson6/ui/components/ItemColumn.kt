package com.example.lesson6.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lesson6.R
import com.example.lesson6.data.model.HealthModel
import timber.log.Timber

private var lastDate: String = ""

/*@Composable
fun ItemChoose(healthModel: List<HealthModel>) {
    if (lastDate != healthModel.createdAt) {
        lastDate = healthModel.createdAt
        ItemDateColumn(healthModel)
        ItemColumn(healthModel)
    } else
        ItemColumn(healthModel)
}*/

fun healthColor(upperPressure: Int, lowerPressure: Int): Float {
    val upperColorInt = if (upperPressure > 180) 0
    else if (upperPressure < 120) 120
    else 120 - (upperPressure - 120) * 2
    Timber.tag("!!!").d("upperColorInt = $upperColorInt      upperPressure = $upperPressure")
    val lowerColorInt = (if (lowerPressure > 120) 0
    else if (lowerPressure < 80) 120
    else 120 - (lowerPressure - 80) * 4)
    Timber
        .tag("!!!")
        .d("lowerColorInt = $lowerColorInt      lowerPressure = $lowerPressure")
    val colorFloat = if (upperColorInt < lowerColorInt) upperColorInt
    else lowerColorInt
    Timber.tag("!!!").d("colorFloat = ${colorFloat}")
    return colorFloat.toFloat()


}

@Composable
fun ItemColumn(healthModel: HealthModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .pointerInput(Unit) {
                Timber
                    .tag("!!!")
                    .d("Long Press!!!: %s", Unit)
            },
        border = BorderStroke(0.dp, Color.DarkGray),
        shape = RoundedCornerShape(0.dp)
    ) {
        val gradientBrush = Brush.horizontalGradient(
            colors = listOf(
                Color.White,
                Color.hsl(
                    healthColor(
                        healthModel.upperPressure.toInt(),
                        healthModel.lowerPressure.toInt()
                    ),
                    1f,
                    0.6f,
                    0.6f
                ),
                Color.White
            ),
            startX = 0f,
            endX = Float.POSITIVE_INFINITY
        )
        Row(
            modifier = Modifier
                .background(brush = gradientBrush)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.33f)
                    .padding(start = 20.dp),
                contentAlignment = Alignment.CenterStart

            ) {
                Text(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    text = healthModel.createdAt.toString()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 24.sp,
                    text = healthModel.upperPressure.toString()
                )
                Text(
                    fontSize = 24.sp,
                    color = Color.Gray,
                    text = "/"
                )
                Text(
                    fontSize = 24.sp,
                    text = healthModel.lowerPressure.toString()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Image(
                    painter = painterResource(R.drawable.ic_heart_24),
                    contentDescription = "heart",
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 24.sp,
                    color = Color.Gray,
                    text = healthModel.pulse.toString()
                )
            }
        }
    }
}

@Composable
fun ItemDateColumn(healthModel: HealthModel) {
    Spacer(
        modifier = Modifier
            .height(1.dp),
    )
    Card(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .height(28.dp),
        border = BorderStroke(0.dp, Color.Gray),
        shape = CutCornerShape(0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.33f)
                .padding(start = 20.dp),
            contentAlignment = Alignment.CenterStart

        ) {
            Text(
                fontSize = 16.sp,
                color = Color.Gray,
                text = healthModel.createdAt
            )

        }
    }
}