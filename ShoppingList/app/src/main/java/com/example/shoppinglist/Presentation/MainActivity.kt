package com.example.shoppinglist.Presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditFinishListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)//Можно место .get(MainViewModel::class.java) написать [MainViewModel::class.java]
        viewModel.shopList.observe(this){
            shopListAdapter.submitList(it)
        }

        binding.addBtnShopItem.setOnClickListener {
            if(orientation()){
            val intent = ShopItemActivity.newIntentAdd(this)
            startActivity(intent)
            }else{
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    override fun onEditFinish() {
        Toast.makeText(this@MainActivity,"Success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun orientation() : Boolean{
        return binding.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.shop_item_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() = with(binding) {
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter
        rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_ENABLED,ShopListAdapter.MAX_PULL_SIZE)
        rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_DISABLED,ShopListAdapter.MAX_PULL_SIZE)

        setupLongClickListener()

        setupClickListener()

        setupSwipeDelete()
    }

    private fun setupSwipeDelete() = with(binding) {
        var onShopItemDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {

            // More code here
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deletShopList(item)
            }

        }

        val itemTouchHelper = ItemTouchHelper(onShopItemDelete)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if(orientation()){
            val intent = ShopItemActivity.newIntentChange(this, it.id)
            startActivity(intent)
            }else{
                launchFragment(ShopItemFragment.newInstanceChangeItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeItemEnable(it)
        }
    }

}