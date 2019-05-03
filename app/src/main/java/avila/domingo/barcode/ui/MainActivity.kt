package avila.domingo.barcode.ui

import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.lifecycle.Observer
import avila.domingo.barcode.R
import avila.domingo.barcode.base.BaseActivity
import avila.domingo.barcode.ui.data.ResourceState
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private val surfaceView: SurfaceView by inject()

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListener()
        mainActivityViewModel.read()
    }

    override fun onResume() {
        super.onResume()
        surface_view.addView(surfaceView)
    }

    override fun onPause() {
        super.onPause()
        surface_view.removeView(surfaceView)
    }

    private fun setListener() {
//        mainActivityViewModel.imagesLiveData.observe(this, Observer { resource ->
//            resource?.run {
//                managementResourceState(status, message)
//                if (status == ResourceState.SUCCESS) {
//                    data?.run {
//                        Log.d("ccc", "${width}x$height ${pixels.size}")
//                    }
//                }
//            }
//        })

        mainActivityViewModel.barcodeLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
                        Log.d("ccc", "Leo: $this")
                    }
                }
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}
}
