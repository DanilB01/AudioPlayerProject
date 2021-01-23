package ru.tsu.audioplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.music)

        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 500)
        }

        val duration = mediaPlayer.duration
        val sDuration = convertFormat(duration)
        playerDuration.text = sDuration

        playImage.setOnClickListener {
            playImage.visibility = View.GONE
            pauseImage.visibility = View.VISIBLE
            mediaPlayer.start()
            seekBar.max = mediaPlayer.duration

            handler.postDelayed(runnable, 0)
        }

        pauseImage.setOnClickListener {
            playImage.visibility = View.VISIBLE
            pauseImage.visibility = View.GONE
            mediaPlayer.pause()

            handler.removeCallbacks(runnable)
        }

        nextImage.setOnClickListener {
            var currentPosition = mediaPlayer.currentPosition
            var curDuration = mediaPlayer.duration

            if(mediaPlayer.isPlaying && curDuration != currentPosition) {
                currentPosition += 5000
                playerPosition.text = convertFormat(currentPosition)
                mediaPlayer.seekTo(currentPosition)
            }
        }

        backImage.setOnClickListener {
            var currentPosition = mediaPlayer.currentPosition
            if(mediaPlayer.isPlaying && currentPosition > 5000) {
                currentPosition -= 5000
                playerPosition.text = convertFormat(currentPosition)
                mediaPlayer.seekTo(currentPosition)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }

                playerPosition.text = convertFormat(mediaPlayer.currentPosition)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        mediaPlayer.setOnCompletionListener {
            pauseImage.visibility = View.GONE
            playImage.visibility = View.VISIBLE
            mediaPlayer.seekTo(0)
        }
    }

    fun convertFormat(duration: Int): String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
        )
    }
}