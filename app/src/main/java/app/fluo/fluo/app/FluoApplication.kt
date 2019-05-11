package app.fluo.fluo.app

import android.app.Application

class FluoApplication : Application {

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        val PROD = true
        val URL_API_DEV = "http://api.fluo.site/v1/"
        val URL_API_PROD = "http://api.fluo.site/v1/"
        val URL_API = if(PROD) URL_API_PROD else URL_API_DEV

        private var instance: FluoApplication? = null

        fun getIntance(): FluoApplication?{
            return instance
        }

    }

}