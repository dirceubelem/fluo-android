package br.com.fluo.fluo.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Account
import br.com.fluo.fluo.services.RetrofitInitializer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            register()
        }

    }

    fun register(){

        var account = Account()
        account.email = email.text.toString()
        account.password = password.text.toString()
        account.name = name.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.register(account)

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

                response?.let {

                    if (it.code() == 200) {

                        MaterialDialog.Builder(this@RegisterActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.success)
                            .content(R.string.register_success)
                            .positiveText(R.string.ok)
                            .onPositive { dialog, which ->

                                var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)

                                finish()

                            }
                            .show()

                    } else {

                        MaterialDialog.Builder(this@RegisterActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.ops)
                            .content(R.string.register_duplicate)
                            .positiveText(R.string.ok)
                            .show()

                    }


                }


            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {
                Toast.makeText(this@RegisterActivity, "Ops", Toast.LENGTH_LONG).show()
            }

        })

    }
}
