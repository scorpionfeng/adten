package com.xtooltech.adten.module.diy

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.common.obd.TextString.result
import com.xtooltech.adten.databinding.ActivityFlowMilBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter


class MilViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
        FlowItem(0x01,"MIL灯(故障指示灯)状态",true,"","亮/灭"),
        FlowItem(0x21,"故障指示灯点亮后行驶里程数",true,"","公里"),
        FlowItem(0x31,"故障码清除后行驶里程数",true,"","公里"),
        FlowItem(0x4D,"故障指标灯点亮后运行时间",true,"","分钟"),
        FlowItem(0x4e,"故障码清除后运行时间",true,"","分钟"),
        FlowItem(0x1f,"发动机启动后运行时间",true,"","分钟")
    )
}


@Route(path = PATH_DIY_MIL)
class MilActivity : BaseVMActivity<ActivityFlowMilBinding, MilViewModel>() {

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
                val value = ObdManger.getIns().readCommonRaw(item.kind)


                printMessage("kind=${item.kind}>" )


                when(item.kind){
                    0x01.toByte()->{
                        var arrayInner = value?.array
                        var flag = arrayInner?.get(arrayInner.size - 5)
                        var result = flag?.toInt()?.shr(7)

                        (result==1).trueLet {
                            item.content="灯亮"
                        }.elseLet {
                             item.content="灯灭"
                        }
                    }
                    0x21.toByte()->{
                        var arrayInner = value?.array
                        var flagA = arrayInner?.get(arrayInner.size - 5)
                        var flagB = arrayInner?.get(arrayInner.size - 4)

                        if (flagB!=null) {
                            flagB=  (flagB+256.toShort()).toShort()
                        }

                        var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                        item.content=distance.toString()
                    }
                    0x31.toByte()->{
                        var arrayInner = value?.array
                        var flagA = arrayInner?.get(arrayInner.size - 5)
                        var flagB = arrayInner?.get(arrayInner.size - 4)
                        if (flagB!=null) {
                            flagB=  (flagB+256.toShort()).toShort()
                        }
                        var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                        item.content=distance.toString()
                    }
                    0x4D.toByte()->{
                        var arrayInner = value?.array
                        var flagA = arrayInner?.get(arrayInner.size - 5)
                        var flagB = arrayInner?.get(arrayInner.size - 4)
                        if (flagB!=null) {
                            flagB=  (flagB+256.toShort()).toShort()
                        }
                        var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                        item.content=distance.toString()
                    }
                    0x4E.toByte()->{
                        var arrayInner = value?.array
                        var flagA = arrayInner?.get(arrayInner.size - 5)
                        var flagB = arrayInner?.get(arrayInner.size - 4)
                        if (flagB!=null) {
                            flagB=  (flagB+256.toShort()).toShort()
                        }

                        var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                        item.content=distance.toString()
                    }
                }

                runOnUiThread{
                    adapter.notifyDataSetChanged()
                }

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

    override fun getLayoutId(): Int = R.layout.activity_flow_mil

    override fun getBindingId(): Int = BR.model
}
