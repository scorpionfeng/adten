package com.xtooltech.adten.module.diy

import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Switch
import androidx.core.widget.addTextChangedListener
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
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter


class FuelViewModel : ViewModel() {
    var datas= mutableListOf<FlowItem>(
        FlowItem(1,"总时间",true,"","秒"),
        FlowItem(2,"平均速度",true,"","km/h"),
        FlowItem(3,"行驶里程",true,"","M"),
        FlowItem(4,"速度",true,"","km/h"),
        FlowItem(5,"区间距离",true,"","km"),
        FlowItem(6,"瞬间油耗",true,"","conn"),
        FlowItem(7,"总油耗",true,"","conn"),
        FlowItem(8,"平均油耗",true,"","conn"),
        FlowItem(9,"区间时间",true,"","秒")
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

    var model=false


    lateinit var  adapter:UniversalAdapter<FlowItem>



    override fun initView() {

        datas=vm.datas

        datas.forEach{
            printMessage("it= ${it.title} + ${it.selected}")
        }
        adapter=UniversalAdapter(datas,R.layout.item_flow_fuel,BR.model)
        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)

        vb.switch1.setOnCheckedChangeListener{
           view, checked->
            model=checked
            toast("model= "+model)
        }
        vb.button15.setOnClickListener{
            startTime=System.currentTimeMillis()
            preTime=startTime
            start()
        }



    }

    fun  process() {

        var speedData = ObdManger.getIns().readFlowItem(ObdItem(0x0D, "车速", false, "", "RPM", "0x00,0x00,0x0D,0x00"))
        takeIf { speedData.isNotEmpty() }?.apply { currSpeed = speedData.toFloat() }
        var duration=(System.currentTimeMillis()-preTime)/1000L
        lastDistance = currSpeed *duration/3600
        totalDistance += lastDistance
        totalDuration+=duration
        val airflow =ObdManger.getIns().readFlowItem(ObdItem(0x10 , "空气流量", false, "", "g/s", "0x00,0x00,0x10,0x00"))
        var trend=0.0f
        takeIf { (airflow.isNotEmpty() &&  airflow.toFloat()>0) && model }?.apply {
//            trend = 1.0f * airflow.toFloat() * 33.77903f / currSpeed
//            val fuelCons = trend * lastDistance / 100
//            totalCons += fuelCons

            trend=airflow.toFloat()/14.7f/0.725f/1000*3600*(duration/3600f)
            totalCons += trend

        }?:apply {
            var displace=1.6
            var fuelAdj=1.0
            val airPress =ObdManger.getIns().readFlowItem(ObdItem(0x0b,"进气压力",false,"","pa","0x00,0x00,0x0B,0x00", listOf()))
            val airTemp =ObdManger.getIns(). readFlowItem(ObdItem(0x0f,"空气温度",false,"","c","0x00,0x00,0x0F,0x00", listOf()))
             val engineRpm = ObdManger.getIns().readFlowItem(ObdItem(0x0c,"转速",false,"","","0x00,0x00,0x0C,0x00", listOf()))
            if(!TextUtils.isEmpty(airPress) && !TextUtils.isEmpty(airTemp) && !TextUtils.isEmpty(engineRpm)){
                var valueAirPress=airPress.toFloat()
                var valueAirTemp:Float=airTemp.toFloat()
                var valueRpm:Float=engineRpm.toFloat()
//                trend= (fuelAdj*8.513/1000*valueRpm*valueAirPress/(valueAirTemp+273.15)*displace*0.85).toFloat()
//                val fuelCons = trend * lastDistance / 100
//                totalCons += fuelCons
                trend= (fuelAdj*8.513/1000*valueRpm*valueAirPress/(valueAirTemp+273.15)*displace*0.85).toFloat()*(duration/3600f)
                totalCons += trend

            }
            //MAP 进气压力 0x0B
            //IAT   进气温度  0x0F

        }
        preTime=System.currentTimeMillis()

        handler.post(Runnable {
            /** 总时间*/
            vm.datas[0].content = ((preTime-startTime)/1000L).toString()
            /** 平均速度 */
            vm.datas[1].content = (totalDistance / (totalDuration/3600f)).toString()
            /** 行驶里程 */
            vm.datas[2].content = totalDistance.toString()
            /** 速度 */
            vm.datas[3].content = currSpeed.toString()
            /** 区间距离 */
            vm.datas[4].content = lastDistance.toString()
            /** 瞬间油耗 */
            vm.datas[5].content = lastDistance.toString()
            /** 总油耗 */
            vm.datas[6].content = totalCons.toString()
            /** 平均油耗 */
            vm.datas[7].content = (totalCons/totalDistance*100).toString()
            /** 区间时间 */
            vm.datas[8].content = duration.toString()
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

    fun click_switch(view: View) {

    }
}
