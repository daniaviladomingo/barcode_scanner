package avila.domingo.barcode.ui

import android.Manifest
import android.os.Bundle
import android.view.SurfaceView
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import avila.domingo.barcode.R
import avila.domingo.barcode.base.BaseActivity
import avila.domingo.barcode.databinding.ActivityMainBinding
import avila.domingo.barcode.ui.data.ResourceState
import avila.domingo.barcode.util.extension.isPermissionGranted
import avila.domingo.barcode.util.extension.isPermissionsGranted
import avila.domingo.barcode.util.extension.requestPermission
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val requestCodeCamera = 1

    private val surfaceView: SurfaceView by inject()

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    private val lifecycleObserver: Unit by inject { parametersOf(this.lifecycle) }

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleObserver.run { }

        AlphaAnimation(0.0f, 1.0f).run {
            duration = 500
            startOffset = 20
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
            binding.scannerLine.startAnimation(this)
        }

        if (isPermissionGranted(Manifest.permission.CAMERA)) {
            init()
        } else {
            requestPermission(Manifest.permission.CAMERA, requestCodeCamera)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.surfaceView.addView(surfaceView)
    }

    override fun onPause() {
        super.onPause()
        binding.surfaceView.removeView(surfaceView)
    }

    private fun init() {
        setListener()
        mainActivityViewModel.read()
    }

    private fun setListener() {
        mainActivityViewModel.barcodeLiveData.observe(this) { resource ->
            resource.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        toast?.cancel()
                        toast = Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT)
                            .apply { show() }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodeCamera -> {
                if (grantResults.isPermissionsGranted()) {
                    init()
                } else {
                    finish()
                }
            }
        }
    }

    override fun getView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}
}
