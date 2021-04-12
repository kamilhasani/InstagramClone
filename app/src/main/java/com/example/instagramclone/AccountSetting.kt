package com.example.instagramclone

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import com.example.instagramclone.databinding.ActivityAccountSettingBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSetting : AppCompatActivity() {
    // viewbinding untuk activity_account_setting.xml
    private lateinit var binding: ActivityAccountSettingBinding

    // buat variabel userInfo berisi database reference
    private lateinit var userInfo: DatabaseReference

    // buat variabel user yang berisi model user
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pengaturan viewbinding dimulai
        val inflater = layoutInflater
        binding = ActivityAccountSettingBinding.inflate(inflater)
        setContentView(binding.root)
        // pengaturan viewbinding selesai

        // setting agar button logout bisa logout dari firebase
        // dan kembali ke activity login
        binding.btnLogout.setOnClickListener {
            // signout / logout dari firebase
            FirebaseAuth.getInstance().signOut()
            // intent menuju activity login
            val intent = Intent(this, LoginActivity::class.java)
            // add flags agar tombol back tidak bisa diklik
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            // mulai activity intent
            startActivity(intent)
            // finish (hapus) activity account setting activity
            finish()
        }

        // tombol close diberi setOnClickListener finish untuk menutup activity
        binding.btnClose.setOnClickListener {
            finish()
        }

        // tombol centang diberi setonclick untuk mengupdate info
        binding.btnAccept.setOnClickListener {
            updateUserInfo()
        }

        // jika ada user yang login
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            // dapatkan UID dari User yang login
            val uidUser = currentUser.uid
            // dapatkan user info yang login berdasarkan UIDnya
            userInfo = FirebaseDatabase.getInstance()
                .reference
                // child users berisi nama folder dari user yang ada di firebase realtime database
                // nama ini harus persis
                .child("users")
                .child(uidUser)

            // jika ada user yang login maka tombol delete akun bisa diklik
            // tombol delete akun setonclick untuk menghapus akun
            binding.btnDelete.setOnClickListener {
//                buat credential berisi email dan password dari user
                val password = "12341234"
                val emailUser = currentUser.email.toString()

                val credential = EmailAuthProvider.getCredential(emailUser, password)
                // reauthenticate untuk login ulang dari sistem
                currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) { // jika berhasil login
                        // hapus user saat ini yang ada di authenticate firebase
                        currentUser.delete()
                        // hapus user infonya juga yang ada di realtime database firebase
                        userInfo.removeValue()
                        // hapus user selesai
                        // Logout dari firebase
                        FirebaseAuth.getInstance().signOut()
                        // intent menuju activity login
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        // finish (hapus) activity account setting activity
                        finish()
                    } else {
                        Log.e("ErrorReauthentic", task.exception.toString())
                    }
                }
            }

            // ambil data dari userInfo
            userInfo.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // jadikan data dari firebase menjadi data class User
                            user = snapshot.getValue(User::class.java) as User
                            binding.run {
                                // Masukkan data name, username, dan Bio ke dalam EditText
                                inputName.text = SpannableStringBuilder(user.fullname)
                                inputUsername.text = SpannableStringBuilder(user.username)
                                inputBio.text = SpannableStringBuilder(user.Bio)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
        }
    }

    // buat private fungsi updateUserInfo() untuk menyimpan info user ke dalam database
    // private fun itu fungsi yang hanya bisa diakses oleh fungsi lain di dalam class yang sama
    private fun updateUserInfo() {
        // akses semua input text yang ada di account setting activity
        binding.run {
            val fullName = inputName.text.toString()
            val userName = inputUsername.text.toString()
            val userBio = inputBio.text.toString()

            // cek apakah semua data terisi
            if (fullName.isEmpty()) {
                Toast.makeText(
                    this@AccountSetting,
                    "Nama Harus Diisi !",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            if (userName.isEmpty()) {
                Toast.makeText(
                    this@AccountSetting,
                    "Username Harus Diisi !",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }



            // buat userMap yang menyimpan data terupdate
            // nama variabel dalam userMap harus persis sama dengan yang ada di firebase
            val userMap = HashMap<String, Any>()
            userMap["Bio"] = userBio
            userMap["email"] = user.email
            userMap["fullname"] = fullName
            userMap["image"] = user.image
            userMap["uid"] = user.uid
            userMap["username"] = userName

            // Update data yang ada pada firebase
            userInfo.updateChildren(userMap)
            Toast.makeText(this@AccountSetting, "User Telah Diupdate", Toast.LENGTH_SHORT)
                .show()
        }
    }


}