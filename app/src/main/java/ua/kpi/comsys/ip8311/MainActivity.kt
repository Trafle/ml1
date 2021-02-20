package ua.kpi.comsys.ip8311

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tomclaw.cache.DiskLruCache

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        cache = DiskLruCache.create(cacheDir, CACHE_SIZE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var vp2 : ViewPager2 = findViewById(R.id.viewpager)

        // Init margin parameters
        val booksPageMargins = vp2.layoutParams as ViewGroup.MarginLayoutParams
        booksPageMargins.setMargins(0, 0, 30, 0)
        val defaultMarignParams = vp2.layoutParams as ViewGroup.MarginLayoutParams
        defaultMarignParams.setMargins(0, 0, 0, 0)

        vp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4;
            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    vp2.layoutParams = defaultMarignParams
                    return BusinessCardFragment()
                } else if (position == 1) {
                    vp2.layoutParams = defaultMarignParams
                    return GraphFragment()
                } else if (position == 2){
                    vp2.layoutParams = booksPageMargins
                    return BooksFragment()
                } else if (position == 3){
                    vp2.layoutParams = defaultMarignParams
                    return CollageFragment()
                } else {
                    return error("lol")
                }
            }
        }

        //
        var tabLayout : TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, vp2) { tab, _ ->
            vp2.setCurrentItem(tab.position, true)
        }.attach()

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

        tabLayout.getTabAt(3)?.apply {
            customView?.setOnClickListener(){
                vp2.setCurrentItem(3)
            }
        }?.setIcon(R.drawable.collage_tab_icon)?.setText("COLLAGE")
    }

}
