# KeepingTrack - COS30017 Assignment 3
## Swinburne University of Technology
## COS30017 - Software Development for Mobile Devices
## Student Name: Cong Duc Danh (Dan) Ngo
## Student ID: 104186810

## App Overview
KeepingTrack is a mobile CRUD application designed for managing movie rentals, allowing users to easily add, view, and manage their movie collection. The app follows the assignment specifications and features an intuitive interface that enhances user experience.

## App Interface and Functionality
### MainActivity and MovieListFragment
The app launches with MainActivity, which opens the MovieListFragment. This fragment uses a RecyclerView to display a list of movie objects fetched from Firebase’s Realtime Database. Each movie has the following attributes: ID, name, published year, genre, director, rating, and notes. The initial list contains 18 movies, each with delete and edit buttons for further actions. A BottomNavigation bar at the bottom facilitates navigation within the app.

### Editing Movies
When a user clicks on a movie to edit, MainActivity adds MovieDetailFragment to the fragment stack, displaying the selected movie's details. Options to delete and edit the movie are available. Upon saving changes, the SharedViewModel updates the Firebase database, and LiveData notifies the MovieListFragment to refresh its UI. A Snackbar appears to confirm the success of the update, with an option to undo changes.

### Deleting Movies
Each delete action, whether in MovieListFragment or MovieDetailFragment, triggers a confirmation dialog. Once confirmed, the movie is deleted from the database and cannot be undone. The SharedViewModel manages the update of the database and the RecyclerView through LiveData observation.

### Adding Movies
Clicking the “Add Movie” button in the BottomNavigation opens AddMovieFragment, where users can add a new movie using the SharedViewModel. The genre selection utilises a Spinner widget to limit options to valid genres while saving screen space. The app automatically assigns a new ID to the added movie by incrementing the last item's ID. A button labeled “1” is used for investigation purposes, indicating whether a movie is added using the Main Thread or a Background Thread.

### SharedViewModel
The SharedViewModel centralises CRUD actions across the three fragments, enabling communication with the UI through LiveData and updates to the Firebase Database. It also maintains the current fragment's state during configuration changes, ensuring that the navigation stack remains intact. The app supports landscape layouts for better usability.


## Investigation
- As part of this assignment, I conducted an investigation comparing two variants of the add movie functionality. Details and a demonstration video can be found on Canvas submission and YouTube: https://www.youtube.com/watch?v=VXiioYwKgfY. 
- Two functions, fetchLastMovieId() and addMovie(), were initially designed to run on the Main Thread. I created variants that utilize Background Threads (using Coroutine and Dispatchers) for these functions.
- Using Profiler, I compared CPU and memory performance for each variant:
+ The Main Thread exhibited better performance, which was unexpected. This could be due to the small scale of the application, making it difficult to observe performance differences. Since the app primarily operates on the Main Thread, switching to Background Thread might have slightly impacted performance.
- In conclusion, I recommend executing fetchLastMovieId() on a Background Thread, as immediate results are not required when opening AddMovieFragment. The addMovie() function should run on the Main Thread since it is a high-priority task that updates the UI.


## Key Knowledge Gaps and Processes
1. ListFragment with RecyclerView
Inspired by the MoreImages app demonstrated in Lecture 7, MovieListFragment integrates with MovieListRecyclerViewAdapter, providing an interactive list of movies displayed with low memory usage.

2. Multi-Fragment with Shared ViewModel
The app contains three fragments, each implementing a shared ViewModel to perform CRUD actions, save states, and update the UI through LiveData.

3. Concurrency
The Movie data class is Serializable, initially intended for concurrency, but this feature was not implemented due to time constraints.
However, the addMovie functionality runs in a Background Thread, utilizing Kotlin coroutines to enhance performance and avoid ANR (Application Not Responding) issues.

4. Use of UI Components
Various UI components, including ImageButton, BottomNavigation, RatingBar, and Spinner, were employed to improve the user experience.
In AddMovieFragment, a Spinner serves as a dropdown for genre selection, conserving screen space compared to other multi-choice widgets.
In MovieDetailFragment, the Edit button toggles to indicate the user has opened edit mode and can save changes.

5. Use of Widgets for User Interactions
To enhance user interactivity, dialogs, snackbars, and toasts were utilised:
Dialog for confirming delete actions.
Snackbar to announce update actions with an option to undo.
Toast messages to confirm successful actions such as deletion, undoing changes, and movie creation.
