package com.ssafy.hifes.ui.group.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.hifes.ui.iconpack.MyIconPack
import com.ssafy.hifes.ui.iconpack.myiconpack.Search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchGroup() {
    var text by remember { mutableStateOf("") }

    val borderStroke = BorderStroke(1.dp, Color.Gray)

    Row(verticalAlignment = Alignment.CenterVertically) {

        BasicTextField(
            value = text,
            onValueChange = { newText ->
                // 입력 값이 변경되면 상태를 업데이트
                text = newText
            },

            textStyle = LocalTextStyle.current.copy(fontSize = 24.sp), // 원하는 크기로 설정
            modifier = Modifier.border(borderStroke, RoundedCornerShape(32.dp)).weight(1f).padding(vertical = 4.dp, horizontal = 8.dp)
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = MyIconPack.Search,
                contentDescription = "Serach",
                tint = Color.Red,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Preview
@Composable
fun SearchPrev() {
    Column(Modifier.padding(horizontal = 8.dp)) {
        SearchGroup()
    }
}