package com.example.mediforme.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mediforme.R
import com.google.android.material.tabs.TabLayout

class SearchmainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmain)
        enableEdgeToEdge()

        // TabLayout 초기화
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        // 탭 추가
        tabLayout.addTab(tabLayout.newTab().setText("아이디 찾기"))
        tabLayout.addTab(tabLayout.newTab().setText("비밀번호 찾기"))

        // 선택된 탭의 색상
        val selectedColor = getColor(R.color.boxColor)
        val unselectedColor = getColor(R.color.textStyle)

        // 탭 텍스트 색상 설정
        tabLayout.setTabTextColors(unselectedColor, selectedColor)

        // 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            // "아이디 찾기" 탭 선택 시
                            loadFragment(SearchIDFragment())
                        }
                        1 -> {
                            loadFragment(SearchpwdFragment())
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택 해제될 때 수행할 작업
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 같은 탭이 다시 선택될 때 수행할 작업
            }
        })

        if (tabLayout.tabCount > 0) {
            tabLayout.getTabAt(0)?.select() // 첫 번째 탭 선택
            loadFragment(SearchIDFragment()) // 기본적으로 아이디 찾기 프래그먼트 로드
        }

    }

    private fun loadFragment(fragment: Fragment) {
        // FragmentManager를 사용하여 Fragment를 로드합니다
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, fragment) // fragment_container는 Activity의 Fragment를 위한 컨테이너
        fragmentTransaction.addToBackStack(null) // 백 스택에 추가하여 사용자가 '뒤로' 버튼을 눌렀을 때 이전 Fragment로 돌아갈 수 있도록 함
        fragmentTransaction.commit()

    }

}