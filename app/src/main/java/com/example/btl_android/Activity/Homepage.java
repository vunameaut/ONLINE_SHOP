package com.example.btl_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SearchView;

import com.example.btl_android.R;
import com.example.btl_android.Adapter.ViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        viewPager = findViewById(R.id.view_pager);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.nav_homepage).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.nav_phone).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.nav_laptop).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.nav_pc).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        // Set up SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý tìm kiếm khi nhấn Enter
                Toast.makeText(Homepage.this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
                // Bạn có thể thực hiện hành động tìm kiếm thực tế ở đây
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng nhập văn bản vào ô tìm kiếm
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_homepage) {
            Toast.makeText(this, "Homepage", Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(0);
        }
        if (item.getItemId() == R.id.nav_phone) {
            Toast.makeText(this, "Phone", Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(1);
        }
        if (item.getItemId() == R.id.nav_laptop) {
            Toast.makeText(this, "Laptop", Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(2);
        }
        if (item.getItemId() == R.id.nav_pc) {
            Toast.makeText(this, "PC", Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(3);
        }
        if (item.getItemId() == R.id.nav_order) {
            Toast.makeText(this, "Order", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.nav_account) {
            Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.nav_setting) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
