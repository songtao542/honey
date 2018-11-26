package com.snt.phoney.ui.user

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.EditUserFragmentBinding
import com.snt.phoney.domain.model.CityPickerConverter
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.utils.Picker
import com.zaaach.citypicker.model.City
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = EditUserFragment().apply {
            this.arguments = arguments
        }
    }

    var user: User = User()

    var binding by autoCleared<EditUserFragmentBinding>()

    private lateinit var viewModel: EditUserViewModel

    private var selectedCities: List<City>? = null

    private lateinit var cupNumberArray: Array<Int>
    private lateinit var cupSizeArray: Array<String>

    private var cupNumber: Int = 0
    private var cupSize: Int = 0

    private fun initCupArray() {
        cupNumberArray = Array(11) { 30 + it }
        cupSizeArray = arrayOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCupArray()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.fragment_edit_user, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_user, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar, true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditUserViewModel::class.java)
        binding.user = viewModel.user

        viewModel.user?.let {
            user = it.copy()
        }

        toolbar.setNavigationOnClickListener { activity?.finish() }


        cityButton.setOnClickListener {
            Picker.toggleCityPicker(activity, { cityPicker ->
                viewModel.cities.observe(this@EditUserFragment, Observer {
                    cityPicker.setCities(CityPickerConverter.convert(it))
                })
            }) { cities ->
                if (cities.isNotEmpty()) {
                    selectedCities = cities
                    var text = StringBuilder()
                    selectedCities!!.forEach { city ->
                        text.append(city.name).append(" ")
                    }
                    city.text = text.toString()
                }
            }
        }
        heightButton.setOnClickListener {
            Picker.togglePicker(activity, getString(R.string.pick_height), 150, 230, user.height, "height") { value, _ ->
                height.text = getString(R.string.height_value_template, value)
                user.height = value
            }
        }
        weightCupButton.setOnClickListener {
            if (viewModel.user?.sex == Sex.MALE.value) {
                Picker.togglePicker(activity, getString(R.string.pick_weight), 40, 150, user.weight.toInt(), "weight") { value, _ ->
                    weightCup.text = getString(R.string.weight_value_template, value)
                    user.weight = value.toDouble()
                }
            } else if (viewModel.user?.sex == Sex.FEMALE.value) {
                Picker.togglePicker(activity, getString(R.string.pick_cup), cupNumberArray, cupNumber, cupSizeArray, cupSize, "cup") { value1, value2 ->
                    val cupValue = "${cupNumberArray[value1]}${cupSizeArray[value2]}"
                    cupNumber = value1
                    cupSize = value2
                    weightCup.text = cupValue
                    user.cup = cupValue
                }
            }
        }

        ageButton.setOnClickListener {
            Picker.togglePicker(activity, getString(R.string.pick_age), 16, 70, user.age, "age") { value, _ ->
                age.text = getString(R.string.age_value_template, value)
                user.age = value
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.edit_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.confirm -> {
                if (isValid()) {
                    var cities = java.lang.StringBuilder()
                    selectedCities?.forEach { city ->
                        cities.append(city.code).append(",")
                    }
                    cities.delete(cities.length - 1, cities.length)
                    val introduce: String = introduce.text?.toString() ?: ""
                    val nickname: String = nickname.text?.toString() ?: ""
                    val wechatAccount: String = wechatAccount.text?.toString() ?: ""
                    viewModel.setFullUserInfo(user.height, user.weight, user.age, user.cup
                            ?: "", cities.toString(), introduce, user.career
                            ?: "", "", wechatAccount, nickname)
                    true
                } else {
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValid(): Boolean {
        val nickname = nickname.text
        val age = age.text
        val height = height.text
        val wc = weightCup.text
        if (TextUtils.isEmpty(nickname) ||
                TextUtils.isEmpty(age) ||
                TextUtils.isEmpty(height) ||
                TextUtils.isEmpty(wc)) {
            return false
        }
        return true
    }
}
