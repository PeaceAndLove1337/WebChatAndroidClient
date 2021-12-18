package com.example.webchatclient.ui.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.activity.ChooseChatActivity
import com.example.webchatclient.ui.presentation.activity.LoginActivity
import com.example.webchatclient.ui.presentation.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var loginText: EditText
    private lateinit var passwordText: EditText
    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        findViews(rootView)
        initViewModel()
        initObservers()
        initButtons()

        return rootView
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
    }

    private fun initObservers() {
        mViewModel.getResultOfRegistrationLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
        mViewModel.getResultOfLoginLiveData().observe(viewLifecycleOwner) {
            if (it.second != null) {
                val intent = Intent(activity, ChooseChatActivity::class.java)
                intent.putExtra(LoginActivity.API_KEY, it.second)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), it.first, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun findViews(rootView: View) {
        loginButton = rootView.findViewById(R.id.button_login)
        registerButton = rootView.findViewById(R.id.button_register)
        loginText = rootView.findViewById(R.id.editTextLogin)
        passwordText = rootView.findViewById(R.id.editTextPassword)
    }

    private fun initButtons() {
        loginButton.setOnClickListener {
            mViewModel.loginUser(
                loginText.text.toString(),
                passwordText.text.toString()
            )
        }

        registerButton.setOnClickListener {
            if (checkThatFieldsAreNotEmpty()) {
                if (passwordText.text.length < 4) {
                    makeShortPasswordToast()
                } else {
                    mViewModel.registerUser(
                        loginText.text.toString(),
                        passwordText.text.toString()
                    )
                }
            }

        }
    }

    /**
     * Проверка возможности регистрации или логина
     * @return true, если пользователь ввел данные в оба поля, false если хотя бы одно поле пустое
     */
    private fun checkThatFieldsAreNotEmpty() = passwordText.text.isNotBlank() && loginText.text.isNotBlank()

    /**
     * Показать тост, уведомляющий о слишком коротком пароле
     */
    private fun makeShortPasswordToast() =
        Toast.makeText(requireContext(), "Password length must be longer than 4", Toast.LENGTH_LONG).show()

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}