package mc.apps.safe.tools

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_start.*

abstract class McSwipeCallback(private val context: Context) : ItemTouchHelper.Callback() {

    /*interface OnItemClickListener {
        fun onItemClick(position:Int)
    }*/

    private val tag = "tests"
    private val p = Paint()
    private val corners = 8f

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //Swipe to Left only
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        //..
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if(dX>0){
            return
        }
        drawBackground(c, recyclerView, viewHolder, dX)

        recyclerView.setOnTouchListener { _: View?, event : MotionEvent? ->
            if(event?.action == MotionEvent.ACTION_UP){

                setItemsClickable(recyclerView, true)
                if (leftRect.contains(event.x, event.y)) {
                    //listener?.onItemClick(viewHolder.adapterPosition)
                    val result = ItemClicked(viewHolder.adapterPosition)
                }else{
                    super@McSwipeCallback.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive)
                }
            }
            false
        }
    }

    abstract fun ItemClicked(adapterPosition: Int): String

    private lateinit var leftRect : RectF

    private fun drawBackground(c: Canvas,
                               recyclerView: RecyclerView,
                               viewHolder: RecyclerView.ViewHolder,
                               dX: Float) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height

        val background = ColorDrawable()
        background.color = Color.TRANSPARENT
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        val unlockIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock_lock)
        val intrinsicWidth = unlockIcon?.intrinsicWidth
        val intrinsicHeight = unlockIcon?.intrinsicHeight
        val iconMargin = (itemHeight - intrinsicHeight!!) / 2

        val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
        val iconRight = itemView.right - iconMargin
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight

        unlockIcon.setBounds(iconLeft, iconTop,iconRight, iconBottom)
        unlockIcon.draw(c)

        val buttonWidth = itemView.width-140F
        leftRect = RectF(
            itemView.left.toFloat(),
            itemView.top.toFloat(),
            itemView.left + buttonWidth-20,
            itemView.bottom.toFloat()
        )

        p.color = Color.TRANSPARENT
        c.drawRoundRect(leftRect, corners, corners, p)

        val position = viewHolder.adapterPosition
        val adapter : FromDbAdapter = recyclerView.adapter as FromDbAdapter
        val item = adapter.getData(position)

        drawText(item.password, c, leftRect, p)
    }

    private fun drawText(text: String, c: Canvas, rect: RectF, p: Paint) {
        val textSize = 50f
        p.color = Color.YELLOW
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text, rect.centerX() - textWidth / 2, rect.centerY() + textSize / 2, p)
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }


    private fun log(message: String) {
        Log.i(tag, "*********************************************")
        Log.i(tag, message)
        Log.i(tag, "*********************************************")
    }


}