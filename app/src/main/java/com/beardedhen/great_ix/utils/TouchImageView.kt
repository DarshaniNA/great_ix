package com.beardedhen.great_ix.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs


/**
 * Extends Android ImageView to include pinch zooming and panning.
 */
class TouchImageView : AppCompatImageView {

    companion object {
        // We can be in one of these 3 states
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val CLICK = 3
    }

    var transformMatrix = Matrix()
    var mode = NONE

    // Remember some things for zooming
    var last = PointF()
    var start = PointF()
    var minScale = 1f
    var maxScale = 3f
    var m = FloatArray(9)
    var redundantXSpace = 0f
    var redundantYSpace = 0f
    var width = 0f
    var height = 0f
    var saveScale = 1f
    var right = 0f
    var bottom = 0f
    var origWidth = 0f
    var origHeight = 0f
    var bmWidth = 0f
    var bmHeight = 0f
    var mScaleDetector: ScaleGestureDetector? = null


    constructor(context: Context) : super(context) {
        sharedConstructing(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        transformMatrix.setTranslate(1f, 1f)
        imageMatrix = transformMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener { v, event ->
            mScaleDetector!!.onTouchEvent(event)
            transformMatrix.getValues(m)
            val x = m[Matrix.MTRANS_X]
            val y = m[Matrix.MTRANS_Y]
            val curr = PointF(event.x, event.y)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    last[event.x] = event.y
                    start.set(last)
                    mode = DRAG
                }
                MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                    var deltaX = curr.x - last.x
                    var deltaY = curr.y - last.y
                    val scaleWidth =
                        Math.round(origWidth * saveScale).toFloat()
                    val scaleHeight =
                        Math.round(origHeight * saveScale).toFloat()
                    if (scaleWidth < width) {
                        deltaX = 0f
                        if (y + deltaY > 0) deltaY = -y else if (y + deltaY < -bottom) deltaY =
                            -(y + bottom)
                    } else if (scaleHeight < height) {
                        deltaY = 0f
                        if (x + deltaX > 0) deltaX = -x else if (x + deltaX < -right) deltaX =
                            -(x + right)
                    } else {
                        if (x + deltaX > 0) deltaX = -x else if (x + deltaX < -right) deltaX =
                            -(x + right)
                        if (y + deltaY > 0) deltaY = -y else if (y + deltaY < -bottom) deltaY =
                            -(y + bottom)
                    }
                    transformMatrix.postTranslate(deltaX, deltaY)
                    last[curr.x] = curr.y
                }
                MotionEvent.ACTION_UP -> {
                    mode = NONE
                    val xDiff = abs(curr.x - start.x).toInt()
                    val yDiff = abs(curr.y - start.y).toInt()
                    if (xDiff < CLICK && yDiff < CLICK) performClick()
                }
                MotionEvent.ACTION_POINTER_UP -> mode = NONE
            }
            imageMatrix = transformMatrix
            invalidate()
            true // indicate event was handled
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (bm != null) {
            bmWidth = bm.width.toFloat()
            bmHeight = bm.height.toFloat()
        }
    }

    fun setMaxZoom(x: Float) {
        maxScale = x
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = Math.min(
                Math.max(.95f, detector.scaleFactor).toDouble(), 1.05
            ).toFloat()
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            right = width * saveScale - width - 2 * redundantXSpace * saveScale
            bottom = (height * saveScale - height
                    - 2 * redundantYSpace * saveScale)
            if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
                transformMatrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2)
                if (mScaleFactor < 1) {
                    transformMatrix.getValues(m)
                    val x = m[Matrix.MTRANS_X]
                    val y = m[Matrix.MTRANS_Y]
                    if (mScaleFactor < 1) {
                        if (Math.round(origWidth * saveScale) < width) {
                            if (y < -bottom) transformMatrix.postTranslate(
                                0f,
                                -(y + bottom)
                            ) else if (y > 0) transformMatrix.postTranslate(0f, -y)
                        } else {
                            if (x < -right) transformMatrix.postTranslate(
                                -(x + right),
                                0f
                            ) else if (x > 0) transformMatrix.postTranslate(-x, 0f)
                        }
                    }
                }
            } else {
                transformMatrix.postScale(
                    mScaleFactor, mScaleFactor, detector.focusX,
                    detector.focusY
                )
                transformMatrix.getValues(m)
                val x = m[Matrix.MTRANS_X]
                val y = m[Matrix.MTRANS_Y]
                if (mScaleFactor < 1) {
                    if (x < -right) transformMatrix.postTranslate(
                        -(x + right),
                        0f
                    ) else if (x > 0) transformMatrix.postTranslate(-x, 0f)
                    if (y < -bottom) transformMatrix.postTranslate(
                        0f,
                        -(y + bottom)
                    ) else if (y > 0) transformMatrix.postTranslate(0f, -y)
                }
            }
            return true
        }
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        // Fit to screen.
        val scale: Float
        val scaleX = width / bmWidth
        val scaleY = height / bmHeight
        scale = Math.min(scaleX, scaleY)
        transformMatrix.setScale(scale, scale)
        imageMatrix = transformMatrix
        saveScale = 1f

        // Center the image
        redundantYSpace = height - scale * bmHeight
        redundantXSpace = width - scale * bmWidth
        redundantYSpace /= 2.toFloat()
        redundantXSpace /= 2.toFloat()
        transformMatrix.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = width - 2 * redundantXSpace
        origHeight = height - 2 * redundantYSpace
        right = width * saveScale - width - 2 * redundantXSpace * saveScale
        bottom = height * saveScale - height - 2 * redundantYSpace * saveScale
        imageMatrix = transformMatrix
    }


    val gd = GestureDetector(context, object : SimpleOnGestureListener() {
        //here is the method for double tap
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    })
}