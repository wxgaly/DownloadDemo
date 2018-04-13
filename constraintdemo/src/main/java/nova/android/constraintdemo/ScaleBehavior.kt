package nova.android.constraintdemo

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView

/**
 *  nova.android.constraintdemo.
 *
 * @author Created by WXG on 2018/4/13 013 14:03.
 * @version V1.0
 */
class ScaleBehavior : CoordinatorLayout.Behavior<ImageView> {

    constructor() : super()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {

        dependency?.apply {

            child?.apply {

                rotationX = dependency.y
                rotationY = dependency.y
//                scaleX = dependency.x - initX

            }

        }

        return super.onDependentViewChanged(parent, child, dependency)
    }

//    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: ImageView, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        child.scaleY = dy.toFloat()
//        Log.d("wxg", " $dy")
//    }


//    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: ImageView, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
//        return true
//    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: ImageView, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        child.scaleY = dyConsumed.toFloat()
        Log.d("wxg", "dyConsumed  $dyConsumed")
        Log.d("wxg", "dxConsumed  $dxConsumed")
    }
}