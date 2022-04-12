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
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
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

    private lateinit var textBox: EditText
    private lateinit var button: Button
    private lateinit var infoLayout: ViewGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var nameText: TextView
    private lateinit var createdAtText: TextView
    private lateinit var favouritesCountText: TextView
    private lateinit var followersCountText: TextView
    private lateinit var friendsCountText: TextView
    private lateinit var statusesCountText: TextView
    private lateinit var imageView: ImageView
    private lateinit var engOrUniGroup: RadioGroup
    private lateinit var typeRadioGroup: RadioGroup
    private lateinit var capRadioButton: RadioButton
    private lateinit var displayScoresRadioButton: RadioButton
    private lateinit var rawScoresRadioButton: RadioButton
    private lateinit var englishRadioButton: RadioButton
    private lateinit var universalRadioButton: RadioButton
    private lateinit var astroturfTextView: TextView
    private lateinit var fakeFollowerTextView: TextView
    private lateinit var financialTextView: TextView
    private lateinit var otherTextView: TextView
    private lateinit var overallTextView: TextView
    private lateinit var selfDeclaredTextView: TextView
    private lateinit var spammerTextView: TextView
    private lateinit var notFoundTextView: TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mainViewModel = MainViewModel()
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initWidgets(binding)

        val statObserver: Observer<BotmeterResult> = Observer {
            progressBar.visibility = View.INVISIBLE
            button.isEnabled = true
            textBox.isEnabled = true
            it.user?.userData?.let { userData ->
                notFoundTextView.visibility = View.INVISIBLE
                infoLayout.visibility = View.VISIBLE
                fillUserData(userData)
                fillImageView(imageView, URL(userData.profileImageURL))
            } ?: run {
                infoLayout.visibility = View.INVISIBLE
                notFoundTextView.visibility = View.VISIBLE
                // If is null.
            }
            radioButtonUpdate(it)
        }

        mainViewModel.resultFormState.observe(this, statObserver)
        textBox.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(arg0: Editable) {
                    button.isEnabled = arg0.isNotEmpty() && arg0.length > 5
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })

        button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            notFoundTextView.visibility = View.INVISIBLE
            button.isEnabled = false
            textBox.isEnabled = false
            mainViewModel.updateForName(textBox.text.toString())
        }

        engOrUniGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }
        typeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            mainViewModel.resultFormState.value?.let { result -> radioButtonUpdate(result) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillUserData(userData: UserData) {
        nameText.text = "Name: " + userData.name.toString()
        createdAtText.text = "Created at: " + userData.createdAt.toString()
        favouritesCountText.text = "Favourites: " + userData.favouritesCount.toString()
        followersCountText.text = "Followers: " + userData.followersCount.toString()
        friendsCountText.text = "Friends: " + userData.friendsCount.toString()
        statusesCountText.text = "Statuses: " + userData.statusesCount.toString()
    }

    private fun radioButtonUpdate(result: BotmeterResult) {
        if (displayScoresRadioButton.isChecked) {
            engOrUniGroup.visibility = View.VISIBLE
            if (universalRadioButton.isChecked) {
                result.displayScores?.universal?.let { score -> fillStats(score) }
            } else {
                result.displayScores?.english?.let { score -> fillStats(score) }
            }
        } else if (rawScoresRadioButton.isChecked) {
            engOrUniGroup.visibility = View.VISIBLE
            if (universalRadioButton.isChecked) {
                result.rawScores?.universal?.let { score -> fillStats(score) }
            } else {
                result.rawScores?.english?.let { score -> fillStats(score) }
            }
        } else { //capsScores
            engOrUniGroup.visibility = View.INVISIBLE
            result.cap?.let { score -> fillCapStats(score) }
        }
    }

    private fun initWidgets(binding: ActivityMainBinding) {
        textBox = binding.editTextTextPersonName
        button = binding.button
        infoLayout = binding.infoLayout
        progressBar = binding.progressBar
        nameText = binding.nameText
        createdAtText = binding.createdAtText
        favouritesCountText = binding.favouritesCountText
        followersCountText = binding.followersCountText
        friendsCountText = binding.friendsCountText
        statusesCountText = binding.statusesCountText
        imageView = binding.imageView
        engOrUniGroup = binding.engOrUniGroup
        typeRadioGroup = binding.typeRadioGroup
        capRadioButton = binding.capRadioButton
        displayScoresRadioButton = binding.displayscoresRadioButton
        rawScoresRadioButton = binding.rawscoresRadioButton
        englishRadioButton = binding.englishRadioButton
        universalRadioButton = binding.universalRadioButton
        astroturfTextView = binding.astroturfTextView
        fakeFollowerTextView = binding.fakeFollowerTextView
        financialTextView = binding.financialTextView
        otherTextView = binding.otherTextView
        overallTextView = binding.overallTextView
        selfDeclaredTextView = binding.selfDeclaredTextView
        spammerTextView = binding.spammerTextView
        notFoundTextView = binding.notFoundTextView
    }

    @SuppressLint("SetTextI18n")
    private fun fillCapStats(score: Cap) {
        astroturfTextView.text = "Universal: " + score.universal
        fakeFollowerTextView.text = "English: " + score.english
        financialTextView.visibility = View.INVISIBLE
        otherTextView.visibility = View.INVISIBLE
        overallTextView.visibility = View.INVISIBLE
        selfDeclaredTextView.visibility = View.INVISIBLE
        spammerTextView.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun fillStats(score: ScoreValues) {
        financialTextView.visibility = View.VISIBLE
        otherTextView.visibility = View.VISIBLE
        overallTextView.visibility = View.VISIBLE
        selfDeclaredTextView.visibility = View.VISIBLE
        spammerTextView.visibility = View.VISIBLE
        astroturfTextView.text = "astroturf: " + score.astroturf
        fakeFollowerTextView.text = "fakeFollower: " + score.fakeFollower
        financialTextView.text = "financial: " + score.financial
        otherTextView.text = "other: " + score.other
        overallTextView.text = "overall: " + score.overall
        selfDeclaredTextView.text = "selfDeclared: " + score.selfDeclared
        spammerTextView.text = "spammer: " + score.spammer

    }

    private fun fillImageView(imageView: ImageView, url: URL) {
        imageView.visibility = View.INVISIBLE
        val handler = Handler(Looper.getMainLooper())
        Executors.newSingleThreadExecutor().execute {
            try {
                val image = BitmapFactory.decodeStream(url.openStream())
                handler.post {
                    imageView.setImageBitmap(
                        image
                    )
                    imageView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(javaClass.canonicalName, "Exception while setting image", e)
                e.printStackTrace()
            }
        }
    }
}