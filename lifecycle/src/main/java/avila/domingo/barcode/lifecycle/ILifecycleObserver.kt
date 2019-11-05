package avila.domingo.barcode.lifecycle

interface ILifecycleObserver {
    fun start()
    fun stop()
    fun destroy()
}