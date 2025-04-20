package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.West
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.ui.theme.ChatAppTheme

@Composable
fun SearchUserScreen(navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    var input by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        SearchUserContent(
            searchQuery = input,
            onSearchQueryChange = { input = it },
            isSearchBarActive = active,
            onIsSearchBarActiveChange = { active = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchBarActive: Boolean,
    onIsSearchBarActiveChange: (Boolean) -> Unit,
) {
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding( if(isSearchBarActive) 0.dp else 16.dp),
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { onIsSearchBarActiveChange(false) },
        active = isSearchBarActive,
        onActiveChange = onIsSearchBarActiveChange,
        placeholder = { Text("Search for a user") },
        shape = SearchBarDefaults.inputFieldShape,
        colors = SearchBarDefaults.colors(containerColor = secondaryCol),
        leadingIcon = {
            if (isSearchBarActive) {
                IconButton(
                    onClick = { onIsSearchBarActiveChange(false) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.West,
                        tint = Color.Black,
                        contentDescription = "Back"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Search,
                    tint = Color.Gray,
                    contentDescription = "Search"
                )
            }
        },
        trailingIcon = {
            if (isSearchBarActive) {
                IconButton(
                    onClick = { onIsSearchBarActiveChange(false) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        tint = primaryCol,
                        contentDescription = "Search"
                    )
                }
            } else {
                null
            }
        },
    ) {
        // Content to display when the search bar is active
        Column {
            Text(text = "Content when active")
        }
    }
}


@Preview
@Composable
fun SearchUserScreenPreview() {
    val navController = rememberNavController()
    ChatAppTheme(darkTheme = false, dynamicColor = false) {
        SearchUserScreen(navController)
    }
}