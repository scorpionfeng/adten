package com.xtooltech.adten.module.scan

import android.view.View
import androidx.lifecycle.ViewModel
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.databinding.ActivityScanBinding
import com.xtooltech.adten.util.PATH_SCAN
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast

class ConnectViewModel : ViewModel() {

    fun bleScan(view: View){

        printMessage("startScanning")
    }

}

@Route(path = PATH_SCAN)
class ScanActivity : BaseVMActivity<ActivityScanBinding, ConnectViewModel>() {

    override fun initView() {
        toast("scan")
    }

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_scan

    override fun getBindingId(): Int = BR.model
}
