package com.example.note.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.note.R
import com.example.note.ui.adapter.ImageAdapter
import com.example.note.utils.RetrofitUtils.getImageUrl

@BindingAdapter("image_from_url", "is_circle", requireAll = false)
fun imageFromUrl(imageView: ImageView, url: String?, isCircle: Boolean?) {
    if (url == null) {
        imageView.setImageResource(R.drawable.ic_empty)
    } else {
        imageView.load(getImageUrl(url)) {
            isCircle?.let {
                if (it) {
                    transformations(CircleCropTransformation())
                }
                scale(Scale.FILL)
            }
        }
    }
}

@BindingAdapter("image_from_url_or_uri")
fun imageFromUrlOrUri(imageView: ImageView, image: ImageAdapter.ImageType<*>?) {
    image ?: return
    when (image) {
        is ImageAdapter.ImageType.ImageUrl -> {
            imageFromUrl(imageView, image.image, false)
        }
        is ImageAdapter.ImageType.ImageBitMap ->{
            imageView.setImageBitmap(image.image)
        }
    }
}