package com.huaihao.bookcrosser.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val (title, content) = createRefs()

                Box(contentAlignment = Alignment.BottomStart, modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(content.top, margin = 16.dp)
                    }) {
                    Text(
                        text = stringResource(id = R.string.app_name_space),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }) {

                    OutlinedTextField(
                        value = "mohuaihao@163.com",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = "momo0918",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = "momo0918",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = "momo0918",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.login))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    BookCrosserTheme {
        SignUpScreen()
    }
}