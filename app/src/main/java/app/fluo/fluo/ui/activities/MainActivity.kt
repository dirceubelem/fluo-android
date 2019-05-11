package app.fluo.fluo.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.fluo.fluo.R
import app.fluo.fluo.model.Account
import app.fluo.fluo.services.RetrofitInitializer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener {
            auth()
        }

        register.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgot.setOnClickListener {
            var intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    fun auth(){

        var s = RetrofitInitializer().serviceAccount()

        var account = Account()
        account.email = email.text.toString()
        account.password = password.text.toString()

        var call = s.auth(account)

        call.enqueue(object: retrofit2.Callback<Account>{

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

                response?.let {

                    if(it.code() == 200){
                        Toast.makeText(this@MainActivity, "Sucesso", Toast.LENGTH_LONG).show()
                    }else{
                        MaterialDialog.Builder(this@MainActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.mainactivity_ops)
                            .content(R.string.mainactivity_user_pass_invalid)
                            .positiveText(R.string.mainactivity_ok)
                            .show()
                    }

                }

            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Ops", Toast.LENGTH_LONG).show()
            }

        })

    }
}
