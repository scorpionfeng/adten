package com.xtooltech.adten.module.account

import android.text.InputType
import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.xtooltech.adten.databinding.ActivityAccountBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.util.printMessage
import kotlinx.android.synthetic.main.activity_account.*

class AccountViewModel : ViewModel() {

     val account:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

     val accountError:MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }


    val passwd:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

     val passwdError:MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    fun login(view: View){


        account.value?:apply {
            accountError.value="帐号不能为空"
            return
        }

        passwd.value?:apply {
            passwdError.value="密码不能为空"
            return
        }

        printMessage("登录中....")




    }

    fun guest(view:View){

        ARouter.getInstance().build(PATH_HOME).navigation()

    }

    fun findPwd(view:View){

        ARouter.getInstance().build(PATH_FINDPWD).navigation();

    }

    fun userProto(view:View){
        ARouter.getInstance().build(PATH_PROTO).navigation();
    }

    fun secret(view:View){
        ARouter.getInstance().build(PATH_SECRET).navigation();

    }

     fun isUserNameValid(username: String): Boolean {

         username.contains('@').trueLet {
             return Patterns.EMAIL_ADDRESS.matcher(username).matches()
         }
         return false
    }

}

@Route(path = PATH_LOGIN)
class AccountActivity : BaseVMActivity<ActivityAccountBinding, AccountViewModel>() {

    override fun initView() {

        account.inputType=InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        account.afterTextChanged {

            vm.isUserNameValid(it).falseLet {
                account.setError("必须是邮箱",resources.getDrawable(R.drawable.ic_default_image))
            }

        }
    }

    override fun initData() {
        vm.accountError.observe(this, Observer {
            account.setError(it,getDrawable(R.drawable.ic_default_image))
        })

        vm.passwdError.observe(this, Observer {
            password.setError(it,getDrawable(R.drawable.ic_default_image))
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_account

    override fun getBindingId(): Int = BR.model


}
