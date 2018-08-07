package nova.android.slidingcarddemo.view

import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.View
import nova.android.slidingcarddemo.utils.TypeUtils

/**
 *  nova.android.slidingcarddemo.view.
 *
 * @author Created by WXG on 2018/8/7 007 14:12.
 * @version V1.0
 */
class SlidingCardBehavior : CoordinatorLayout.Behavior<SlidingCardLayout>() {

    private val TAG = "SlidingCardBehavior"
    private var mInitialOffset: Int = 0

    override fun onMeasureChild(parent: CoordinatorLayout, child: SlidingCardLayout, parentWidthMeasureSpec: Int,
                                widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        //进行偏移
        val offset = getChildMeasureOffset(parent, child)
        val height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        Log.d(TAG, "The type of parent is ${TypeUtils.analysisClassInfo(this)}")
        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: SlidingCardLayout, layoutDirection: Int): Boolean {

        //默认进行摆放子view
        parent.onLayoutChild(child, layoutDirection)

        //给子view进行偏移
        val previous: SlidingCardLayout? = getPreviousChild(parent, child)
        previous?.apply {
            val offset = top + headerViewHeight
            child.offsetTopAndBottom(offset)
        }

        mInitialOffset = child.top

        return true
    }

    private fun getPreviousChild(parent: CoordinatorLayout, child: SlidingCardLayout): SlidingCardLayout? {

        val cardIndex = parent.indexOfChild(child)
        for (i in cardIndex - 1 downTo 0) {
            val view = parent.getChildAt(i)
            if (view is SlidingCardLayout) {
                return view
            }
        }
        return null
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: SlidingCardLayout,
                                     directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        //判断是否是垂直拖拽操作
        val isVertical = axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) != 0
        return isVertical && child == directTargetChild
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: SlidingCardLayout, target: View,
                                   dx: Int, dy: Int, consumed: IntArray, type: Int) {
        consumed[1] = scroll(
                child,
                dy,
                mInitialOffset,
                mInitialOffset + child.height - child.headerViewHeight)

        shiftSlidings(consumed[1], coordinatorLayout, child)

    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: SlidingCardLayout, target: View,
                                dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {

        val shift = scroll(
                child,
                dyUnconsumed,
                mInitialOffset,
                mInitialOffset + child.height - child.headerViewHeight)

        shiftSlidings(shift, coordinatorLayout, child)

    }

    private fun shiftSlidings(shift: Int, parent: CoordinatorLayout, child: SlidingCardLayout) {
        if (shift == 0) {
            return
        }

        if (shift > 0) { // 向上推

            var current = child
            var card = getPreviousChild(parent, current)

            while (card != null) {

                val offset = getHeaderOverlap(card, current)
                if (offset > 0) {
                    card.offsetTopAndBottom(-offset)
                }

                current = card
                card = getPreviousChild(parent, current)

            }

        } else {// 向下推

            var current = child
            var card = getNextChild(parent, current)

            while (card != null) {

                val offset = getHeaderOverlap(current, card)
                if (offset > 0) {
                    card.offsetTopAndBottom(offset)
                }

                current = card
                card = getNextChild(parent, current)

            }

        }
    }

    private fun getHeaderOverlap(above: SlidingCardLayout, below: SlidingCardLayout): Int = above.top + above
            .headerViewHeight - below.top

    private fun getNextChild(parent: CoordinatorLayout, child: SlidingCardLayout): SlidingCardLayout? {

        val cardIndex = parent.indexOfChild(child) + 1
        val childCount = parent.childCount

        for (i in cardIndex..childCount) {
            val view = parent.getChildAt(i)
            if (view is SlidingCardLayout) {
                return view
            }
        }

        return null
    }

    private fun scroll(child: SlidingCardLayout, dyUnconsumed: Int, minOffset: Int, maxOffset: Int): Int {
        val initialOffset = child.top
        val offset = clamp(initialOffset - dyUnconsumed, minOffset, maxOffset) - initialOffset
        child.offsetTopAndBottom(offset)

        return -offset
    }

    private fun clamp(i: Int, minOffset: Int, maxOffset: Int): Int = when {
        i > maxOffset -> maxOffset
        i < minOffset -> minOffset
        else -> i
    }

    private fun getChildMeasureOffset(parent: CoordinatorLayout, child: SlidingCardLayout): Int {

        var offset = 0

        //遍历每一个child的header的高度，相加
        for (i in 0..parent.childCount) {
            val view = parent.getChildAt(i)
            if (view != child && view is SlidingCardLayout) {
                offset += view.headerViewHeight
            }
        }

        return offset
    }

}