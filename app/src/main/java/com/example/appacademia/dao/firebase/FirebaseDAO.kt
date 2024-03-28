package com.example.appacademia.dao.firebase

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase que proporciona métodos estáticos para obtener instancias de Firebase Firestore y Firebase Auth.
 */
class FirebaseDAO {
    companion object {
        /**
         * Instancia de FirebaseFirestore para acceder a la base de datos Firestore.
         */
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCESTORE: FirebaseFirestore? = null/**
         * Instancia de FirebaseAuth para autenticación de usuarios.
         */
        private var INSTANCEAUTH: FirebaseAuth? = null

        /**
         * Método estático para obtener la instancia de FirebaseFirestore.
         * Si INSTANCESTORE es nulo, se crea una nueva instancia, de lo contrario, se devuelve la instancia existente.
         */
        fun getInstanceFirestore(): FirebaseFirestore {
            synchronized(this) {
                if (INSTANCESTORE == null)
                    INSTANCESTORE = FirebaseFirestore.getInstance()
                return INSTANCESTORE as FirebaseFirestore
            }
        }

        /**
         * Método estático para obtener la instancia de FirebaseAuth.
         * Si INSTANCEAUTH es nulo, se crea una nueva instancia, de lo contrario, se devuelve la instancia existente.
         */
        fun getInstanceFirebase(): FirebaseAuth {
            synchronized(this) {
                if (INSTANCEAUTH == null)
                    INSTANCEAUTH = FirebaseAuth.getInstance()
                return INSTANCEAUTH as FirebaseAuth
            }
        }
    }
}