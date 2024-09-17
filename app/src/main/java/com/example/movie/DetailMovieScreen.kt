package com.example.movie

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    title: String
) {
    val context = navController.context
    val resources = context.resources
    val titles = resources.getStringArray(R.array.data_title)
    val movieIndex = titles.indexOf(title)

    val selectedTitle = resources.getStringArray(R.array.data_title)[movieIndex]
    val selectedImage = resources.getStringArray(R.array.data_image2)[movieIndex]
    val selectedDescription = resources.getStringArray(R.array.data_description)[movieIndex]
    val selectedReleaseDate = resources.getStringArray(R.array.data_release)[movieIndex]
    val selectedUrl = resources.getStringArray(R.array.data_url)[movieIndex]

    val github = resources.getString(R.string.owner_github)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .padding(bottom = 16.dp)
        ) {
            GlideImage(
                model = selectedImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        MovieElement("Judul", selectedTitle)
        MovieElement("Deskripsi", selectedDescription)
        MovieElement("Tanggal Rilis", selectedReleaseDate)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Cek film ini di: $selectedUrl \nDan jangan lupa follow akun github saya di ${github}")
                    type = "text/plain"
                }
                ContextCompat.startActivity(context, Intent.createChooser(shareIntent, "Bagikan film ini"), null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )
            Text(text = "Share", fontSize = 18.sp)
        }
    }
}

@Composable
fun MovieElement(title: String, value: String) {
    Column {
        Text(
            text = "$title Film: ",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}