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
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityFreezeListBinding
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.adtenx.util.toObdIndex
import com.xtooltech.adtenx.util.toObdPid
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



    private fun getData() {
        Thread{
            val enterSucc = ObdManger.getIns().enter()
            printMessage("entersucc ?= $enterSucc")
            enterSucc?.trueLet {


                var freezeList = ObdManger.getIns().queryFreezeList()

                freezeList.isNotEmpty().trueLet {
                    freezeList.forEach {
                        var element = ds[it.toObdIndex()]
                        element?.apply {
                            vm.datas.add(ObdItem(it.toObdPid(), element.first, false, "", element.second, it.toObdIndex()))
                        }
                    }
                }
            }

            handler.post {
                adapter.notifyDataSetChanged()
                toast("succe ? =" + enterSucc)
                hud.dismiss()

            }
        }.start()
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

            var value = ObdManger.getIns().readFreezeItem(data)
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
                var value = ObdManger.getIns() .readFreezeItem(it)
                it.content=value
            }

            runOnUiThread{
                adapter.notifyDataSetChanged()
            }



        }.start()
    }


}
