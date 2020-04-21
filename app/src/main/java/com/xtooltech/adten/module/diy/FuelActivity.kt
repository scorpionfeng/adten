package com.xtooltech.adten.module.diy

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityDiyFuelBinding
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.widget.UniversalAdapter


class FuelViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
        FlowItem(1,"总时间",true,"","秒"),
        FlowItem(2,"平均速度",true,"","km/h"),
        FlowItem(3,"行驶里程",true,"","M"),
        FlowItem(4,"平均油耗",true,"","%"),
        FlowItem(5,"速度",true,"","km/h"),
        FlowItem(6,"区间距离",true,"","km"),
        FlowItem(7,"瞬间油耗",true,"","conn"),
        FlowItem(8,"总油耗",true,"","conn"),
        FlowItem(9,"平均油耗",true,"","conn"),
        FlowItem(10,"区间时间",true,"","秒")
    )
}


@Route(path = PATH_DIY_FUEL)
class FuelActivity : BaseVMActivity<ActivityDiyFuelBinding, FuelViewModel>() {

    private var totalDuration: Float=0.0f
    private var totalCons: Float =0f
    private var totalDistance: Float=0.0f
    private var totoalTime:Float=0f
    val handler: Handler= Handler()
    lateinit var  datas:MutableList<FlowItem>
    var amount=0

    var show=true

     var startTime:Long=0L
    var preTime:Long =0L
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

        startTime=System.currentTimeMillis()
        preTime=startTime
        start()

    }

    fun process() {

        var speedData = ObdManger.getIns().readFlowItem(ObdItem(0x0D, "车速", false, "", "RPM", "0x00,0x00,0x0D,0x00"))
        //99
//        var speedData=93f
//        currSpeed=93f
        takeIf { speedData.isNotEmpty() }?.apply { currSpeed = speedData.toFloat() }
        //4.36=99*
        var duration=(System.currentTimeMillis()-preTime)/1000L
        lastDistance = currSpeed *duration/3600
        totalDistance += lastDistance
        totalDuration+=duration
       // 535
        val airflow =ObdManger.getIns().readFlowItem(ObdItem(0x10, "空气流量", false, "", "g/s", "0x00,0x00,0x10,0x00"))
        var trend=0.0f
        takeIf { airflow.isNotEmpty() &&  airflow.toFloat()>0}?.apply {
             trend = 1.0f * airflow.toFloat() * 33.77903f / currSpeed     // 131 = 384*33.77903 / 99
            val fuelCons = trend * lastDistance / 100 //-> 5=131 *4.3/100
            totalCons += fuelCons   // 5
        }
        preTime=System.currentTimeMillis()

        handler.post(Runnable {
            /** 总时间*/
            vm.datas[0].content = ((preTime-startTime)/1000L).toString()
            /** 平均速度 */
            vm.datas[1].content = (totalDistance / (totalDuration/3600f)).toString()
            /** 行驶里程 */
            vm.datas[2].content = totalDistance.toString()
            /** 平均油耗 */
            vm.datas[3].content = totalCons.toString()
            /** 速度 */
            vm.datas[4].content = currSpeed.toString()
            /** 区间距离 */
            vm.datas[5].content = lastDistance.toString()
            /** 瞬间油耗 */
            vm.datas[6].content = lastDistance.toString()
            /** 总油耗 */
            vm.datas[7].content = totalCons.toString()
            /** 平均油耗 */
            vm.datas[8].content = (totalCons/totalDistance*100).toString()
            /** 区间时间 */
            vm.datas[9].content = duration.toString()
            printMessage(vm.datas.toString())
            adapter.notifyDataSetChanged()

        })




    }


    private fun start(){

        var task=object :Runnable{
            override fun run() {
                Thread{
                    amount++
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
