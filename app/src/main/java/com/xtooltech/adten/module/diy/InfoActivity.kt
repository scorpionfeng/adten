package com.xtooltech.adten.module.diy

import android.app.backup.FullBackupDataOutput
import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityDiyInfoBinding
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter
import java.io.File


class InfoViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
        FlowItem(1,"VIN",true,"",""),
        FlowItem(2,"液位",true,"","%"),
        FlowItem(3,"电压",true,"","mv"),
        FlowItem(4,"油耗",true,"","%"),
        FlowItem(5,"进气流量",true,"","g/s"),
        FlowItem(6,"固件版本",true,"","")
    )
}


@Route(path = PATH_DIY_INFO)
class InfoActivity : BaseVMActivity<ActivityDiyInfoBinding, InfoViewModel>() {

    val handler: Handler= Handler()
    lateinit var  datas:MutableList<FlowItem>

    var show=true


    lateinit var  adapter:UniversalAdapter<FlowItem>



    override fun initView() {

        datas=vm.datas

        datas.forEach{
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter=UniversalAdapter(datas,R.layout.item_flow_info,BR.model)
        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{
            _,item,index->

            request_obd_state(item)
        }

    }

    private fun request_obd_state(item:FlowItem) {
        printMessage("request ${item.title}")
        Thread{

            when(item.kind){
                1.toByte()->{
                    var readVinCode = ObdManger.getIns().readVin()
                    readVinCode.apply {
                        item.content=readVinCode
                    }
                }
                2.toByte()->{
                    var fuelConsValue= ObdManger.getIns().fuelCons()
                    fuelConsValue?.apply{
                        item.content=fuelConsValue
                    }
                }
                3.toByte()->{
                    var dvValue= ObdManger.getIns().readDv()
                    dvValue?.apply{
                        item.content=dvValue
                    }
                }
                4.toByte()->{
                    var fuelConsValue= ObdManger.getIns().fuelCons()
                    fuelConsValue?.apply{
                      item.content=fuelConsValue
                    }
                }
                5.toByte()->{
                    var airValue= ObdManger.getIns().readFlowItem(ObdItem(0x10,"进气流量",false,"","g/s","0x00,0x00,0x10,0x00"))
                    airValue?.apply{
                      item.content=airValue
                    }
                }
                6.toByte()->{
                    var info= ObdManger.getIns().readBoxInfo()
                    info?.apply{
                        item.content=info.version
                    }
                }

            }

                runOnUiThread{
                    adapter.notifyDataSetChanged()
                }


        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        show=false
    }


    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_diy_info

    override fun getBindingId(): Int = BR.model
}
