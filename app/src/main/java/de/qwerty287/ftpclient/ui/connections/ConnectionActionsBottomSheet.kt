package de.qwerty287.ftpclient.ui.connections

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.qwerty287.ftpclient.R
import de.qwerty287.ftpclient.data.AppDatabase
import de.qwerty287.ftpclient.data.entitites.Connection
import de.qwerty287.ftpclient.data.utils.ConnectionEntityMapper
import de.qwerty287.ftpclient.databinding.BottomSheetConnectionActionsBinding
import kotlinx.coroutines.launch

class ConnectionActionsBottomSheet : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        val KEY_CONNECTION_ID = "connectionId"

        fun newInstance(connectionId: Int): ConnectionActionsBottomSheet =
            ConnectionActionsBottomSheet().apply {
                arguments = bundleOf(Pair(KEY_CONNECTION_ID, connectionId))
            }
    }

    private var binding: BottomSheetConnectionActionsBinding? = null

    private lateinit var connection: Connection
    private lateinit var db: AppDatabase
    private val mapper by lazy { ConnectionEntityMapper() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = BottomSheetConnectionActionsBinding.inflate(inflater, container, false)
        this.binding = binding

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = checkNotNull(binding)

        db = AppDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            connection =
                db.connectionDao().get(requireArguments().getInt(KEY_CONNECTION_ID).toLong())!!
            binding.connectionName.text = connection.title
        }

        initListeners()
    }

    private fun initListeners() {
        val binding = checkNotNull(binding)

        with(binding) {
            deleteConnection.setOnClickListener {
                lifecycleScope.launch {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.delete_connection_confirmation)
                        .setMessage(R.string.delete_connection_message)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                            lifecycleScope.launch {
                                db.bookmarkDao().getAllByConnection(connection.id.toLong())
                                    .forEach {
                                        db.bookmarkDao().delete(it)
                                    }
                                db.connectionDao().delete(connection)
                            }
                        }
                        .create()
                        .show()
                    dismiss()
                }
            }

            editConnection.setOnClickListener {
                val options = bundleOf(
                    Pair(KEY_CONNECTION_ID, connection.id)
                )
                findNavController().navigate(
                    R.id.action_ConnectionsFragment_to_AddConnectionFragment,
                    options
                )
                dismiss()
            }

            copyConnection.setOnClickListener {
                lifecycleScope.launch {
                    db.connectionDao().copyElem(
                        mapper.mapToModel(connection)
                    )
                }
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val binding = checkNotNull(binding)

        with(binding) {
            deleteConnection.setOnClickListener(null)
            editConnection.setOnClickListener(null)
            copyConnection.setOnClickListener(null)
        }
        this.binding = null
    }
}