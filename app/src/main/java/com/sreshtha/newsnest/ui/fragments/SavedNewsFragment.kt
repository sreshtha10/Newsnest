package com.sreshtha.newsnest.ui.fragments

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentSavedNewsBinding
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment() {
    private var binding:FragmentSavedNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter:NewsAdapter
    private lateinit var mView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView= view

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        viewModel.getAllSavedArticles().observe(viewLifecycleOwner,{
            if(it != null){
                adapter.differ.submitList(it)
            }

        })

        viewModel.getAllSavedArticles()


        setUpSwipeToDelete()




    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun setUpRecyclerView(){
        adapter = NewsAdapter()
        binding?.savedNewsRv?.adapter = adapter
        binding?.savedNewsRv?.layoutManager = LinearLayoutManager(activity)

    }


    private fun setUpSwipeToDelete(){

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ){

            private val delIcon = ContextCompat.getDrawable(activity!!.baseContext, R.drawable.ic_delete)
            private val background = ColorDrawable()
            private val intrinsicHeight = delIcon?.intrinsicHeight
            private val intrinsicWidth = delIcon?.intrinsicWidth
            private val backgroundColor = Color.parseColor("#4e4f52")
            private val clearPaint = Paint().apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteArticle(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if(isCanceled){
                    clearCanvas(c,itemView.right+dX,itemView.top.toFloat(), itemView.right.toFloat(),itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }


                background.color = backgroundColor
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                // Calculate position of delete icon
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Draw the delete icon
                delIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                delIcon?.draw(c)





                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding?.savedNewsRv)

    }


    private fun deleteArticle(position: Int){
        val article = adapter.differ.currentList[position]
        viewModel.delete(article)
        Snackbar.make(
            mView,
            "Article Deleted",
            Snackbar.LENGTH_SHORT
        ).apply {
            setAction("Undo"){
                viewModel.insert(article)
            }
            show()
        }

        viewModel.getAllSavedArticles()
    }
}