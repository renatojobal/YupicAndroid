package com.yupic.yupic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yupic.yupic.model.Project

@Composable
fun OffsetScreen() {
    val list = listOf(
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        )
    )

    LazyColumn{
        items(items = list, itemContent = {item ->

            ProjectCard(project = item)

        })


    }
}

@Preview(showBackground = true)
@Composable
fun OffsetScreenPreview() {
    val list = listOf(
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        ),
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        )
    )

    LazyColumn{
        items(items = list, itemContent = {item -> 
        
            ProjectCard(project = item)    
            
        })


    }
}

@Composable
fun ProjectCard(
    project: Project
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(8.dp)
            .background(color = MaterialTheme.colors.primary, shape = MaterialTheme.shapes.small)
            
    ) {
        
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            
            Column() {
                Text(text = project.title)
                Text(text = project.description)
            }
            
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.onSecondary,
                    shape = MaterialTheme.shapes.small
                )) {
                
            }
            
        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    ProjectCard(
        Project(
            title= "TEAMTREES",
            "Team Trees, stylized as #TEAMTREES, is a collaborative fundraiser that raised 20 million U.S. dollars before 2020 to plant 20 million trees. The initiative was started by American YouTubers MrBeast and Mark Rober"
        )
    )
}