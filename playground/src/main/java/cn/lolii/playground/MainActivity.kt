package cn.lolii.playground

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import city.picker.SampleCityPickerActivity
import expandable.sample.ExpansionPanelMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import photo.picker.sample.SamplePhotoPickerActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityPicker.setOnClickListener {
            startActivity(Intent(this@MainActivity, SampleCityPickerActivity::class.java))
        }
        photoPicker.setOnClickListener {
            startActivity(Intent(this@MainActivity, SamplePhotoPickerActivity::class.java))
        }
        expandable.setOnClickListener {
            startActivity(Intent(this@MainActivity, SampleExpandableActivity::class.java))
        }
        expandableSample.setOnClickListener {
            startActivity(Intent(this@MainActivity, ExpansionPanelMainActivity::class.java))
        }

        apiLevel.text = "API:${Build.VERSION.SDK_INT}"
    }
}
