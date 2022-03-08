package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minesweeper.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    // val mySB = Snackbar.make(binding.root, "Test Msg", Snackbar.LENGTH_LONG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val mySB = Snackbar.make(binding.root, "Test Msg", Snackbar.LENGTH_LONG)
//        mySB.setAction("Ok") {
//            binding.msgDisplay.text = "SNACKBAR"
//        }.show()

        // starts the game on default
        binding.minesweeperView.resetGame()

        binding.btnReset.setOnClickListener {
            binding.minesweeperView.resetGame()
            binding.interactionToggle.isChecked = false
        }

        binding.interactionToggle.setOnCheckedChangeListener {_, isChecked ->
//            if (isChecked) {
//            // Flag mode
//                binding.minesweeperView.setInteractionMode(true)
//            } else {
//                // The toggle is disabled
//                binding.btnReset.text = "toggle disabled"
//            }
            binding.minesweeperView.setFlagMode(isChecked)
        }

    }

//    fun snackBarMsg(msg: String) {
//        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
//    }

}