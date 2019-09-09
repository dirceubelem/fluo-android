package br.com.fluo.fluo.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Account
import br.com.fluo.fluo.services.RetrofitInitializer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import kotlinx.android.synthetic.main.activity_forgot.*
import retrofit2.Call
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        email.setText(intent.getStringExtra("email"))

        btnForgot.setOnClickListener {
            forgot()
        }
    }

    fun forgot() {
        var account = Account()
        account.email = email.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.forgot(account)

        call.enqueue(object : retrofit2.Callback<Void> {

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                response?.let {

                    if (it.code() == 204) {

                        MaterialDialog.Builder(this@ForgotActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.success)
                            .content(R.string.forgot_message)
                            .positiveText(R.string.ok)
                            .onPositive { dialog, which ->
                                finish()
                            }
                            .show()

                    }

                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {

            }

        })


    }
}
