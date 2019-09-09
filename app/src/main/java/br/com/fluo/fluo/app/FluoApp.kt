package br.com.fluo.fluo.app

import android.app.Application
import br.com.fluo.fluo.models.Account

class FluoApp : Application {

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    object Directories {
        val IMAGES = "images"
    }

    object FileProvider {
        val authority = "br.com.fluo.fluo.files.provider.authority.1.x.1.1.abc.xyz_xpna"
    }

    companion object {

        // https://github.com/dirceubelem/task-api

        val TYPE_IMAGE = ".png"
        val URL_IMAGE = "https://file.fluo.site/"

        var account: Account? = null

        val PROD = true
        val URL_DEV = "https://api.fluo.site/v1/"
        val URL_PROD = "https://api.fluo.site/v1/"
        val URL_API = if (PROD) URL_PROD else URL_DEV

        private var instance: FluoApp? = null

        fun getInstance(): FluoApp? {
            return instance
        }

    }
}