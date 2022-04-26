package pl.edu.wat.twiapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.edu.wat.twiapp.botmeter.BotmeterResult
import pl.edu.wat.twiapp.botmeter.Cap
import pl.edu.wat.twiapp.botmeter.ScoreValues
import pl.edu.wat.twiapp.botmeter.UserData
import pl.edu.wat.twiapp.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var defaultPhoto: Bitmap

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

        mainViewModel.resultFormState.observe(
            this
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
                //if is null
                binding.infoLayout.visibility = View.INVISIBLE
                binding.notFoundTextView.visibility = View.VISIBLE
                when {
                    it.error == "JSON Error" -> {
                        binding.notFoundTextView.text = "User not found"
                    }
                    it.error?.isNotEmpty() == true -> {
                        binding.notFoundTextView.text = it.message
                    }
                    else -> {
                        binding.notFoundTextView.text = "Error"
                    }
                }
            }
            radioButtonUpdate(it)
        }



        binding.editTextTextPersonName.addTextChangedListener(getEditTextWatcher())

        binding.button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                updateDataForName()
            }
        }

        binding.engOrUniGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }
        binding.typeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }

        defaultPhoto = BitmapFactory.decodeResource(resources, R.drawable.default_profile_normal)
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

    private suspend fun updateDataForName() {
        binding.progressBar.visibility = View.VISIBLE
        binding.notFoundTextView.visibility = View.INVISIBLE
        binding.button.isEnabled = false
        binding.editTextTextPersonName.isEnabled = false
        mainViewModel.updateForName(binding.editTextTextPersonName.text.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun fillUserData(userData: UserData) {
        binding.baseInfoText.text = ""
        binding.baseInfoText.append("Name: " + userData.name.toString() + "\n")
        binding.baseInfoText.append("Created at: " + userData.createdAt.toString() + "\n")
        binding.baseInfoText.append("Favourites: " + userData.favouritesCount.toString() + "\n")
        binding.baseInfoText.append("Followers: " + userData.followersCount.toString() + "\n")
        binding.baseInfoText.append("Friends: " + userData.friendsCount.toString() + "\n")
        binding.baseInfoText.append("Statuses: " + userData.statusesCount.toString() + "\n")
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
                result.rawScores?.universal?.let { score -> fillStats(score, raw = true) }
            } else {
                result.rawScores?.english?.let { score -> fillStats(score, raw = true) }
            }
        } else { //capsScores
            binding.engOrUniGroup.visibility = View.INVISIBLE
            result.cap?.let { score -> fillCapStats(score) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillCapStats(score: Cap, raw: Boolean = false) {
        fillDetailText(
            listOf(
                Triple("Universal", score.universal, raw),
                Triple("English", score.english, raw)
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun fillStats(score: ScoreValues, raw: Boolean = false) {
        fillDetailText(
            listOf(
                Triple("Astroturf", score.astroturf, raw),
                Triple("Fake follower", score.fakeFollower, raw),
                Triple("Financial", score.financial, raw),
                Triple("Other", score.other, raw),
                Triple("Self declared", score.selfDeclared, raw),
                Triple("Spammer", score.spammer, raw),
                Triple("Overall", score.overall, raw),
            )
        )

    }

    private fun fillDetailText(list: List<Triple<String, Double?, Boolean>>) {
        binding.detailText.text = ""
        list.forEach { (key, value, isRaw) ->
            binding.detailText.append("$key: ")
            binding.detailText.append(value?.let { getColorEmoji(if (isRaw) (value * 5.0) else value) })
            binding.detailText.append(" ${value}\n")
        }
    }

    private fun getColorEmoji(value: Double): CharSequence {
        return when {
            value > 3.75 -> "🔴"
            value > 2.5 -> "🟠"
            value > 1.25 -> "🟡"
            else -> "🟢"
        }
    }

    private fun fillImageView(url: URL) {
        binding.imageView.setImageBitmap(
            defaultPhoto
        )
        GlobalScope.launch(Dispatchers.Main) {
            mainViewModel.getPhoto(url)?.let { photo ->
                binding.imageView.setImageBitmap(photo)
            } ?: run {
                binding.imageView.setImageBitmap(
                    defaultPhoto
                )
            }
        }

    }
}