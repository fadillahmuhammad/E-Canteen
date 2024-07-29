package com.dicoding.ecanteen.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dicoding.ecanteen.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModel(
    modifier: Modifier = Modifier,
    query: String,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    padding: Int
) {
    SearchBar(
        query = query,
        onQueryChange = onSearch,
        onSearch = {  },
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            onClear()
                        }
                )
            }
        },
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        shape = RoundedCornerShape(20.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
        ),
        modifier = modifier
            .padding(padding.dp)
            .fillMaxWidth()
    ) {
    }
}
