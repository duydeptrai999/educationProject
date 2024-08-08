package com.example.asm2f.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.asm2f.R
import com.example.asm2f.viewmodel.GetGenresViewModel
import com.example.asm2f.viewmodel.MovieDetailViewModel
import com.example.asm2f.data.Resource
import com.example.asm2f.data.Status
import kotlinx.coroutines.launch

class MovieUIDetailCompose {

    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
    @Composable
    fun MovieDetailScreen(
        viewModel: MovieDetailViewModel = viewModel(),
        movieId: Int,
        getGenresViewModel: GetGenresViewModel = viewModel()
    ) {
        val movieDetail by viewModel.movieDetail.observeAsState()
        val expanded by viewModel.expanded
        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val cast by viewModel.cast.observeAsState(emptyList())
        val moviesOfGenre by getGenresViewModel.getGenresOfMovie.observeAsState(Resource.success(emptyList()))

        LaunchedEffect(movieId) {
            viewModel.fetchMovieDetail(movieId)
            viewModel.fetchMovieCredits(movieId)
            getGenresViewModel.getGenres()
        }

        val genres by getGenresViewModel.getGenres.observeAsState()
        LaunchedEffect(movieDetail, genres) {

            movieDetail?.genres?.takeIf { it.isNotEmpty() }?.let { genre ->


                val genreIdString = genre.joinToString(","){it.id}
                Log.d("MovieUIDetailCompose", "Selected genreIds: $genreIdString")
                getGenresViewModel.getGenresOfMoive(genreIdString)
            } ?: Log.d("MovieUIDetailCompose", "No genreIds found in movieDetail")


        }


        LaunchedEffect(scrollState.firstVisibleItemIndex, scrollState.firstVisibleItemScrollOffset) {
            if (scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 100) {
                viewModel.expanded.value = false
            }
        }

        LaunchedEffect(expanded) {
            if (expanded) {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            movieDetail?.let { movie ->
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            GlideImage(
                                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                contentDescription = "Movie Poster",
                                modifier = Modifier
                                    .size(200.dp)
                                    .weight(1f),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(
                                    text = movie.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = movie.genres.joinToString(",") { it.name },
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    val filterStar = ((movie.voteAverage) / 2).toInt()
                                    for (i in 1..5) {
                                        Icon(
                                            painter = painterResource(id = if (i <= filterStar) R.drawable.baseline_star else R.drawable.baseline_star_border),
                                            contentDescription = "Star",
                                            tint = Color.Yellow,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Text(
                                        text = " ${movie.voteAverage} / 10",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Language: English",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${movie.releaseDate} (USA)",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (expanded) {
                            Text(
                                text = movie.overview,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            Text(
                                text = movie.overview,
                                fontSize = 16.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (expanded) "Show Less" else "View All",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                viewModel.expanded.value = !viewModel.expanded.value
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cast",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CastUIDetailCompose().CastGrid(cast)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Similar Movies",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            moviesOfGenre?.let { movies ->
                when (movies.status) {
                    Status.SUCCESS -> {
                        val moviesList = movies.data ?: emptyList()
                        items(moviesList.size) { index ->
                            val movie = moviesList[index]
                            MovieItem1(
                                imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                title = movie.title,
                                rating = (movie.voteAverage / 2).toInt()
                            )
                        }
                    }
                    Status.ERROR -> {
                        item {
                            Text(text = "Error loading similar movies", color = Color.Red)
                        }
                    }
                    else -> {
                        item {
                            Text(text = "Loading similar movies...")
                        }
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun MovieItem(imageUrl: String, title: String, genres: String, rating: Int) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = genres,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Icon(
                            painter = painterResource(id = if (i <= rating) R.drawable.baseline_star else R.drawable.baseline_star_border),
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun MovieItem1(imageUrl: String, title: String, rating: Int) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Icon(
                            painter = painterResource(id = if (i <= rating) R.drawable.baseline_star else R.drawable.baseline_star_border),
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
