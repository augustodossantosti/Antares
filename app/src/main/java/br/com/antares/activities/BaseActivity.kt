package br.com.antares.activities

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import br.com.antares.R
import br.com.antares.extensions.toast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * Activity que contém alguns métodos comuns a outras activities
 * da aplicação.
 *
 * @author Augusto Santos
 * @version 1.0
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST_CODE = 100
    }

    var toolbar: Toolbar? = null
    private var currentPhotoPath: Uri? = null

    /**
     *
     */
    fun takeAPhoto(): Uri? {
        validateCameraPermissions()
        return currentPhotoPath
    }

    /**
     * Realiza o processo de validação da permissões necessárias para
     * o app, verificando a necessidade de requeri-las ao usuario.
     */
    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private fun validateCameraPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (EasyPermissions.hasPermissions(this, *permissions)){
            launchCamera()
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.camera_request_rationale),
                    CAMERA_REQUEST_CODE, *permissions)
        }
    }

    /**
     * Executa o aplicativo padrão de câmera e salva a foto tirada.
     */
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            currentPhotoPath = fileUri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }
    }

    /**
     * Delega as operações referentes a permissões do uso da câmera
     * e acesso ao armazenamento interno a lib EasyPermissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,
                grantResults, this)
    }

    /**
     * Cria o menu da Toolbar.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    /**
     * Trata os eventos de clique nos diferentes botões
     * no menu da Toolbar.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId

        if (itemId == R.id.opt_search) {
            toast("Search")
            return true
        }
        if (itemId == R.id.opt_export) {
            toast("Export")
            return true
        }
        if (itemId == R.id.opt_settings) {
            toast("Settings")
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
