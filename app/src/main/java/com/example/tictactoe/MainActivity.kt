package com.example.tictactoe

import android.database.CrossProcessCursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.databinding.FragmentGameBinding
import com.example.tictactoe.databinding.FragmentScoreBinding
import android.R
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class MainActivity : FragmentActivity() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    //private MutableLiveData<String> currentTurn = Turn.CROSS

    val currentTurn: MutableLiveData<Turn> by lazy {
        MutableLiveData<Turn>()
    }

    private var noughtsScore = 0
    private var crossesScore = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding: FragmentGameBinding
    private lateinit var score: FragmentScoreBinding
    private lateinit var main: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentGameBinding.inflate(layoutInflater)
        main = ActivityMainBinding.inflate(layoutInflater)
        score = FragmentScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameObserver = Observer<Turn> { newName ->
            // Update the UI, in this case, a TextView.
            var turnText = ""
            if (currentTurn.value == Turn.CROSS)
                turnText = "Turn $CROSS"
            else if (currentTurn.value == Turn.NOUGHT)
                turnText = "Turn $NOUGHT"

            binding.turnTV.text = turnText
        }

        currentTurn.observe(this, nameObserver);
        currentTurn.value = Turn.CROSS;

        initBoard()
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    fun boardTapped(view: android.view.View) {
        if (view !is Button)
            return
        addToBoard(view)

        if (checkForVictory(NOUGHT)) {
            noughtsScore++
            result("Noughts Win!")
        }

        if (checkForVictory(CROSS)) {
            crossesScore++
            result("Crosses Win!")
        }

        if (fullBoard()){
            result("Draw")
        }
    }

    private fun checkForVictory(s: String): Boolean {
        //Horizontal Victory
        if (match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if (match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if (match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        //Vertical Victory
        if (match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if (match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if (match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        //Diagonal Victory
        if (match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if (match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true

        return false
    }

    private fun match(button: Button, symbol : String) = button.text == symbol

    private fun result(title: String) {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset") {
                _,_ ->
                resetBoard()
            }
            .setNegativeButton("Send e-mail") {
                    _,_ ->
                sendEmail()
            }
            .setCancelable(false)
            .show()
    }

    private fun sendEmail() {
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null
            )
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, "Tic tac toe!");
        intent.putExtra(Intent.EXTRA_TEXT, "I've won");

        startActivity(intent)
        resetBoard()
    }

    private fun resetBoard() {
        for (button in boardList) {
            button.text = ""
        }

        if (firstTurn == Turn.NOUGHT)
            firstTurn = Turn.CROSS
        else if (firstTurn == Turn.CROSS)
            firstTurn = Turn.NOUGHT

        currentTurn.value = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean {
        for (button in boardList){
            if (button.text == "")
                return false
        }
        return true
    }

    private fun addToBoard(button: Button) {
        if (button.text != "")
            return

        if (currentTurn.value == Turn.NOUGHT) {
            button.text = NOUGHT
            currentTurn.value = Turn.CROSS
        }
        else if (currentTurn.value == Turn.CROSS) {
            button.text = CROSS
            currentTurn.value = Turn.NOUGHT
        }
        setTurnLabel()
    }

    private fun setTurnLabel() {
        var turnText = ""
        if (currentTurn.value == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if (currentTurn.value == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }

    companion object{
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
}