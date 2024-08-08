import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.util.SparseArray
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.asm2f.ui.fragment.AllMoviesFragment
import com.example.asm2f.ui.fragment.NowPlayingFragment
import com.example.asm2f.ui.fragment.PopularFragment
import com.example.asm2f.ui.fragment.TopRateFragment
import com.example.asm2f.ui.fragment.UpComingFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = SparseArray<Fragment>()

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> AllMoviesFragment()
            1 -> PopularFragment()
            2 -> TopRateFragment()
            3 -> UpComingFragment()
            4 -> NowPlayingFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
        fragmentList.put(position, fragment)
        return fragment
    }

    fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Movies"
            1 -> "Popular"
            2 -> "Top Rated"
            3 -> "Upcoming"
            4 -> "Now Playing"
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    fun getFragment(position: Int): Fragment? {
        return fragmentList[position]
    }
}
