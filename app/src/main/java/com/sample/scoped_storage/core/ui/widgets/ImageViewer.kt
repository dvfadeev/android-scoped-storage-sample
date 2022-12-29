package com.sample.scoped_storage.core.ui.widgets

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.sample.scoped_storage.core.ui.theme.additionalColors
import com.sample.scoped_storage.core.utils.FileTypes

@Composable
fun ImageViewer(
    uri: Uri?,
    type: String,
    size: Dp,
    modifier: Modifier = Modifier,
    isCropEnabled: Boolean = true
) {
    if (type.startsWith(FileTypes.MIME_TYPE_IMAGE_ALL) || type.startsWith(FileTypes.MIME_TYPE_VIDEO_ALL)) {
        val sizeModifier = if (isCropEnabled) {
            Modifier.size(size)
        } else {
            Modifier
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .decoderFactory(VideoFrameDecoder.Factory())
                .build(),
            contentDescription = null,
            contentScale = if (isCropEnabled) {
                ContentScale.Crop
            } else ContentScale.Fit,
            modifier = modifier
                .then(sizeModifier)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.additionalColors.lightText, RoundedCornerShape(8.dp)),
        )
    }
}