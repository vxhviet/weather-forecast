package com.example.weatherforecast.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.data.source.Result

/**
 * Created by viet on 1/19/21.
 */
abstract class BaseFragment<T : BaseViewModel> : Fragment() {
    open var tagName: String? = javaClass.simpleName
    lateinit var navController: NavController
    abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // when using <FragmentContainerView> instead of <fragment> in xml (recommended),
        // if we can't retrieve the NavController from the supportFragmentManger, we can use the Navigation.findNavController()
        // just not in onCreate().
        navController = findNavController()
        observeLiveData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            viewModel.isLoadingEvent.postValue(false)
    }

    open fun observeLiveData() {
        observeLiveData(viewModel)
    }

    fun observeLiveData(vm: BaseViewModel) {
        vm.isLoadingEvent.observe(viewLifecycleOwner, Observer<Boolean> {
            it?.let {
                onChangedLoadingStatus(it)
            }
        })
        vm.errorLiveData.observe(viewLifecycleOwner, Observer<Result.Error> {
            it?.let {
                onError(it)
                vm.errorLiveData.value = null
            }
        })
    }

    open fun onChangedLoadingStatus(isShowLoader: Boolean) {
        val act = requireActivity()
        if (act is BaseActivity) {
            act.onChangedLoadingStatus(isShowLoader)
        }
    }

    open fun onError(error: Result.Error) {
        val act = requireActivity()
        if (act is BaseActivity) {
            act.onError(error)
        }
    }

}