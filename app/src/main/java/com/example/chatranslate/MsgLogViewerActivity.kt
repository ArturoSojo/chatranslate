package com.example.chatranslate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatranslate.databinding.ActivityMsgLogViewerBinding
import java.io.File
import java.io.PrintWriter

class MsgLogViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMsgLogViewerBinding
    private lateinit var msgLogFileName: String
    private lateinit var msgLogFile: File
    private lateinit var adapter: MsgLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMsgLogViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener nombre de archivo según app
        msgLogFileName = when (intent.getStringExtra("app")) {
            "whatsapp" -> "msgLog.txt"
            "signal" -> "signalMsgLog.txt"
            else -> {
                Toast.makeText(this, "App desconocida", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }

        msgLogFile = File(filesDir, msgLogFileName)
        setupRecyclerView()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        adapter = MsgLogAdapter(readLog())
        binding.msgLogRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MsgLogViewerActivity)
            setHasFixedSize(true)
            adapter = this@MsgLogViewerActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshMsgLog()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun readLog(): List<String> {
        return if (msgLogFile.exists()) {
            msgLogFile.readLines().asReversed()
        } else {
            emptyList()
        }
    }

    private fun refreshMsgLog() {
        adapter.updateData(readLog())
    }

    private fun clearLog() {
        try {
            PrintWriter(msgLogFile).use { it.println("") }
            Toast.makeText(this, R.string.cleared, Toast.LENGTH_SHORT).show()
            refreshMsgLog()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.clear_failed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
                    this,
                    getString(R.string.clear_msg_log),
                    getString(R.string.clear_msg_log_confirm),
                    getString(R.string.yes),
                    getString(R.string.cancel)
                ) { _, _ -> clearLog() }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
