package com.example.scoped_storage_example.core.ui.widgets

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import com.example.scoped_storage_example.R

@Composable
fun ImageViewer(
    uri: Uri?,
    size: Dp,
    type: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Image(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp)),
        painter = rememberImagePainter(
            uri,
            builder = {
                with(LocalDensity.current) { size(size.roundToPx()) }
                placeholder(R.color.cardview_dark_background)
                if (type.startsWith("video")) {
                    decoder(VideoFrameDecoder(context))
                    crossfade(true)
                }
            }
        ),
        contentScale = ContentScale.Fit,
        contentDescription = null
    )
}