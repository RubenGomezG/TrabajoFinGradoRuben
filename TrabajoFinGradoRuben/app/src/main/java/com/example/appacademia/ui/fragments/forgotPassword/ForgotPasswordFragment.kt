package com.example.appacademia.ui.fragments.forgotPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.appacademia.R
import com.example.appacademia.databinding.FragmentForgotPasswordBinding
import com.example.appacademia.databinding.FragmentLoginBinding
import com.example.appacademia.ui.activities.MainActivity
import com.example.appacademia.ui.fragments.login.LoginViewModel

class ForgotPasswordFragment : Fragment() {
    private lateinit var btnEnviar: Button
    private lateinit var navController: NavController
    private var _binding: FragmentForgotPasswordBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    /***
     * Crear la vista y enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return devuelve la vista proyectada en el fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val forgotPasswordViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        navController = (activity as MainActivity).getNavController()

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root


        btnEnviar = binding.btnEnviarEmail
        btnEnviar.setOnClickListener {
            enviarEmail(root)
        }

        return root
    }

    /***
     * Al destruir la vista, borramos el contenido del binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Comprueba que el campo email está rellenado
     * y que existe. Después, manda el correo.
     *
     * @param view - vista actual
     */
    fun enviarEmail(view: View) {
        if(binding.campoEmail.text.length > 0){
            val mensaje = "¡Email enviado con éxito!"
            val duracion = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext().applicationContext, mensaje, duracion)
            toast.show()

            gotoLogin()
        }else{
            val mensaje = "Introduzca un email válido, por favor"
            val duracion = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext().applicationContext, mensaje, duracion)
            toast.show()
        }
    }

    /**
     * Ir al login
     */
    fun gotoLogin() {
        navController.navigate(R.id.navigation_login)
    }

}
