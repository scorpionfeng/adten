package com.xtooltech.adten.module.diy

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFlowMilBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter
import kotlin.experimental.and


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

        datas = vm.datas
        datas.forEach {
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter = UniversalAdapter(datas, R.layout.item_flow_detail, BR.model)
        vb.list.adapter = adapter
        vb.list.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClick { _, item, index ->

            requestItem(item)
        }

      //  handler.postDelayed(speedRunnable, 1000)
    }

    private fun requestItem(item:FlowItem) {
        printMessage("request ${item.title}")
        Thread{

//            enterSucc?.falseLet {
//                printMessage("entersucc ?= $enterSucc")
//                enterSucc = ObdManger.getIns().enter()
//            }
//
//
//            enterSucc?.trueLet {
                val value = ObdManger.getIns().readCommonRaw(item.kind)


                printMessage("kind=${item.kind}>" )


                when(item.kind){
                    0x01.toByte()->{
                        value.isNotEmpty().trueLet {
                            var flag = value?.get(2)
                            var result = flag?.toInt()?.and(0x80)
                            (result==1).trueLet {
                                item.content="灯亮"
                            }.elseLet {
                                item.content="灯灭"
                            }
                        }

                    }
                    0x21.toByte()->{
                        value.isNotEmpty().trueLet {
                            var flagA = value?.get(2)
                            var flagB = value?.get(3)

                            if (flagB!=null) {
                                if(flagB<0){
                                    flagB = flagB.and(0xff.toByte())
                                }
                            }

                            var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                            item.content=distance.toString()
                        }

                    }
                    0x31.toByte()->{

                        value.isNotEmpty().trueLet {
                            var flagA = value?.get(2)
                            var flagB = value?.get(3)
                            if (flagB!=null) {
                                if(flagB<0){
                                    flagB = flagB.and(0xff.toByte())
                                }
                            }
                            var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                            item.content=distance.toString()
                        }

                    }
                    0x4D.toByte()->{
                        value.isNotEmpty().trueLet {
                            // 加7f还否定应答判断
                            var flagA = value?.get(2)
                            var flagB = value?.get(3)
                            if (flagB!=null) {
                                if(flagB<0){
                                    flagB = flagB.and(0xff.toByte())
                                }
                            }
                            var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                            item.content=distance.toString()
                        }

                    }
                    0x4E.toByte()->{
                        value.isNotEmpty().trueLet {
                            var flagA = value?.get(2)
                            var flagB = value?.get(3)
                            if (flagB!=null) {
                                if(flagB<0){
                                    flagB = flagB.and(0xff.toByte())
                                }
                            }

                            var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                            item.content=distance.toString()
                        }

                    }
                    0x1f.toByte()->{
                        value.isNotEmpty().trueLet {
                            var flagA = value?.get(2)
                            var flagB = value?.get(3)
                            if (flagB!=null) {
                                if(flagB<0){
                                    flagB = flagB.and(0xff.toByte())
                                }
                            }

                            var distance= (flagA?.times(256) ?: 0) + (flagB?:0)
                            item.content=distance.toString()
                        }

                    }
                }

                runOnUiThread{
                    adapter.notifyDataSetChanged()
                }

//            }

        }.start()
    }


    private var speedRunnable= object :Runnable {


        override fun run() {
            Thread {

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
            }.start()
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
