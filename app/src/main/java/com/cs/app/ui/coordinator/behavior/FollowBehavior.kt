package com.cs.app.ui.coordinator.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:39
 *
 * @Desc :
 */
class FollowBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<TextView>(context, attrs) {

    /**
     * 这个方法在对界面进行布局时至少会调用一次，用来确定本次交互行为中的dependent view，
     * 在上面的代码中，当dependency是Button类的实例时返回true，
     * 就可以让系统知道布局文件中的Button就是本次交互行为中的dependent view
     */
    override fun layoutDependsOn(parent: CoordinatorLayout, child: TextView, dependency: View): Boolean {
        return dependency is Button
    }

    /**
     * 当dependent view发生变化时，这个方法会被调用，参数中的child相当于本次交互行为中的观察者，
     * 观察者可以在这个方法中对被观察者的变化做出响应，从而完成一次交互行为
     */
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: TextView, dependency: View): Boolean {
        child.x = dependency.x + 150
        child.y = dependency.y + 150
        return true
    }


}