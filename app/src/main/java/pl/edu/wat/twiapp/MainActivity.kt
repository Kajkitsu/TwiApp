package pl.edu.wat.twiapp

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import pl.edu.wat.twiapp.botmeter.BotmeterResult
import pl.edu.wat.twiapp.botmeter.Cap
import pl.edu.wat.twiapp.botmeter.ScoreValues
import pl.edu.wat.twiapp.botmeter.UserData
import pl.edu.wat.twiapp.databinding.ActivityMainBinding
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mainViewModel = MainViewModel()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainViewModel.resultFormState.observe(this
        ) {
            binding.progressBar.visibility = View.INVISIBLE
            binding.button.isEnabled = true
            binding.editTextTextPersonName.isEnabled = true
            it.user?.userData?.let { userData ->
                binding.notFoundTextView.visibility = View.INVISIBLE
                binding.infoLayout.visibility = View.VISIBLE
                fillUserData(userData)
                fillImageView(URL(userData.profileImageURL))
            } ?: run {
                binding.infoLayout.visibility = View.INVISIBLE
                binding.notFoundTextView.visibility = View.VISIBLE
                // If is null.
            }
            radioButtonUpdate(it)
        }



        binding.editTextTextPersonName.addTextChangedListener(getEditTextWatcher())

        binding.button.setOnClickListener {
            updateDataForName()
        }

        binding.engOrUniGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }
        binding.typeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }
    }

    private fun getEditTextWatcher(): TextWatcher? {
        return object : TextWatcher {
            override fun afterTextChanged(arg0: Editable) {
                binding.button.isEnabled = arg0.isNotEmpty() && arg0.length > 5
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
    }

    private fun updateDataForName(){
        binding.progressBar.visibility = View.VISIBLE
        binding.notFoundTextView.visibility = View.INVISIBLE
        binding.button.isEnabled = false
        binding.editTextTextPersonName.isEnabled = false
        mainViewModel.updateForName(binding.editTextTextPersonName.text.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun fillUserData(userData: UserData) {
        binding.nameText.text = "Name: " + userData.name.toString()
        binding.createdAtText.text = "Created at: " + userData.createdAt.toString()
        binding.favouritesCountText.text = "Favourites: " + userData.favouritesCount.toString()
        binding.followersCountText.text = "Followers: " + userData.followersCount.toString()
        binding.friendsCountText.text = "Friends: " + userData.friendsCount.toString()
        binding.statusesCountText.text = "Statuses: " + userData.statusesCount.toString()
    }

    private fun radioButtonUpdate(result: BotmeterResult) {
        if (binding.displayScoresRadioButton.isChecked) {
            binding.engOrUniGroup.visibility = View.VISIBLE
            if (binding.universalRadioButton.isChecked) {
                result.displayScores?.universal?.let { score -> fillStats(score) }
            } else {
                result.displayScores?.english?.let { score -> fillStats(score) }
            }
        } else if (binding.rawScoresRadioButton.isChecked) {
            binding.engOrUniGroup.visibility = View.VISIBLE
            if (binding.universalRadioButton.isChecked) {
                result.rawScores?.universal?.let { score -> fillStats(score) }
            } else {
                result.rawScores?.english?.let { score -> fillStats(score) }
            }
        } else { //capsScores
            binding.engOrUniGroup.visibility = View.INVISIBLE
            result.cap?.let { score -> fillCapStats(score) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillCapStats(score: Cap) {
        fillDetailText(
            listOf(
                Pair("Universal", score.universal),
                Pair("English", score.english)
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun fillStats(score: ScoreValues) {
        fillDetailText(
            listOf(
                Pair("Astroturf", score.astroturf),
                Pair("Fake follower", score.fakeFollower),
                Pair("Financial", score.financial),
                Pair("Other", score.other),
                Pair("Self declared", score.selfDeclared),
                Pair("Spammer", score.spammer),
                Pair("Overall", score.overall),
            )
        )

    }

    private fun fillDetailText(list: List<Pair<String, Double?>>) {
        binding.detailText.text = ""
        list.forEach { (key, value) ->
            binding.detailText.append("$key: ")
            binding.detailText.append(value?.let { getColorEmoji(it) })
            binding.detailText.append(" $value\n")
        }
    }

    private fun getColorEmoji(value: Double): CharSequence {
        return when {
            value > 3.75 -> {
                "🔴"
            }
            value > 2.5 -> {
                "🟠"
            }
            value > 1.25 -> {
                "🟡"
            }
            else -> {
                "🟢"
            }
        }
    }

    private fun fillImageView(url: URL) {
        binding.imageView.visibility = View.INVISIBLE
        val handler = Handler(Looper.getMainLooper())
        Executors.newSingleThreadExecutor().execute {
            try {
                val image = BitmapFactory.decodeStream(url.openStream())
                handler.post {
                    binding.imageView.setImageBitmap(
                        image
                    )
                    binding.imageView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(javaClass.canonicalName, "Exception while setting image", e)
                e.printStackTrace()
            }
        }
    }
}