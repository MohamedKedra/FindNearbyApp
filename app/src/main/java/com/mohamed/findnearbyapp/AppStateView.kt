package com.mohamed.findnearbyapp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.app_state_view.view.*

class AppStateView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context,R.layout.app_state_view,this)
    }

    fun setMessage(msg : String){
        tv_message.text = msg
    }

    fun setLoading(isLoading : Boolean){
        if(isLoading){
            pb_loading.visibility = View.VISIBLE
            iv_icon_msg.visibility = View.GONE
        }else{
            pb_loading.visibility = View.GONE
            iv_icon_msg.visibility = View.VISIBLE
        }
    }

    fun setIconMessage(res : Int){
        iv_icon_msg.setImageResource(res)
    }

}