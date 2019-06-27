package com.example.vinod.moviesapplication.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.adapter.MoviesAdapter
import com.example.vinod.moviesapplication.databinding.ActivityHomeScreenBinding
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.repository.MoviesRepository
import com.example.vinod.moviesapplication.utils.Constants.CHANNEL_ID
import com.example.vinod.moviesapplication.utils.Constants.CHANNEL_NAME
import com.example.vinod.moviesapplication.utils.Constants.COUNT_DOWN_INTERVAL
import com.example.vinod.moviesapplication.utils.Constants.INCREMENT_COUNT
import com.example.vinod.moviesapplication.utils.Constants.INTENT_REQUEST
import com.example.vinod.moviesapplication.utils.Constants.ITEM_POSITION_KEY
import com.example.vinod.moviesapplication.utils.Constants.MAX_DURATION_OF_TEST
import com.example.vinod.moviesapplication.utils.Constants.MOVIES_LIST_KEY
import com.example.vinod.moviesapplication.utils.MyBounceInterpolator
import com.example.vinod.moviesapplication.utils.hideView
import com.example.vinod.moviesapplication.utils.showView

class HomeScreenActivity : AppCompatActivity(), MoviesAdapter.IActivityCommunicator {

  private var mBinder: ActivityHomeScreenBinding? = null
  private var mMoviesAdapter: MoviesAdapter? = null
  private val moviesRepository by lazy { MoviesRepository(this) }
  private var mCountDownTimer: CountDownTimer? = null
  private var mOriginalMoviesList: ArrayList<Movies> = mutableListOf<Movies>() as ArrayList<Movies>
  private var mAnimUpDown: Animation? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinder = DataBindingUtil.setContentView(
      this, R.layout.activity_home_screen
    )
    mBinder?.pbProgress?.visibility = View.VISIBLE
    mBinder?.includeWaitingForData?.clTopLayout?.showView()
    animateImageView()
    moviesRepository.deleteFromDatabase()
    mOriginalMoviesList = moviesRepository.listOfMovies
    mBinder?.pbProgress?.visibility = View.GONE
    startCountDownTimerForData()
  }

  private fun animateImageView() {
    mAnimUpDown = AnimationUtils.loadAnimation(
      this, R.anim.rotate_animation
    )
    val mInterpolator = MyBounceInterpolator(0.2, 20.0)
    mAnimUpDown?.interpolator = mInterpolator
    mBinder?.includeWaitingForData?.imageView?.startAnimation(mAnimUpDown)
  }

  private fun setToolbar() {
    mBinder?.tbToolbar?.run {
      setNavigationOnClickListener {
        onBackPressed()
      }
      title = getString(R.string.txt_movies_list)
      setTitleTextColor(
        ContextCompat.getColor(
          this@HomeScreenActivity, R.color.color_white
        )
      )
    }
  }

  private fun setAdapter(moviesList: List<Movies>?) {
    mBinder?.rvMoviesList?.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
    mMoviesAdapter = moviesList?.let { MoviesAdapter(it, this) }
    mBinder?.rvMoviesList?.adapter = mMoviesAdapter
  }

  private fun startCountDownTimerForData() {
    mCountDownTimer =
        object : CountDownTimer(MAX_DURATION_OF_TEST.toLong(), COUNT_DOWN_INTERVAL.toLong()) {
          override fun onTick(millisUntilFinished: Long) {
            //Implementation isn't required
          }

          override fun onFinish() {
            addDataToDataBase()
          }
        }.start()
  }

  private fun addDataToDataBase() {
    val moviesList = mutableListOf<Movies>()
    if (isListHasData()) {
      moviesList.add(mOriginalMoviesList[INCREMENT_COUNT])
    }
    moviesRepository.insertFacilityModel(moviesList)
    mAnimUpDown?.cancel()
    mBinder?.includeWaitingForData?.clTopLayout?.hideView()
    createNotification(this, INCREMENT_COUNT)
    if (isListHasData()) {
      INCREMENT_COUNT++
    }
    moviesRepository.getMoviesList()?.observe(this, Observer<List<Movies>> { mMoviesList ->
      setAdapter(mMoviesList)
      mMoviesAdapter?.notifyDataSetChanged()
    })
    if (isListHasData()) {
      mCountDownTimer?.start()
    }
  }

  private fun isListHasData(): Boolean {
    return 16 > INCREMENT_COUNT
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_movies, menu)
    // Associate searchable configuration with the SearchView
    val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = menu.findItem(R.id.sort).actionView as SearchView
    searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    searchView.maxWidth = Integer.MAX_VALUE

    // Listening to search query text change
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        // Filter recycler view when query submitted
        mMoviesAdapter?.filter?.filter(query)
        return false
      }

      override fun onQueryTextChange(query: String): Boolean {
        // Filter recycler view when text is changed
        mMoviesAdapter?.filter?.filter(query)
        return false
      }
    })
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    return if (id == R.id.sort) {
      true
    } else super.onOptionsItemSelected(item)

  }

  override fun onItemSelected(movies: Movies, position: Int) {
    val mIntent = Intent(this, MoviesDetailActivityWithWebView::class.java)
    //val mIntent = Intent(this, MoviesDetailActivityWithViewPager::class.java)
    mIntent.putExtra(MOVIES_LIST_KEY, mOriginalMoviesList)
    mIntent.putExtra(ITEM_POSITION_KEY, position)
    startActivity(mIntent)
  }

  private fun createNotification(context: Context, position: Int) {
    context.let {
      createNotificationChannel(it)
      val mIntent = Intent(it, MoviesDetailActivityWithWebView::class.java)
      mIntent.putExtra(MOVIES_LIST_KEY, mOriginalMoviesList)
      mIntent.putExtra(ITEM_POSITION_KEY, position)
      val pendingIntent = PendingIntent.getActivity(
        it, INTENT_REQUEST, mIntent, PendingIntent.FLAG_UPDATE_CURRENT
      )

      val notification = NotificationCompat.Builder(it, CHANNEL_ID).setContentTitle(
        mOriginalMoviesList[position].name + ", " + mOriginalMoviesList[position].year
      ).setContentText(mOriginalMoviesList[position].genre).setSmallIcon(
        R.drawable.movie_app_icon
      ).setContentIntent(pendingIntent)
      notification.setAutoCancel(true)
      notification.priority = NotificationCompat.PRIORITY_MAX
      val notificationManager =
        it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.notify(1, notification.build())
    }
  }

  private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
      // Register the channel with the system
      val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  private fun resetKeys() {
    INCREMENT_COUNT = 0
    mCountDownTimer?.cancel()
    mAnimUpDown?.cancel()
  }

  override fun onBackPressed() {
    super.onBackPressed()
    resetKeys()
  }

  override fun onDestroy() {
    super.onDestroy()
    resetKeys()
  }
}
