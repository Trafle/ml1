package ua.kpi.comsys.ip8311

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        println("WENT INTO MAIN ACTIVITY")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setContentView(R.layout.secondary_activity)
        var vp2 : ViewPager2 = findViewById(R.id.kakoi_nebud)

        vp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2;
            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return SecondaryActivity()
                } else if (position == 1) {
                    return TertiaryActivity()
                } else {
                    return error("lol")
                }
            }
        }

        var tabLayout : TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, vp2) { tab, position ->
            vp2.setCurrentItem(tab.position, true)
        }.attach()

        tabLayout.getTabAt(0)?.apply{
            customView?.setOnClickListener(){
                vp2.setCurrentItem(0)
            }
        }?.setIcon(R.drawable.ic_tab_general)?.setText("GENERAL")

        tabLayout.getTabAt(1)?.apply {
            customView?.setOnClickListener(){
                vp2.setCurrentItem(1)
            }
        }?.setIcon(R.drawable.ic_tab_secondary)?.setText("STUFF")
    }

}
