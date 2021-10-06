package com.example.sbtechincaltest.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sbtechincaltest.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.login.setOnClickListener { viewModel.login() }
        binding.username.editText?.doAfterTextChanged {  viewModel.username = it.toString() }
        binding.password.editText?.doAfterTextChanged {  viewModel.passwword = it.toString() }

        viewModel.loginViewState.observe(viewLifecycleOwner, {
            it.loginSuccessful?.let {
                //TODO nav to photos screen
                Toast.makeText(context, "login successful", Toast.LENGTH_SHORT).show()
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