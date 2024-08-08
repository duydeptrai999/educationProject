package com.example.asm2f.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.asm2f.data.model.Cast

class CastUIDetailCompose {

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CastGrid(cast: List<Cast>) {
        LazyRow(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(8.dp)
        ) {
//            items(cast) { castMember ->
//                CastMemberItem(castMember)
//            }
            items(cast.chunked(6)) { rowItems ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val startIndex = i * 3
                            val endIndex = minOf(startIndex + 3, rowItems.size)
                            for (j in startIndex until endIndex) {
                                CastMemberItem(rowItems[j])
                            }
                        }
                    }
                }
            }

        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CastMemberItem(castMember: Cast) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500${castMember.profile_path}",
                contentDescription = castMember.name,
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = castMember.name,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}
