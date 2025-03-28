package ru.android.nectar

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.nectar.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNavigationView = binding.bottomNavView

        setStatusBarColor()
        window.isNavigationBarContrastEnforced = false
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)


        val bottomNavFragments = setOf(
            R.id.shopFragment,
            R.id.exploreFragment,
            R.id.cartFragment,
            R.id.favouriteFragment,
            R.id.accountFragment,
            R.id.exploreFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in bottomNavFragments) {
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
            }
        }
//        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_shop -> {
                    navController.navigate(R.id.shopFragment)
                    true
                }
                R.id.menu_explore -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                R.id.menu_cart -> {
                    navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.menu_favourite -> {
                    navController.navigate(R.id.favouriteFragment)
                    true
                }
                R.id.menu_account -> {
                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(Color.TRANSPARENT)
                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

}
