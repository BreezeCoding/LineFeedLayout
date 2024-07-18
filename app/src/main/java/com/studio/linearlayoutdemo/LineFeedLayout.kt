package com.studio.linearlayoutdemo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class LineFeedLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var horizontalGap: Int = 0
    var verticalGap: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 获取容器控件高度模式
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        // 获取容器的宽度
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) { // 当前容器的高度被指定为精确的数值
            height = MeasureSpec.getSize(heightMeasureSpec)
        } else { // 手动计算容器的高度
            // 容器剩余的宽度
            var remainWidth = width
            (0 until childCount).map { getChildAt(it) }.forEach { child ->
                val layoutParams = child.layoutParams as MarginLayoutParams
                if (isNewLine(
                        lp = layoutParams,
                        child = child,
                        remainWidth = remainWidth,
                        horizontalGap = horizontalGap
                    )
                ) {
                    height += (layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight + verticalGap)
                    remainWidth = width - child.measuredWidth
                } else {
                    remainWidth -= child.measuredWidth
                    if (height == 0) height =
                        (layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight + verticalGap)
                }

                remainWidth -= (layoutParams.leftMargin + layoutParams.rightMargin + horizontalGap)
            }
        }
        setMeasuredDimension(width, height)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var lastBottom = 0
        (0 until childCount).map { getChildAt(it) }.forEach { child ->
            val lp = child.layoutParams as MarginLayoutParams
            if (isNewLine(
                    lp = lp,
                    child = child,
                    remainWidth = r - l - left,
                    horizontalGap = horizontalGap
                )
            ) {
                left = -lp.leftMargin
                top = lastBottom
                lastBottom = 0
            }

            val childLeft = left + lp.leftMargin
            val childTop = top + lp.topMargin
            child.layout(
                childLeft,
                childTop,
                childLeft + child.measuredWidth,
                childTop + child.measuredHeight
            )

            if (lastBottom == 0) lastBottom = child.bottom + lp.bottomMargin + verticalGap
            left += child.measuredWidth + lp.leftMargin + lp.rightMargin + horizontalGap
        }
    }

    /**
     * 判断是否需要新起一行
     */
    private fun isNewLine(
        lp: MarginLayoutParams,
        child: View,
        remainWidth: Int,
        horizontalGap: Int
    ): Boolean {
        val childOccupation = lp.leftMargin + lp.rightMargin + child.measuredWidth
        return (childOccupation + horizontalGap > remainWidth) && (childOccupation > remainWidth)
    }

}