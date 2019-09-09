package br.com.fluo.fluo.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.fluo.fluo.BuildConfig
import br.com.fluo.fluo.R
import br.com.fluo.fluo.app.FluoApp
import br.com.fluo.fluo.helper.SharedPreferencesHelper
import br.com.fluo.fluo.models.Account
import br.com.fluo.fluo.services.RetrofitInitializer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            login()
        }

        btnRegister.setOnClickListener {
            register()
        }

        textForgot.setOnClickListener {
            forgot()
        }

        // TODO retirar
        if (BuildConfig.DEBUG) {

            email.setText("dirceu@fourtime.com")
            password.setText("senha123")

        }

    }

    fun forgot() {
        var intent = Intent(this@LoginActivity, ForgotActivity::class.java)
        intent.putExtra("email", email.text.toString())
        startActivity(intent)
    }

    fun register() {
        var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login() {

        progress.visibility = View.VISIBLE

        var account = Account()
        account.email = email.text.toString()
        account.password = password.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.auth(account)

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

                progress.visibility = View.GONE

                response?.let {

                    if (it.code() == 200) {

                        FluoApp.account = it.body()

                        SharedPreferencesHelper.saveString(
                            this@LoginActivity,
                            "account",
                            "userData",
                            account.getJSON().toString()
                        )

                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {

                        MaterialDialog.Builder(this@LoginActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.ops)
                            .content(R.string.invalid_user_and_pass)
                            .positiveText(R.string.ok)
                            .show()

                    }


                }


            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {

                progress.visibility = View.GONE

                Toast.makeText(this@LoginActivity, "Ops", Toast.LENGTH_LONG).show()
            }

        })


    }
}
