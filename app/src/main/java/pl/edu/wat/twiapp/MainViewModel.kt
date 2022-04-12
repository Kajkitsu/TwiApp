package pl.edu.wat.twiapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.edu.wat.twiapp.botmeter.BotmeterApi
import pl.edu.wat.twiapp.botmeter.BotmeterResult

class MainViewModel : ViewModel() {
    private val _resultForm = MutableLiveData<BotmeterResult>()
    val resultFormState: LiveData<BotmeterResult> = _resultForm

    val api = BotmeterApi()

    fun updateForName(screenName: String) {
        val thread = Thread {
            try {
                _resultForm.postValue(api.getBootStats(screenName))
                if (_resultForm.value?.message != null) {
                    Log.e(this.javaClass.canonicalName, _resultForm.value?.message!!)
                }
                if (_resultForm.value?.error != null) {
                    Log.e(this.javaClass.canonicalName, _resultForm.value?.error!!)
                }
                Log.d(this.javaClass.canonicalName, _resultForm.value.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    fun clear() {
        _resultForm.postValue(BotmeterResult())
    }
}
