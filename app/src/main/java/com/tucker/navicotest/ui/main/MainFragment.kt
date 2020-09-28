package com.tucker.navicotest.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tucker.navicotest.R

/**
 * This is where the View resides
 * It uses the viewmodel delegate with the viewmodel factory to make the ViewModel
 * RecyclerView is all pretty standard stuff, except that I'm using ListAdapter instead of a direct implementation of RecyclerView.Adapter
 * NOT IMPLEMENTED but would like to do for future:
 * -Add in preferences or some other way to specify the owner and repo parameters so that any github repository could be queried
 * (functionality for specifying the above is there, just no way in the UI to set them to anything different)
 * -An optional request was to add in functionality to show a person's location (if available) in a maps view of some sort - ran out of time for this
 */

class MainFragment : Fragment() {

	companion object {
		fun newInstance() = MainFragment()
	}

	private lateinit var refreshButton: Button
	private lateinit var spinner: ProgressBar

	//NOTE: layoutmanager for this set in the layout file - it's just a LinearLayoutManager and we don't need to do anything special for it
	private lateinit var recyclerView: RecyclerView
	private lateinit var recyclerViewAdapter: GithubContributorAdapter

	//viewmodel property delegate from the android fragment-ktx package
	private val viewModel: MainViewModel by viewModels({this}, {MainViewModelFactory(
		GithubContributorRepository(getGithubNetworkService())
	)})

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View {
		val view = inflater.inflate(R.layout.main_fragment, container, false)
		//could have used View Binding, just went vanilla for now
		refreshButton = view.findViewById(R.id.refreshButton)
		spinner = view.findViewById(R.id.spinner)
		recyclerView = view.findViewById(R.id.recyclerview)
		recyclerViewAdapter = GithubContributorAdapter(Glide.with(this))
		recyclerView.adapter = recyclerViewAdapter

		val decorationCount = recyclerView.itemDecorationCount
		if (decorationCount > 0) {
			for (i in decorationCount downTo 0) {
				recyclerView.removeItemDecorationAt(i)
			}
		}

		//adds in a small gap between items, except for the last one
		recyclerView.addItemDecoration(VerticalSpaceItemDecorator(resources.getDimensionPixelOffset(R.dimen.recycler_item_spacing)))

		refreshButton.setOnClickListener {
			//default params for owner and repo are "cli" and "cli"
			//This will show the github repo https://github.com/cli/cli
			//this was top of trending today!
			viewModel.updateContributorList()
		}

		//show/hide loading spinner - this is set by the coroutine
		viewModel.loading.observe(viewLifecycleOwner) {
			it.let { show ->
				spinner.visibility = if (show) View.VISIBLE else View.GONE
			}
		}

		//show a toast if there was a problem with getting the data from the github API
		viewModel.toast.observe(viewLifecycleOwner) { text ->
			text?.let {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
				viewModel.onToastShown()
			}
		}

		//observe our contributor list - when it changes we update our adapter
		viewModel.contributorList.observe(viewLifecycleOwner) {
			recyclerViewAdapter.submitList(it)
		}

		return view
	}

	class VerticalSpaceItemDecorator(private val verticalSpace: Int): RecyclerView.ItemDecoration() {

		override fun getItemOffsets(
			outRect: Rect,
			view: View,
			parent: RecyclerView,
			state: RecyclerView.State
		) {
			super.getItemOffsets(outRect, view, parent, state)
			parent.adapter?.let { adapter ->
				//only do this if the adapter is set and we're not the last item in the list
				if (parent.getChildLayoutPosition(view) != adapter.itemCount - 1) {
					outRect.bottom = verticalSpace
				}
			}
		}
	}

}