package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minesweeper.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // starts the game on default
        binding.minesweeperView.resetGame()
        binding.score.text = getString(R.string.flags_remaining_label, 3)
        snackBarMsg(getString(R.string.new_game))

        // Resets the game on click of RESET button
        binding.btnReset.setOnClickListener {
            binding.minesweeperView.resetGame()
            binding.interactionToggle.isChecked = false
        }

        // Handles the toggling for Reveal and Flag functionalities
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

    // Creates SnackBar messages
    fun snackBarMsg(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }
    // updates the remaining flag count
    fun updateRemainingFlags(score: Int) {
        binding.score.text = getString(R.string.flags_remaining_label, score)
    }

}