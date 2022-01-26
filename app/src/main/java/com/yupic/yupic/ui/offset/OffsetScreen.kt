package com.yupic.yupic.ui.offset


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yupic.yupic.model.Project
import com.yupic.yupic.R
import com.yupic.yupic.SharedViewModel
import com.yupic.yupic.ui.theme.YupicTheme

@ExperimentalMaterialApi
@Composable
fun OffsetScreen(sharedViewModel: SharedViewModel) {
    val list = sharedViewModel.getProjectsListLiveData().value

    LazyColumn{
        if (list != null) {
            items(items = list.toList(), itemContent = {item ->

                ProjectCard(project = item)

            })
        }


    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun OffsetScreenPreview() {

    val list = listOf(
        Project(
            name= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            name= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            name= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            name= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        )
    )
    YupicTheme {
        LazyColumn {
            items(items = list, itemContent = { item ->

                ProjectCard(project = item)

            })


        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ProjectCard(
    project: Project
) {
    Card(onClick = { /*TODO*/ },
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(120.dp)
        ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(11f)
                    .padding(10.dp)
            ) {
                Text(text = project.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = project.description, maxLines = 4, overflow = TextOverflow.Ellipsis)
            }
            Box(modifier = Modifier
                .weight(6f)
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            Color(0xFF141E32)
                        ),
                        startX = 0.0f,
                        endX = 60f
                    ),
                    shape = MaterialTheme.shapes.small
                )

            ) {
                Image (
                    painter = painterResource(R.drawable.team_trees_example),
                    "team trees icon",
                    modifier = Modifier
                        .padding(start=30.dp)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


        }

    }


}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    YupicTheme {
        ProjectCard(
            Project(
                name = "TEAMTREES",
                "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
            )
        )
    }
}