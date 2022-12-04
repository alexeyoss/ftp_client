package de.qwerty287.ftpclient.ui.connections

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import de.qwerty287.ftpclient.R
import de.qwerty287.ftpclient.data.AppDatabase
import de.qwerty287.ftpclient.databinding.FragmentConnectionsBinding


class ConnectionsFragment : Fragment(R.layout.fragment_connections) {

    private var binding: FragmentConnectionsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val binding = FragmentConnectionsBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = checkNotNull(binding)

        initMenuProvider()

        binding.fabAddConnection.setOnClickListener {
            findNavController().navigate(R.id.action_ConnectionsFragment_to_AddConnectionFragment)
        }
    }

    private fun initMenuProvider() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.connection_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.bookmarks_menu -> {
                            findNavController().navigate(R.id.action_ConnectionsFragment_to_BookmarksFragment)
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    override fun onResume() {
        super.onResume()
        showConnections()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding = null
    }

    /**
     * Get and display the connections
     */
    private fun showConnections() {
        val binding = checkNotNull(binding)

        AppDatabase.getInstance(requireContext())
            .connectionDao().getAll()
            .observe(viewLifecycleOwner) { connectionsList ->
                with(binding) {
                    if (connectionsList.isEmpty()) {
                        noConnections.isVisible = true
                        recyclerviewMain.adapter = null
                    } else {
                        recyclerviewMain.layoutManager = if (connectionsList.size == 1) {
                            LinearLayoutManager(requireContext())
                        } else {
                            GridLayoutManager(requireContext(), 2)
                        }
                        recyclerviewMain.adapter = ConnectionAdapter(
                            connectionsList,
                            findNavController(),
                            requireActivity().supportFragmentManager
                        )
                    }
                }
            }
    }
}