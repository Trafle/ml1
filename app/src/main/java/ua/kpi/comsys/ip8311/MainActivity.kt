package ua.kpi.comsys.ip8311

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setContentView(R.layout.secondary_activity)
        var vp2 : ViewPager2 = findViewById(R.id.viewpager)
        vp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3;
            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return BusinessCardActivity()
                } else if (position == 1) {
                    return GraphActivity()
                } else if (position == 2){
                    return BooksActivity()
                } else {
                    return error("lol")
                }
            }
        }

        var tabLayout : TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, vp2) { tab, _ ->
            vp2.setCurrentItem(tab.position, true)
        }.attach()

        val tabTextFont = ResourcesCompat.getFont(this, R.font.minecrafter)

        tabLayout.getTabAt(0)?.apply{
            customView?.setOnClickListener(){
                vp2.setCurrentItem(0)
            }
        }?.setIcon(R.drawable.ic_tab_business_card)?.setText("GENERAL")

        tabLayout.getTabAt(1)?.apply {
            customView?.setOnClickListener(){
                vp2.setCurrentItem(1)
            }
        }?.setIcon(R.drawable.ic_tab_graphs)?.setText("GRAPHS")

        tabLayout.getTabAt(2)?.apply {
            customView?.setOnClickListener(){
                vp2.setCurrentItem(2)
            }
        }?.setIcon(R.drawable.ic_tab_books)?.setText("BOOKS")
    }

}
