package com.example.sbtechincaltest.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.sbtechincaltest.R
import com.example.sbtechincaltest.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModel()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener { viewModel.login() }
        binding.username.editText?.doAfterTextChanged { viewModel.username = it.toString() }
        binding.password.editText?.apply {
            doAfterTextChanged {
                viewModel.password = it.toString()
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        viewModel.login()
                        true
                    }
                    else -> false
                }
            }
        }

        viewModel.loginViewState.observe(viewLifecycleOwner, {
            it.loginSuccessful?.let {
                binding.root.findNavController()
                    .navigate(R.id.action_loginFragment_to_photosFragment)
            }

            binding.username.error = it.usernameError
            binding.password.error = it.passwordError
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}