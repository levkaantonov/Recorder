package levkaantonov.com.study.recorder.ui.fragments.list_of_records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import levkaantonov.com.study.recorder.databinding.RecordItemBinding
import levkaantonov.com.study.recorder.data.db.Record
import java.util.concurrent.TimeUnit

class ListOfRecordsAdapter(
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ListOfRecordsAdapter.RecordViewHolder>() {
    var data = listOf<Record>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = RecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        if (position == -1) {
            return
        }
        val item = data[position] ?: return
        holder.bind(item)
    }


    override fun getItemCount(): Int = data.size

    inner class RecordViewHolder(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val record = data[position]
                        onClickListener.playRecord(record.path)
                    }
                }

                root.setOnLongClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val record = data[position]
                        onClickListener.removeItem(record.path, record.id)
                    }
                    false
                }
            }
        }

        fun bind(item: Record) {
            binding.apply {
                val duration = item.length
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                val seconds =
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MILLISECONDS.toSeconds(
                        minutes
                    )
                tvFileLength.text = String.format("%02d:%02d", minutes, seconds)
                tvFileName.text = item.name
            }
        }
    }

}

interface OnClickListener {
    fun playRecord(filePath: String)
    fun removeItem(itemPath: String, recordId: Int)
}