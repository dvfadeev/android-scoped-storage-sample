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
import com.example.scoped_storage_example.core.utils.FileTypes

@Composable
fun ImageViewer(
    uri: Uri?,
    type: String,
    size: Dp,
    modifier: Modifier = Modifier,
    isCropEnabled: Boolean = true,
) {
    if (type.startsWith(FileTypes.MIME_TYPE_IMAGE_ALL) || type.startsWith(FileTypes.MIME_TYPE_VIDEO_ALL)) {
        val context = LocalContext.current

        val sizeModifier = if (isCropEnabled) {
            Modifier.size(size)
        } else {
            Modifier
        }

        Image(
            modifier = modifier
                .then(sizeModifier)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberImagePainter(
                uri,
                builder = {
                    with(LocalDensity.current) { size(size.roundToPx()) }

                    placeholder(R.color.cardview_dark_background)
                    if (type.startsWith(FileTypes.MIME_TYPE_VIDEO_ALL)) {
                        decoder(VideoFrameDecoder(context))
                        crossfade(true)
                    }
                }
            ),
            contentScale = if (isCropEnabled) {
                ContentScale.Crop
            } else ContentScale.Fit,
            contentDescription = null
        )
    }
}