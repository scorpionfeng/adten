package com.xtooltech.adten.module.diy
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.adten.common.obd.Freeze_DataType_STD
import com.xtooltech.adten.databinding.ActivityFreezeListBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter


class FreezeListViewModel : ViewModel() {



    companion object{
        private lateinit var instance: FreezeListViewModel

       fun setIns(ins:FreezeListViewModel){
           instance=ins
       }

        fun getIns():FreezeListViewModel{
            return instance
        }
    }

    var datas= mutableListOf<ObdItem>(
    )
}




@Route(path = PATH_DIY_FREEZE)
class FreezeListActivity : BaseVMActivity<ActivityFreezeListBinding, FreezeListViewModel>() {

    private lateinit  var  hud: KProgressHUD
    private lateinit var handler: Handler

    val adapter= UniversalAdapter(vm.datas,R.layout.item_freeze,BR.model)

    var maskBuffer = ShortArray(32)

    private fun getData() {
        UtilThread.execute{
            var enterSucc = ObdManger.getIns().enter()
            printMessage("entersucc ?= $enterSucc")
            enterSucc?.trueLet {

                var datas: MutableList<ByteArray?> = supportFreeze()

                (datas.size>0).trueLet {
                        mergeFreezePid(datas, maskBuffer, ObdManger.getIns().computerOffset())
                        var pid = produFreezePid(maskBuffer)
                        printMessage(pid.toString())
                        var freezeKeyList = dataFlow4KeyList(pid,0x02)

                        freezeKeyList.forEach {
                            var element = ds[it.toObdIndex()]
                            element?.apply {
                                vm.datas.add(ObdItem(it.toObdPid(), element.first, false, "", element.second, it.toObdIndex()))
                            }
                    }

                }


            }

            handler.post{
                adapter.notifyDataSetChanged()
                toast("succe ? ="+enterSucc)
                hud.dismiss()

            }
        }
    }

    override fun initView() {

        handler=Handler(Looper.getMainLooper())

        hud= KProgressHUD(this).setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在加载...")
        hud.show()

        getData()

        vb.list.adapter=adapter
        vb.list.layoutManager= LinearLayoutManager(this)
        adapter.setOnItemClick{
            bd,item,index->

            readItem(item)
        }

    }





    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.activity_freeze_list

    override fun getBindingId(): Int = BR.model


    fun readItem(data:ObdItem){
        Thread {

            var value = ObdManger.getIns().readFreezeState(data)
            data.content = value

            runOnUiThread {
                toast("value= $value")
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    fun readData(view: View) {
        Thread{

            vm.datas.forEach{
                var value = ObdManger.getIns() .readFreezeState(it)
                it.content=value
            }

            runOnUiThread{
                adapter.notifyDataSetChanged()
            }



        }.start()
    }


}
