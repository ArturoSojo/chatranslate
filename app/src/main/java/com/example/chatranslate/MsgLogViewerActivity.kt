package com.example.chatranslate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatranslate.databinding.ActivityMsgLogViewerBinding
import java.io.File
import java.io.PrintWriter
import java.lang.Exception

class MsgLogViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMsgLogViewerBinding
    private var msgLogFileName = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding
        binding = ActivityMsgLogViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        msgLogFileName =
            if (intent.getStringExtra("app") == "whatsapp") "msgLog.txt" else "signalMsgLog.txt"

        // Set up RecyclerView
        binding.msgLogRecyclerView.adapter = MsgLogAdapter(readFile(File(this.filesDir, msgLogFileName)))
        binding.msgLogRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.msgLogRecyclerView.setHasFixedSize(true)

        // Set up SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshMsgLog()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun readFile(fileName: File): List<String> = fileName.bufferedReader().readLines().asReversed()

    private fun refreshMsgLog() {
        binding.msgLogRecyclerView.adapter = MsgLogAdapter(readFile(File(this.filesDir, msgLogFileName)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_msg_log_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                refreshMsgLog()
                true
            }
            R.id.action_clear -> {
                AlertDialogHelper.showDialog(
                    this@MsgLogViewerActivity,
                    getString(R.string.clear_msg_log),
                    getString(R.string.clear_msg_log_confirm),
                    getString(R.string.yes),
                    getString(R.string.cancel)
                ) { _, _ ->
                    try {
                        PrintWriter(
                            File(
                                this.filesDir,
                                msgLogFileName
                            )
                        ).use { out -> out.println("") }
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.cleared),
                            Toast.LENGTH_SHORT
                        ).show()
                        refreshMsgLog()
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.clear_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
