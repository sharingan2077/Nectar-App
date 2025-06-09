package ru.android.nectar.ui.account

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.nectar.databinding.FragmentAboutBinding
import androidx.core.net.toUri


class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater)

        binding.imgTelegram.setOnClickListener {
            val url = "https://t.me/sunawr"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = url.toUri()
            }
            startActivity(intent)
        }

        binding.textPrivacyPolicy.setOnClickListener {
            showPrivacyDialog()
        }

        return binding.root
    }

    private fun showPrivacyDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Политика конфиденциальности")
            .setMessage(
                "Мы уважаем вашу конфиденциальность. Все предоставленные данные используются " +
                        "исключительно для обработки заказов и улучшения работы приложения. Мы не передаём ваши данные третьим лицам."
            )
            .setPositiveButton("ОК", null)
            .show()
    }


}