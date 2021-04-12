package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater

        binding = ActivityLoginBinding.inflate(inflater)
        setContentView(binding.root)

        loginDialog = LoadingDialog(this)

        binding.run {

            btnSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            btnLogin.setOnClickListener {
                loginUser()
            }
        }
    }

    // buat fungsi loginUser()
    private fun loginUser() {
        // buat variabel berisi email dan password dari editText
        val email = binding.inputEmail.text.toString()
        val passWord = binding.inputPass.text.toString()

        // cek kalau email dan password itu ada
        if (email.isEmpty()) { // jika email kosong
            Toast.makeText(this, "Email harus diisi", Toast.LENGTH_SHORT).show()
            // return agar fungsi berakhir
            return
        }
        if (passWord.isEmpty()) { // jika password kosong
            Toast.makeText(this, "Password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }
        if (passWord.length < 8) { // Jika password kurang dari 8 karakter
            Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        // jika email dan password sudah sesuai
        // buat variabel untuk menghubungkan ke Firebase
        val mAuth = FirebaseAuth.getInstance()

        // tampilkan loading terlebih dahulu sebelum ke firebase
        loginDialog.startLoadingDialog()

        // masuk ke firebase dengan email dan password
        mAuth.signInWithEmailAndPassword(email, passWord)
            .addOnCompleteListener { task ->
                // jika berhasil login
                if (task.isSuccessful) {
                    // tutup loading
                    loginDialog.dismissDialog()
                    // lanjut intent ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    // addFlag menambahkan opsi pada intent
                    // CLEAR_TASK digunakan untuk menonaktifkan tombol back
                    // NEW_TASK digunakan untuk membuat activity baru
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    // tutup activity login dengan finish()
                    finish()
                } else { // jika tidak berhasil login
                    // Tampilkan Toast pesan error login
                    val message = task.exception.toString()
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    // sign Out untuk keluar dari firebase
                    mAuth.signOut()
                    // tutup loading
                    loginDialog.dismissDialog()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        // Jika user tidak null atau ada maka lanjut ke MainActivity
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}