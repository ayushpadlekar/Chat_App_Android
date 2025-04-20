package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.data.model.User
import com.ayushxp.chatapp.ui.theme.ChatAppTheme
import com.ayushxp.chatapp.viewmodel.ChatViewModel

@Composable
fun SearchUserScreen(navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    // ChatViewModel instance
    val viewModel: ChatViewModel = viewModel()

    var input by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchResults by viewModel.searchResults.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        SearchUser(
            searchQuery = input,
            onSearchQueryChange = { input = it },
            isSearchBarActive = active,
            onIsSearchBarActiveChange = { active = it },
            onSearch = {
                if (input.isNotBlank()) {
                    viewModel.searchUser(input.trim())
                }
            },
            searchResults = searchResults,
            onUserClick = { user ->
                viewModel.getOrCreateChat(user.uid) { chatId ->
                    if (chatId != null) {
                        navController.navigate("chat/$chatId/${user.username}") {
                            popUpTo("search") { inclusive = true }
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUser(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchBarActive: Boolean,
    onIsSearchBarActiveChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    searchResults: List<User>,
    onUserClick: (User) -> Unit
) {
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding( if(isSearchBarActive) 0.dp else 16.dp),
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {
            onSearch()
            onIsSearchBarActiveChange(true)
        },
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
                    onClick = {
                        onSearch()
                        onIsSearchBarActiveChange(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        tint = primaryCol,
                        contentDescription = "Search"
                    )
                }
            }
        }
    ) {
        // Content to display when the search bar is active
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            if (searchResults.isEmpty()) {
                Text("No users found.", color = Color.Gray, modifier = Modifier.padding(start = 15.dp))
            } else {
                searchResults.forEach { user ->
                    UserResultItem(user = user, onClick = { onUserClick(user) })
                }
            }
        }
    }
}

@Composable
fun UserResultItem(user: User, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 15.dp)
    ) {
        Text(text = user.username, color = Color.Black, fontSize = 20.sp)
        Text(text = user.email, color = Color.Gray)
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