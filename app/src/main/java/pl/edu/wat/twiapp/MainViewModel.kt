package pl.edu.wat.twiapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.edu.wat.twiapp.botmeter.BotmeterApi
import pl.edu.wat.twiapp.botmeter.BotmeterResult
import java.net.URL
import java.util.concurrent.Executors

class MainViewModel : ViewModel() {
    private val _resultForm = MutableLiveData<BotmeterResult>()
    val resultFormState: LiveData<BotmeterResult> = _resultForm

    val api = BotmeterApi()

    suspend fun updateForName(screenName: String) {
        return withContext(Dispatchers.IO) {
            val retVal = api.getBootStats(screenName)
            if (retVal.message != null) {
                Log.e(this.javaClass.canonicalName, "Message: " + retVal.message)
            }
            if (retVal.error != null) {
                Log.e(this.javaClass.canonicalName, "Error: " + retVal.error)
            }
            _resultForm.postValue(retVal)
        }

    }
    fun clear() {
        _resultForm.postValue(BotmeterResult())
    }

    suspend fun getPhoto(url: URL): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext BitmapFactory.decodeStream(url.openStream())
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }
}
