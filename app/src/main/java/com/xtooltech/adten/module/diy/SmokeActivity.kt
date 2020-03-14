package com.xtooltech.adten.module.diy

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFlowSmokeBinding
import com.xtooltech.adten.util.PATH_DIY_SMOKE
import com.xtooltech.adten.util.UtilThread
import com.xtooltech.adten.util.falseLet
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter


class SmokeViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
//        FlowItem(0x01, "MIL灯(故障指示灯)状态",true,"","灯亮/灯灭"),
        FlowItem(0x03,"故障码",true,"","个"),
        FlowItem(0x07,"未决故障码",true,"","个"),
        FlowItem(0x0a,"永久故障码",true,"","个")
//        FlowItem(0x4e,"失火监测",true,"","XX公里"),
//        FlowItem(0x1f,"燃油系统监测",true,"","XX小时/XX分钟/XX秒"),
//        FlowItem(0x1f,"综合成分监测",true,"","XX小时/XX分钟/XX秒"),
//        FlowItem(0x1f,"EGR和/或VVT系统",true,"","XX小时/XX分钟/XX秒")
    )
}



@Route(path = PATH_DIY_SMOKE)
class SmokeActivity : BaseVMActivity<ActivityFlowSmokeBinding, SmokeViewModel>() {

    val handler: Handler= Handler()
    lateinit var  datas:MutableList<FlowItem>

    var show=true

    var enterSucc:Boolean? = false

    lateinit var  adapter:UniversalAdapter<FlowItem>



    override fun initView() {

        val datas=vm.datas.filter { it.selected }

        datas.forEach{
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter=UniversalAdapter(datas,R.layout.item_flow_detail,BR.model)
        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{
            _,item,index->

            request_obd_state(item)
//            when(item.kind.toInt()){
//                0x01.toInt()->request_obd_state(item)
//                0x21.toInt()->request_obd_state1(item)
//                0x31.toInt()->request_obd_state2(item)
//                0x4D.toInt()->request_obd_state3(item)
//                0x4e.toInt()->request_obd_state4(item)
//                0x1f.toInt()->request_obd_state5(item)
//
//            }
        }




    }

    private fun request_obd_state(item:FlowItem) {
        printMessage("request ${item.title}")
        Thread{

            enterSucc?.falseLet {
                printMessage("entersucc ?= $enterSucc")
                enterSucc = ObdManger.getIns().enter()
            }


            enterSucc?.trueLet {
                val value = ObdManger.getIns().readVin()
                printMessage("kind=${item.kind}>" + value)
            }






        }.start()
    }





    private fun request_obd_state1(item:FlowItem) {
        printMessage("request ${item.title}")
    }
    private fun request_obd_state2(item:FlowItem) {
        printMessage("request ${item.title}")
    }
    private fun request_obd_state3(item:FlowItem) {
        printMessage("request ${item.title}")
    }
    private fun request_obd_state4(item:FlowItem) {
        printMessage("request ${item.title}")
    }
    private fun request_obd_state5(item:FlowItem) {
        printMessage("request ${item.title}")
    }


    private var speedRunnable= object :Runnable {


        override fun run() {
            UtilThread.execute {

                while (show) {
                    datas.forEach {
                        val value = ObdManger.getIns().readCommon(it.kind)
                        printMessage("kind= ${it.kind}>" + value)
                        it.content = value
                    }

                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        handler.postDelayed(this, 500)

                    }
                }


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        show=false
    }


    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_flow_smoke

    override fun getBindingId(): Int = BR.model
}
