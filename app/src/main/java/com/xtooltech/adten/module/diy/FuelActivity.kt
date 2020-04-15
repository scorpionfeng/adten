package com.xtooltech.adten.module.diy

import android.app.backup.FullBackupDataOutput
import android.os.Handler
import androidx.annotation.WorkerThread
import androidx.core.os.postAtTime
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityDiyFuelBinding
import com.xtooltech.adten.databinding.ActivityDiyInfoBinding
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter


class FuelViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
        FlowItem(1,"时间",true,"",""),
        FlowItem(2,"平时速度",true,"","km"),
        FlowItem(3,"行驶里程",true,"","M"),
        FlowItem(4,"平均油耗",true,"","%")
    )
}


@Route(path = PATH_DIY_FUEL)
class FuelActivity : BaseVMActivity<ActivityDiyFuelBinding, FuelViewModel>() {

    private var totalCons: Float =0f
    private var totalDistance: Float=0f
    val handler: Handler= Handler()
    lateinit var  datas:MutableList<FlowItem>

    var show=true

     var startTime:Long=0L
    var lastTime:Long =0L
    var speedUnit=1
    var currSpeed=0f

    var speed:Long=0

    var lastDistance=0f


    lateinit var  adapter:UniversalAdapter<FlowItem>



    override fun initView() {

        datas=vm.datas

        datas.forEach{
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter=UniversalAdapter(datas,R.layout.item_flow_fuel,BR.model)
        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)


        start()

    }

    fun process() {

        takeIf { startTime == 0L }?.apply { startTime = System.currentTimeMillis() }
//99
        var speedData = ObdManger.getIns().readFlowItem(ObdItem(0x0D, "车速", false, "", "RPM", "0x00,0x00,0x0D,0x00"))
        takeIf { speedData.isNotEmpty() }?.apply { currSpeed = speedData.toFloat() }
//4.36=99*
        lastDistance = currSpeed * (System.currentTimeMillis() - lastTime) / 1000.0f / 3600.0f
        totalDistance += lastDistance
        // 384
        val flowData = ObdManger.getIns().readFlowItem(ObdItem(0x10, "空气流量", false, "", "g/s", "0x00,0x00,0x10,0x00"))
        takeIf { flowData.isNotEmpty() }?.apply {
            val trend = 1.0f * flowData.toFloat() * 33.77903f / currSpeed     // 131 = 384*33.77903 / 99
            val fuelCons = trend * (lastDistance / 100) //-> 5=131 *4.3/100
            totalCons += fuelCons   // 5
        }

        lastTime = System.currentTimeMillis()

        handler.post(Runnable {
            vm.datas[0].content = lastTime.toString()
            vm.datas[1].content = (totalDistance / (lastTime - startTime)).toString()
            vm.datas[2].content = totalDistance.toString()
            vm.datas[3].content = totalCons.toString()
            adapter.notifyDataSetChanged()

        })

    }


    private fun start(){

        var task=object :Runnable{
            override fun run() {
                Thread{
                    process()
                    handler.postDelayed(this,2000)
                }.start()

            }
        }

        handler.post(task)


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

    override fun getLayoutId(): Int = R.layout.activity_diy_fuel

    override fun getBindingId(): Int = BR.model
}
