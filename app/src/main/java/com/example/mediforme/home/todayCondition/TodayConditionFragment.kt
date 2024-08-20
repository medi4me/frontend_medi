package com.example.mediforme.home.todayCondition

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.Data.Status
import com.example.mediforme.Data.StatusRequest
import com.example.mediforme.Data.StatusResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentTodayConditionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodayConditionFragment : Fragment() {
    lateinit var binding: FragmentTodayConditionBinding
    private lateinit var adapter: WeekDayAdapter
    private lateinit var items: List<WeekDayItem>
    private var selectedDateItem: WeekDayItem? = null
    private var todayIndex: Int = 0
    private var selectedOption: LinearLayout? = null //선택된 기분 버튼 저장변수
    private var selectedAnswerButton: Button? = null //선택된 음주여부 버튼 저장 변수
    private var selectedConditionButton: Button? = null  //선택된 컨디션 버튼 저장 변수
    private lateinit var clickedDate: String // 체크된 날짜

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // clickedDate 초기화
        val currentCalendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        clickedDate = sdf.format(currentCalendar.time)

        binding = FragmentTodayConditionBinding.inflate(inflater,container,false)

        binding.howTodayBackBtnIV.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            parentFragmentManager.popBackStack()
        }
        // 날짜 데이터 초기화
        val weekData = getWeekDates()
        items = weekData.first
        todayIndex = weekData.second

        // RecyclerView 설정
        adapter = WeekDayAdapter(items) { dateItem ->
            selectedDateItem = dateItem
            onDateItemClick(dateItem)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        // 오늘 날짜 선택
        selectTodayDate()

        // 옵션 LinearLayout 참조
        val optionGood_LL = binding.optionGoodLL
        val optionSoso_LL = binding.optionSosoLL
        val optionBad_LL = binding.optionBadLL

        //음주 여부 버튼 참조
        val btnNo = binding.btnNo
        val btnYes = binding.btnYes

        //컨티션 버튼 참조
        val BtnConditionGood = binding.condtionGoodBtn
        val BtnConditionSoso = binding.condtionSosoBtn
        val BtnConditionBad= binding.condtionBadBtn
        val editTextStatus = binding.editTextStatus
        val textViewStatus = binding.textViewStatus



        // 컨디션 리니어 버튼클릭 이벤트 설정
        optionGood_LL.setOnClickListener { selectOptionFeel(optionGood_LL, optionSoso_LL, optionBad_LL) }
        optionSoso_LL.setOnClickListener { selectOptionFeel(optionSoso_LL, optionGood_LL, optionBad_LL) }
        optionBad_LL.setOnClickListener { selectOptionFeel(optionBad_LL, optionGood_LL, optionSoso_LL) }

        //음주 여부 버튼클릭 이벤트 설정
        btnNo.setOnClickListener { selectAnswerButton(btnNo, btnYes) }
        btnYes.setOnClickListener { selectAnswerButton(btnYes, btnNo) }

        BtnConditionGood.setOnClickListener { selectConditionButton(BtnConditionGood,BtnConditionSoso,BtnConditionBad) }
        BtnConditionSoso.setOnClickListener {selectConditionButton(BtnConditionSoso,BtnConditionGood,BtnConditionBad)  }
        BtnConditionBad.setOnClickListener { selectConditionButton(BtnConditionBad,BtnConditionGood,BtnConditionSoso) }

        // 저장 버튼 클릭 이벤트 설정
        binding.saveBtn.setOnClickListener {
            onSaveButtonClick()
        }

        // 수정하기 버튼 클릭 이벤트 설정
        binding.editBtn.setOnClickListener {
            onEditButtonClick()
        }

        // EditText가 포커스를 잃었을 때 TextView로 전환
        editTextStatus.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateTextViewStatus()
            }
        }
        // TextView 클릭 시 EditText로 전환
        textViewStatus.setOnClickListener {
            textViewStatus.visibility = View.INVISIBLE
            editTextStatus.visibility = View.VISIBLE
            editTextStatus.requestFocus()
            showKeyboard(editTextStatus)
        }

        // EditText에서 엔터를 눌렀을 때 키보드 내리기 및 TextView로 전환
        editTextStatus.setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                event.action == android.view.KeyEvent.ACTION_DOWN) {
                updateTextViewStatus()
                true
            } else {
                false
            }
        }
        return binding.root
    }


    // TextView에 EditText의 내용을 설정하고 전환
    private fun updateTextViewStatus() {
        binding.textViewStatus.visibility = View.VISIBLE
        binding.editTextStatus.visibility = View.INVISIBLE
        binding.textViewStatus.text = binding.editTextStatus.text.toString()
        hideKeyboard()
    }

    // 키보드 숨기기
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    // 키보드 보이기
    private fun showKeyboard(editText: EditText) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }



    private fun selectOptionFeel(selected: LinearLayout, vararg others: LinearLayout) {
        // 선택된 옵션의 배경 및 텍스트 색상 변경
        selected.setBackgroundResource(R.drawable.option_background_select)
        val selectedText = selected.getChildAt(1) as TextView // LinearLayout의 두 번째 자식인 TextView
        selectedText.setTextColor(resources.getColor(R.color.white, null))

        // 다른 옵션들은 원래대로 돌려놓기
        for (other in others) {
            other.setBackgroundResource(R.drawable.option_background)
            val otherText = other.getChildAt(1) as TextView // LinearLayout의 두 번째 자식인 TextView
            otherText.setTextColor(resources.getColor(R.color.black, null))
        }
        // 현재 선택된 옵션을 저장
        selectedOption = selected
    }

    private fun selectAnswerButton(selected: Button, other: Button) {
        // 선택된 버튼의 배경 및 텍스트 색상 변경
        selected.setBackgroundResource(R.drawable.option_button_background_select)
        selected.setTextColor(resources.getColor(R.color.white, null))

        // 다른 버튼은 원래대로 돌려놓기
        other.setBackgroundResource(R.drawable.option_button_background)
        other.setTextColor(resources.getColor(R.color.black, null))

        // 현재 선택된 버튼을 저장
        selectedAnswerButton = selected
    }

    private fun selectConditionButton(selected:Button, vararg others:Button){
        //선택된 버튼 배경 및 텍스트 색상변경
        selected.setBackgroundResource(R.drawable.option_button_background_select)
        selected.setTextColor(resources.getColor(R.color.white,null))

        //다른 버튼 원래대로 돌려놓기
        for(other in others){
            other.setBackgroundResource(R.drawable.option_button_background)
            other.setTextColor(resources.getColor(R.color.black, null))
        }
        // 현재 선택된 컨디션 버튼을 저장
        selectedConditionButton = selected
    }

    private fun onSaveButtonClick() {
        var selectedText: String? = null
        var answerText: String? = null
        var conditionText: String? = null
        var statusText: String? = null

        // 선택된 옵션이 있는지 확인
        selectedOption?.let { option ->
            selectedText = (option.getChildAt(1) as TextView).text.toString()

            // selectedText 값에 따라 변환
            selectedText = when (selectedText) {
                "좋아요" -> "GOOD"
                "그럭저럭" -> "NOTBAD"
                "나빠요" -> "BAD"
                else -> null // 기본 값 설정
            }
        } ?: run {
            Log.d("TodayConditionFragment", "No Option Selected")
        }

        // 선택된 답변 버튼이 있는지 확인
        selectedAnswerButton?.let { button ->
            answerText = button.text.toString()

            // 아니요, 마셨어요 변환
            answerText = when (answerText) {
                "아니요" -> "NODRINK"
                "마셨어요" -> "DRINK"
                else -> null // 기본 값 설정
            }

        } ?: run {
            Log.d("TodayConditionFragment", "No Answer Selected")
        }

        // 선택된 컨디션 버튼이 있는지 확인
        selectedConditionButton?.let { button ->
            conditionText = button.text.toString()

            // selectedText 값에 따라 변환
            conditionText = when (conditionText) {
                "아주 좋아요" -> "GOOD"
                "그럭저럭" -> "NOTBAD"
                "별로에요" -> "BAD"
                else -> null // 기본 값 설정
            }

        } ?: run {
            Log.d("TodayConditionFragment", "No Condition Selected")
        }

        // 상태 메시지 가져오기
        statusText = binding.editTextStatus.text.toString()

        // 현재 날짜 가져오기
        val date = clickedDate

        // Retrofit 호출을 통해 서버로 데이터 전송
        val statusRequest = StatusRequest(
            status = selectedText,
            drink = answerText,
            statusCondition = conditionText,
            memo = statusText,
            date = date
        )

        val retrofit = getRetrofit()
        val statusService = retrofit.create(Status::class.java)

        statusService.addStatus(statusRequest).enqueue(object : Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    // 서버로부터 성공적인 응답을 받은 경우
                    val statusResponse = response.body()
                    Log.d("TodayConditionFragment", "Status saved: $statusResponse")

                    // 저장 버튼 비활성화 및 수정 버튼 활성화
                    binding.saveBtn.visibility = View.GONE
                    binding.editBtn.visibility = View.VISIBLE

                    updateTextViewStatus()
                } else {
                    // 서버 응답이 실패한 경우
                    Log.e("TodayConditionFragment", "Failed to save status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                // 네트워크 오류 등의 실패 처리
                Log.e("TodayConditionFragment", "Error saving status", t)
            }
        })
    }



    // Method to handle Edit button click (optional functionality)
    private fun onEditButtonClick() {

        var selectedText : String? = null
        var answerText : String? = null
        var conditionText : String? = null
        var statusText : String? = null

        selectedOption?.let { option ->
             selectedText = (option.getChildAt(1) as TextView).text.toString()
            //Log.d("TodayConditionFragment", "Selected Edit Option: $selectedText")
        } ?: run {
            Log.d("TodayConditionFragment", "No Option Selected")
        }

        // 선택된 답변 버튼이 있는지 확인
        selectedAnswerButton?.let { button ->
            answerText = button.text.toString()
           // Log.d("TodayConditionFragment", "Selected Edit Answer: $answerText")
        } ?: run {
            Log.d("TodayConditionFragment", "No Answer Selected")
        }
        // 선택된 컨디션 버튼이 있는지 확인
        selectedConditionButton?.let { button ->
            conditionText = button.text.toString()
            //Log.d("TodayConditionFragment", "Selected Edit Condition: $conditionText")
        } ?: run {
            Log.d("TodayConditionFragment", "No Condition Edit Selected")
        }

        // TextView에 있는 상태 메시지 전송
        statusText = binding.editTextStatus.text.toString()


        // TODO: 상태 메시지 데이터를 서버로 전송하거나 저장하는 코드 추가
        Log.d("TodayConditionFragment", "Selected Edit Option2: $selectedText")
        Log.d("TodayConditionFragment", "Selected Edit Answer2: $answerText")
        Log.d("TodayConditionFragment", "Selected Edit Condition2: $conditionText")
        Log.d("TodayConditionFragment", "Status Edit Text2: $statusText")
        Log.d("TodayConditionFragment", "Today Edit Date2 $clickedDate")
        // EditText의 내용을 TextView로 전환
        updateTextViewStatus()

    }

    private fun selectTodayDate() {
        // 오늘 날짜 아이템을 선택 상태로 설정
        val todayItem = items[todayIndex]
        todayItem.isSelected = true
        selectedDateItem = todayItem
        adapter.notifyDataSetChanged()

        // 오늘 날짜로 스크롤
        binding.recyclerView.scrollToPosition(todayIndex)

        // 현재 날짜와 시간을 가져옴
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.DAY_OF_MONTH, todayItem.date.toInt())
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val clickedDate = sdf.format(currentCalendar.time)
        binding.homeDate.text = clickedDate

        // 선택된 날짜에 해당하는 요일과 날짜로 업데이트
        binding.homeDate.text = getString(R.string.date_format, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, todayItem.date)
    }

    private fun onDateItemClick(dateItem: WeekDayItem) {
        // 날짜 아이템 클릭 시
        Log.d(TAG, "Clicked on date: ${dateItem.date}")

        // 모든 아이템의 isSelected 상태를 false로 설정
        items.forEach { it.isSelected = false }

        // 클릭한 아이템의 isSelected 상태를 true로 설정
        dateItem.isSelected = true

        // 어댑터에 알림을 보냄
        adapter.notifyDataSetChanged()

        // 현재 날짜와 시간을 가져옴
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.MONTH, dateItem.month - 1) // Calendar.MONTH는 0부터 시작하므로 -1
        currentCalendar.set(Calendar.DAY_OF_MONTH, dateItem.date.toInt())

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        clickedDate = sdf.format(currentCalendar.time)

        binding.homeDate.text = clickedDate

        // 선택된 날짜에 해당하는 요일과 날짜로 업데이트
        binding.homeDate.text = getString(R.string.date_format, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, dateItem.date)

        // 화면을 초기 상태로 되돌리기... api 연결되면 삭제할 예정 이부분은
        resetViewToInitialState()

        // api연결해서 선택된 날짜 정보들 가져와서 선택(저장 되어있던 애들 선택)
        // 선택한 날짜에 해당하는 데이터 가져오기
        //fetchDataForDate(clickedDate) /// 이부분은 api연결할 때 저 함수 안에서 호출되게 설정해야함
    }

    //옵션 선택 초기화 api연결하면 사용 안함 ..
    private fun resetViewToInitialState() {
        // 옵션 선택 초기화
        val options = listOf(binding.optionGoodLL, binding.optionSosoLL, binding.optionBadLL)
        options.forEach {
            it.setBackgroundResource(R.drawable.option_background)
            val text = it.getChildAt(1) as TextView
            text.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        selectedOption = null

        // 답변 버튼 초기화
        val answerButtons = listOf(binding.btnNo, binding.btnYes)
        answerButtons.forEach {
            it.setBackgroundResource(R.drawable.option_button_background)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        selectedAnswerButton = null

        // 컨디션 버튼 초기화
        val conditionButtons = listOf(binding.condtionGoodBtn, binding.condtionSosoBtn, binding.condtionBadBtn)
        conditionButtons.forEach {
            it.setBackgroundResource(R.drawable.option_button_background)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        selectedConditionButton = null

        // 텍스트 필드 초기화
        binding.editTextStatus.text = null
        binding.textViewStatus.visibility = View.GONE
        binding.editTextStatus.visibility = View.VISIBLE

        // 저장 버튼 보이기 및 수정 버튼 숨기기
        binding.saveBtn.visibility = View.VISIBLE
        binding.editBtn.visibility = View.GONE
    }



    // 다른 날짜 아이템 버튼 눌렀을 때, 정보 가져와서 밑에 프래그먼트에 저장된 값들 선택되어있게
    private fun fetchDataForDate(date: String) {
        // TODO: 여기에 서버 API 호출 코드를 추가예정
        // Retrofit를 사용하여 데이터를 가져온다

        // 가정: 서버에서 받아온 데이터를 아래와 같이 가정
        val fetchedOption = "GOOD" // 서버에서 받아온 기분 (GOOD, NOTBAD, BAD)
        val fetchedAnswer = "NODRINK" // 서버에서 받아온 음주 여부 (DRINK, NODRINK)
        val fetchedCondition = "NOTBAD" // 서버에서 받아온 컨디션 (GOOD, NOTBAD, BAD)
        val fetchedStatusText = "오늘은 좋은 날입니다." // 서버에서 받아온 상태 메시지

        // 데이터를 UI에 반영하는 함수 호출
        updateUIWithFetchedData(fetchedOption, fetchedAnswer, fetchedCondition, fetchedStatusText)
    }
    private fun updateUIWithFetchedData(option: String?, answer: String?, condition: String?, statusText: String?) {
        // 기분 옵션을 선택
        when (option) {
            "GOOD" -> selectOptionFeel(binding.optionGoodLL, binding.optionSosoLL, binding.optionBadLL)
            "NOTBAD" -> selectOptionFeel(binding.optionSosoLL, binding.optionGoodLL, binding.optionBadLL)
            "BAD" -> selectOptionFeel(binding.optionBadLL, binding.optionGoodLL, binding.optionSosoLL)
        }

        // 음주 여부 버튼 선택
        when (answer) {
            "DRINK" -> selectAnswerButton(binding.btnYes, binding.btnNo)
            "NODRINK" -> selectAnswerButton(binding.btnNo, binding.btnYes)
        }

        // 컨디션 버튼 선택
        when (condition) {
            "GOOD" -> selectConditionButton(binding.condtionGoodBtn, binding.condtionSosoBtn, binding.condtionBadBtn)
            "NOTBAD" -> selectConditionButton(binding.condtionSosoBtn, binding.condtionGoodBtn, binding.condtionBadBtn)
            "BAD" -> selectConditionButton(binding.condtionBadBtn, binding.condtionGoodBtn, binding.condtionSosoBtn)
        }

        // 상태 메시지 업데이트
        binding.editTextStatus.setText(statusText)
        binding.textViewStatus.text = statusText

        // 상태 메시지 TextView를 보이도록 설정
        binding.textViewStatus.visibility = View.VISIBLE
        binding.editTextStatus.visibility = View.INVISIBLE

        // 수정 모드로 변경
        binding.saveBtn.visibility = View.GONE
        binding.editBtn.visibility = View.VISIBLE
    }
}