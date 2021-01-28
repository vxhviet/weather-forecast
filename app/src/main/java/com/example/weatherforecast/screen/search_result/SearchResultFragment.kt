package com.example.weatherforecast.screen.search_result

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.WeatherForecastApplication
import com.example.weatherforecast.base.BaseFragment
import com.example.weatherforecast.data.source.DefaultForecastRepository
import com.example.weatherforecast.util.hideKeyboard
import com.example.weatherforecast.util.observeEvent
import com.example.weatherforecast.util.showHideDividerOnScroll
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 * Created by viet on 1/19/21.
 */
class SearchResultFragment : BaseFragment<SearchResultViewModel>() {
    override val viewModel: SearchResultViewModel by viewModels {
        SearchResultViewModelFactory((requireContext().applicationContext as WeatherForecastApplication).forecastRepository)
    }

    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter(listOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fsr_result_list_rc.showHideDividerOnScroll(fsr_header_divider)

        fsr_search_edt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.onSearchButtonClicked(fsr_search_edt.text)
                    return true
                }
                return false
            }
        })

        fsr_search_btn.setOnClickListener {
            viewModel.onSearchButtonClicked(fsr_search_edt.text)
        }

        fsr_result_list_rc.run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SearchResultFragment.adapter
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        viewModel.invalidSearchLengthEvent.observeEvent(viewLifecycleOwner) {
            val content = getString(R.string.invalid_search_length, it)
            Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
        }

        viewModel.forecastResultListLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.dismissKeyboardEvent.observeEvent(viewLifecycleOwner) {
            fsr_search_edt.hideKeyboard()
            fsr_search_edt.clearFocus()
        }
    }
}