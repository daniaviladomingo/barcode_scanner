package avila.domingo.barcode.base

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import avila.domingo.barcode.R
import avila.domingo.barcode.databinding.ActivityBaseBinding
import avila.domingo.barcode.ui.data.ResourceState

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private lateinit var baseBinding: ActivityBaseBinding

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)

        binding = getView()
        (baseBinding.view).addView(
            binding.root,
            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        )

        baseBinding.viewEmpty.emptyListener = checkAgain()
        baseBinding.viewError.errorListener = tryAgain()

        initializeToolbar()
    }
    protected abstract fun getView(): T

    private fun initializeToolbar() {}

    protected fun managementResourceState(resourceState: ResourceState, message: String?) {
        when (resourceState) {
            ResourceState.LOADING -> {
                baseBinding.view.visibility = VISIBLE
                baseBinding.viewError.visibility = GONE
                baseBinding.viewEmpty.visibility = GONE
                baseBinding.viewProgress.visibility = VISIBLE
            }
            ResourceState.SUCCESS -> {
                baseBinding.view.visibility = VISIBLE
                baseBinding.viewError.visibility = GONE
                baseBinding.viewEmpty.visibility = GONE
                baseBinding.viewProgress.visibility = GONE
            }
            ResourceState.EMPTY -> {
                baseBinding.view.visibility = GONE
                baseBinding.viewError.visibility = GONE
                baseBinding.viewEmpty.visibility = VISIBLE
                baseBinding.viewProgress.visibility = GONE
            }
            ResourceState.ERROR -> {
                baseBinding.view.visibility = GONE
                baseBinding.viewError.visibility = VISIBLE
                baseBinding.viewError.findViewById<TextView>(R.id.error_message).text = message ?: ""
                baseBinding.viewEmpty.visibility = GONE
                baseBinding.viewProgress.visibility = GONE
            }
        }
    }

    abstract fun checkAgain(): () -> Unit

    abstract fun tryAgain(): () -> Unit
}