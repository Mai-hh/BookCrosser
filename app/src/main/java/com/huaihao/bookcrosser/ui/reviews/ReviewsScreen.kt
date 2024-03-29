package com.huaihao.bookcrosser.ui.reviews

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(uiState: ReviewsUiState, onEvent: (ReviewsEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reviews", style = MaterialTheme.typography.headlineLarge) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add A Review")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(count = uiState.reviews.size) { index: Int ->
                ReviewCard(
                    review = uiState.reviews[index],
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddReviewScreen() {
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    AddReviewContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
}

@Composable
fun AddReviewContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}

@Composable
fun NoPermissionScreen(
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Permission request")
        },
        text = {
            Text("This permission is important for this app. Please grant the permission.")
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun CameraScreen() {

}

@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: Review) {
    ElevatedCard(modifier = modifier) {
        ConstraintLayout {
            val (title, content, preview) = createRefs()
            Text(
                text = "Title",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                })
            Text(
                text = stringResource(id = R.string.content_test),
                modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(title.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ReviewCardPreview() {
    BookCrosserTheme {
        ReviewCard(modifier = Modifier, Review(title = "TestTile", content = ""))
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenPreview() {
    MaterialTheme {
        ReviewsScreen(
            uiState = ReviewsUiState(
                listOf(
                    Review(title = "A1", content = stringResource(id = R.string.content_test)),
                    Review(title = "A1", content = stringResource(id = R.string.content_test))
                )
            )
        ) {}
    }
}