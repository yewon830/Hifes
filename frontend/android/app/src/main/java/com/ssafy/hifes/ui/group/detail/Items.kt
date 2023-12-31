package com.ssafy.hifes.ui.group.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.hifes.ui.theme.pretendardFamily

// https://ichef.bbci.co.uk/news/640/cpsprodpb/E172/production/_126241775_getty_cats.png

data class User(
    val url: String,
    val name: String
) {
}

@Composable
fun GroupMember(user: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = user.url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = user.name, fontFamily = pretendardFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun GroupMemberRow(groupMember: List<User>) {
    Column {
        Text(text = "멤버", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(groupMember) {item ->
                GroupMember(user = item)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}


@Preview
@Composable
fun GroupMemeberPrev() {
    val user = User(
        url = "https://fastly.picsum.photos/id/10/2500/1667.jpg?hmac=J04WWC_ebchx3WwzbM-Z4_KC_LeLBWr5LZMaAkWkF68",
        name = "L"
    )
//    GroupMember(user)
    GroupMemberRow(groupMember = listOf(user, user, user, user, user, user))
}