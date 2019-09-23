package br.com.fluo.fluo.ui.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import br.com.fluo.fluo.R
import br.com.fluo.fluo.app.FluoApp
import br.com.fluo.fluo.helper.DateTime
import br.com.fluo.fluo.helper.ImageHelper
import br.com.fluo.fluo.helper.SDCardUtils
import br.com.fluo.fluo.helper.SharedPreferencesHelper
import br.com.fluo.fluo.helper.db.DBHelper
import br.com.fluo.fluo.models.Account
import br.com.fluo.fluo.services.RetrofitInitializer
import br.com.fluo.fluo.ui.dialogs.NewTaskDialog
import br.com.fluo.fluo.ui.fragments.ProjectsFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity(), NewTaskDialog.NewTaskDialogListener {

    private val GALLERY = 1
    private val CAMERA = 2
    private var type: Int = 0
    private var pictureFile: String = ""
    lateinit var imageViewChangeReference: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userWelcome.text = getString(R.string.textwelcome, FluoApp.account!!.name)
//        userWelcome.text = String.format(resources.getString(R.string.textwelcome), FluoApp.account!!.name)
        howManyTasks.setText(R.string.textnotasks)

        var url = "https://file.fluo.site/".plus(FluoApp.account!!.picture)

        Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).into(avatar)

        setFragment(ProjectsFragment())

        avatar.setOnClickListener {

            photoClick(avatar, 1)

        }

        btnaddtask.setOnClickListener {

            var i = Intent(this, AddTaskActivity::class.java)
            startActivity(i)

//            AddTaskDialog.getDialog().show(supportFragmentManager, "")
        }

    }

    fun getDatabase() {

        val s = RetrofitInitializer().serviceDatabase()

        val call = s.getDatabase()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    DBHelper.salvarBanco(this@MainActivity, response.body().byteStream())
                } catch (e: Exception) {
                    Log.e("Baixando banco", e.message)
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    fun photoClick(image: ImageView, type: Int) {
        this.type = type
        imageViewChangeReference = image
        showMenuDialog()
    }

    fun showMenuDialog() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showMenu()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()

    }

    fun showMenu() {

        val pictureDialog =
            android.support.v7.app.AlertDialog.Builder(this)
        pictureDialog.setTitle(R.string.select_menu)

        val pictureDialogItems = arrayOf(
            getString(R.string.select_camera),
            getString(R.string.select_gallery),
            getString(R.string.get_database),
            getString(R.string.select_exit)
        )

        pictureDialog.setItems(pictureDialogItems) { dialog, which ->

            when (which) {
                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGalery()
                2 -> getDatabase()
                3 -> exitFluo()
            }

        }

        pictureDialog.show()

    }

    fun takePhotoFromCamera() {

        val image = DateTime.now().toString("yyyyMMddHHmmss")

        val file =
            SDCardUtils.getSdCardFile(this, FluoApp.Directories.IMAGES, image + FluoApp.TYPE_IMAGE)

        pictureFile = file.absolutePath

        val uri = FileProvider.getUriForFile(this, FluoApp.FileProvider.authority, file)

        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(i, CAMERA)

    }

    fun choosePhotoFromGalery() {

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // imagem selecionada a partir da galeria
        data?.let {

            if (requestCode == GALLERY) {

                val contentURI = it.data

                try {

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                    saveBitmapFromGallery(bitmap)

                } catch (e: IOException) {
                }

            }

        }

        if (data == null && requestCode == CAMERA) {
            saveBitmapFromCamera()
        }

    }

    fun saveBitmapFromGallery(bitmap: Bitmap) {

        var rotate = bitmap.height < bitmap.width

        var bm = ImageHelper.cropToSquare(bitmap)

        var fileName = ImageHelper.imagePath(DateTime.now().toString("yyyyMMddHHmmss")).absolutePath
        var file = ImageHelper.saveImage(this, bm, fileName)

        val myFile = File(file)

        Glide.with(this).load(myFile).apply(RequestOptions.circleCropTransform()).into(avatar)

        var uri = Uri.fromFile(myFile)

        ImageHelper.normalizeImageForUri(this, uri, rotate)

        sendFile(myFile)
    }

    fun saveBitmapFromCamera() {

        var bitmap = BitmapFactory.decodeFile(pictureFile)

        var rotate = bitmap.height < bitmap.width

        bitmap = ImageHelper.cropToSquare(bitmap)

        ImageHelper.saveImage(this, bitmap, pictureFile)

        var file = File(pictureFile)

        var uri = Uri.fromFile(file)

        ImageHelper.normalizeImageForUri(this, uri, rotate)

        Glide.with(this).load(file).apply(RequestOptions.circleCropTransform()).into(avatar)
        sendFile(file)
    }

    fun sendFile(file: File) {

        var s = RetrofitInitializer().serviceAccount()

        var call = s.sendPhoto(
            image = MultipartBody.Part.createFormData(
                "filename", file.name,
                RequestBody.create(MediaType.parse("image/*"), file)
            )
        )

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {

            }

        })

    }

    // envio de múltiplos arquivos
    fun sendFile(file: List<File>, position: Int) {

        if (position > file.size) {

            var s = RetrofitInitializer().serviceAccount()

            var call = s.sendPhoto(
                image = MultipartBody.Part.createFormData(
                    "filename", file[position].name,
                    RequestBody.create(MediaType.parse("image/*"), file[position])
                )
            )

            call.enqueue(object : retrofit2.Callback<Account> {

                override fun onResponse(call: Call<Account>?, response: Response<Account>?) {
                    sendFile(file, position + 1)
                }

                override fun onFailure(call: Call<Account>?, t: Throwable?) {

                }

            })

        } else {
            // TODO: sucesso
        }

    }


    fun exitFluo() {

        MaterialDialog.Builder(this).theme(Theme.LIGHT).title(R.string.confirm)
            .content(R.string.confirm_exit)
            .positiveText(R.string.confirm_yes).onPositive { dialog, which ->

                SharedPreferencesHelper.delete(this, "account", "userData")

                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()

            }.negativeText(R.string.confirm_no).show()

    }

//    val millisInFuture: Long = 60000
//
//    fun createTimer() {
//        val countDownInterval: Long = 1000
//        timer(millisInFuture, countDownInterval).start()
//    }
//
//    fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
//
//        return object : CountDownTimer(millisInFuture, countDownInterval) {
//
//            override fun onFinish() {
//
//                // TODO: exibe a mensagem que o tempo expirou, etc
//
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//
//                var perc = millisInFuture - millisUntilFinished
//                var progress = ((perc * 100) / millisInFuture)
//
//                progressAvatar.progress = progress.toInt()
//
//            }
//        }
//
//    }

    override fun selected(id: String, index: Int) {

    }

    fun setFragment(f: Fragment) {
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, f)
        ft.addToBackStack(null)
        ft.commit()
    }

}
