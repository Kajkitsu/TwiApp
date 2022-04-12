package pl.edu.wat.twiapp.botmeter

import pl.edu.wat.twiapp.utils.oauthinterceptor.OauthKeys

class Keys {
    companion object {
        val TwitterApiBearer =
            "AAAAAAAAAAAAAAAAAAAAABfzIwEAAAAA4zFDXr1At%2BzCokQbRz1rVGTF%2BBw%3DFFIKfUYS7wXEffukbEcqkNGbK9xaT7tavv7dtPzswbQjIW1GBm"
        val RapidApi = "dc38eb8c81msh992e22d473a511ap1c0b4cjsn234b1740e5b8"

        //Callback
        fun getOauthKeys() = OauthKeys(
            consumerKey = "KRQwHtSsmZPm7OGCtAtKJpJ16",
            consumerSecret = "gwFEPneitnCMJrEvWjhhC4REIhZSd5qbibjH752CxrCv6E7zow",
            accessToken = "1318229087148789760-sZraj39sCJQHMYnXbwPvn6Ky3YO7jT",
            accessSecret = "mfflmvddCJea5Pj8Z1fMWzB0n3WvfE8C2sgOOZV88N5Bx"
        )

    }

}